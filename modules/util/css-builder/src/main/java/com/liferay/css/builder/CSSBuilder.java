/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.css.builder;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import com.liferay.css.builder.internal.util.CSSBuilderUtil;
import com.liferay.css.builder.internal.util.FileUtil;
import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.rtl.css.RTLCSSConverter;
import com.liferay.sass.compiler.SassCompiler;
import com.liferay.sass.compiler.SassCompilerException;
import com.liferay.sass.compiler.jni.internal.JniSassCompiler;
import com.liferay.sass.compiler.ruby.internal.RubySassCompiler;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author Brian Wing Shun Chan
 * @author Raymond Augé
 * @author Eduardo Lundgren
 * @author Shuyang Zhou
 * @author David Truong
 * @author Christopher Bryan Boyd
 */
public class CSSBuilder implements AutoCloseable {

	public static void main(String[] args) throws Exception {
		CSSBuilderArgs cssBuilderArgs = new CSSBuilderArgs();

		JCommander jCommander = new JCommander(cssBuilderArgs);

		try {
			File jarFile = FileUtil.getJarFile();

			if (jarFile.isFile()) {
				jCommander.setProgramName("java -jar " + jarFile.getName());
			}
			else {
				jCommander.setProgramName(CSSBuilder.class.getName());
			}

			jCommander.parse(args);

			if (cssBuilderArgs.isHelp()) {
				_printHelp(jCommander);
			}
			else {
				try (CSSBuilder cssBuilder = new CSSBuilder(cssBuilderArgs)) {
					cssBuilder.execute();
				}
			}
		}
		catch (ParameterException pe) {
			System.err.println(pe.getMessage());

			_printHelp(jCommander);

			System.exit(1);
		}
	}

	public CSSBuilder(CSSBuilderArgs cssBuilderArgs) throws Exception {
		_cssBuilderArgs = cssBuilderArgs;

		if (_cssBuilderArgs.getBaseDir() == null) {
			_cssBuilderArgs.setBaseDir(
				new File(System.getProperty("user.dir")));
		}

		File importDir = _cssBuilderArgs.getImportDir();

		if (importDir != null) {
			if (importDir.isFile()) {
				importDir = _unzipImport(importDir);

				_cleanImportDir = true;
			}
			else {
				_cleanImportDir = false;
			}

			_importDirName = importDir.getCanonicalPath();
		}
		else {
			_cleanImportDir = false;
			_importDirName = null;
		}

		if (_cssBuilderArgs.getIncludes() == null) {
			_cssBuilderArgs.setIncludes(StringPool.BLANK);
		}

		List<String> rtlExcludedPathRegexps =
			_cssBuilderArgs.getRtlExcludedPathRegexps();

		_rtlExcludedPathPatterns = new Pattern[rtlExcludedPathRegexps.size()];

		for (int i = 0; i < rtlExcludedPathRegexps.size(); i++) {
			_rtlExcludedPathPatterns[i] = Pattern.compile(
				rtlExcludedPathRegexps.get(i));
		}

		_initSassCompiler(_cssBuilderArgs.getSassCompilerClassName());
	}

	@Override
	public void close() throws Exception {
		if (_cleanImportDir) {
			FileUtil.deltree(Paths.get(_importDirName));
		}

		_sassCompiler.close();
	}

	public void execute() throws Exception {
		List<String> fileNames = new ArrayList<>();

		File baseDir = _cssBuilderArgs.getBaseDir();

		if (!baseDir.exists()) {
			throw new IOException("Directory " + baseDir + " does not exist");
		}

		for (String dirName : _cssBuilderArgs.getIncludes()) {
			List<String> sassFileNames = _collectSassFiles(dirName, baseDir);

			fileNames.addAll(sassFileNames);
		}

		if (fileNames.isEmpty()) {
			System.out.println("There are no files to compile");

			return;
		}

		for (String fileName : fileNames) {
			long startTime = System.currentTimeMillis();

			_parseSassFile(fileName);

			System.out.println(
				StringBundler.concat(
					"Parsed ", fileName, " in ",
					String.valueOf(System.currentTimeMillis() - startTime),
					"ms"));
		}
	}

	public boolean isRtlExcludedPath(String filePath) {
		for (Pattern pattern : _rtlExcludedPathPatterns) {
			Matcher matcher = pattern.matcher(filePath);

			if (matcher.matches()) {
				return true;
			}
		}

		return false;
	}

	private static void _printHelp(JCommander jCommander) throws Exception {
		jCommander.usage();
	}

	private List<String> _collectSassFiles(String dirName, File baseDir)
		throws Exception {

		List<String> fileNames = new ArrayList<>();

		String basedir = new File(baseDir, dirName).toString();

		String[] scssFiles = _getScssFiles(basedir);

		if (!_isModified(basedir, scssFiles)) {
			long oldestSassModifiedTime = _getOldestModifiedTime(
				basedir, scssFiles);

			String[] scssFragments = _getScssFragments(basedir);

			long newestFragmentModifiedTime = _getNewestModifiedTime(
				basedir, scssFragments);

			if (oldestSassModifiedTime > newestFragmentModifiedTime) {
				return fileNames;
			}
		}

		for (String fileName : scssFiles) {
			if (fileName.contains("_rtl")) {
				continue;
			}

			fileNames.add(_normalizeFileName(dirName, fileName));
		}

		return fileNames;
	}

	private long _getNewestModifiedTime(String baseDir, String[] fileNames) {
		Stream<String> stream = Stream.of(fileNames);

		return stream.map(
			fileName -> Paths.get(baseDir, fileName)
		).map(
			FileUtil::getLastModifiedTime
		).max(
			Comparator.naturalOrder()
		).orElse(
			Long.MIN_VALUE
		);
	}

	private long _getOldestModifiedTime(String baseDir, String[] fileNames) {
		Stream<String> stream = Stream.of(fileNames);

		return stream.map(
			fileName -> Paths.get(baseDir, fileName)
		).map(
			FileUtil::getLastModifiedTime
		).min(
			Comparator.naturalOrder()
		).orElse(
			Long.MIN_VALUE
		);
	}

	private String _getRtlCss(String fileName, String css) throws Exception {
		String rtlCss = css;

		try {
			if (_rtlCSSConverter == null) {
				_rtlCSSConverter = new RTLCSSConverter();
			}

			rtlCss = _rtlCSSConverter.process(rtlCss);
		}
		catch (Exception e) {
			System.out.println(
				StringBundler.concat(
					"Unable to generate RTL version for ", fileName,
					StringPool.COMMA_AND_SPACE, e.getMessage()));
		}

		return rtlCss;
	}

	private String[] _getScssFiles(String baseDir) throws IOException {
		String[] fragments = {"**/_*.scss"};
		String[] includes = {"**/*.scss"};

		Stream<String[]> stream = Stream.of(fragments, _EXCLUDES);

		String[] excludes = stream.flatMap(
			Stream::of
		).toArray(
			String[]::new
		);

		return FileUtil.getFilesFromDirectory(baseDir, includes, excludes);
	}

	private String[] _getScssFragments(String baseDir) throws IOException {
		String[] includes = {"**/_*.scss"};

		return FileUtil.getFilesFromDirectory(baseDir, includes, _EXCLUDES);
	}

	private void _initSassCompiler(String sassCompilerClassName)
		throws Exception {

		int precision = _cssBuilderArgs.getPrecision();

		if ((sassCompilerClassName == null) ||
			sassCompilerClassName.isEmpty() ||
			sassCompilerClassName.equals("jni")) {

			try {
				System.setProperty("jna.nosys", Boolean.TRUE.toString());

				_sassCompiler = new JniSassCompiler(precision);

				System.out.println("Using native Sass compiler");
			}
			catch (Throwable t) {
				System.out.println(
					"Unable to load native compiler, falling back to Ruby");

				_sassCompiler = new RubySassCompiler(precision);
			}
		}
		else {
			try {
				_sassCompiler = new RubySassCompiler(precision);

				System.out.println("Using Ruby Sass compiler");
			}
			catch (Exception e) {
				System.out.println(
					"Unable to load Ruby compiler, falling back to native");

				System.setProperty("jna.nosys", Boolean.TRUE.toString());

				_sassCompiler = new JniSassCompiler(precision);
			}
		}
	}

	private boolean _isModified(String dirName, String[] fileNames)
		throws Exception {

		for (String fileName : fileNames) {
			if (fileName.contains("_rtl")) {
				continue;
			}

			fileName = _normalizeFileName(dirName, fileName);

			File file = new File(fileName);
			File cacheFile = CSSBuilderUtil.getOutputFile(
				fileName, _cssBuilderArgs.getOutputDirName());

			if (file.lastModified() != cacheFile.lastModified()) {
				return true;
			}
		}

		return false;
	}

	private String _normalizeFileName(String dirName, String fileName) {
		fileName = dirName + StringPool.SLASH + fileName;

		fileName = fileName.replace(CharPool.BACK_SLASH, CharPool.SLASH);
		fileName = fileName.replace(StringPool.DOUBLE_SLASH, StringPool.SLASH);

		return fileName;
	}

	private String _parseSass(String fileName) throws SassCompilerException {
		File sassFile = new File(_cssBuilderArgs.getBaseDir(), fileName);

		String filePath = sassFile.toPath().toString();

		String cssBasePath = filePath;

		int pos = filePath.lastIndexOf("/css/");

		if (pos >= 0) {
			cssBasePath = filePath.substring(0, pos + 4);
		}
		else {
			pos = filePath.lastIndexOf("/resources/");

			if (pos >= 0) {
				cssBasePath = filePath.substring(0, pos + 10);
			}
		}

		String css = _sassCompiler.compileFile(
			filePath, _importDirName + File.pathSeparator + cssBasePath,
			_cssBuilderArgs.isGenerateSourceMap(), filePath + ".map");

		return css;
	}

	private void _parseSassFile(String fileName) throws Exception {
		File file = new File(_cssBuilderArgs.getBaseDir(), fileName);

		if (!file.exists()) {
			return;
		}

		String ltrContent = _parseSass(fileName);

		_writeOutputFile(fileName, ltrContent, false);

		if (isRtlExcludedPath(fileName)) {
			return;
		}

		String rtlContent = _getRtlCss(fileName, ltrContent);

		String rtlCustomFileName = CSSBuilderUtil.getRtlCustomFileName(
			fileName);

		File rtlCustomFile = new File(
			_cssBuilderArgs.getBaseDir(), rtlCustomFileName);

		if (rtlCustomFile.exists()) {
			rtlContent += _parseSass(rtlCustomFileName);
		}

		_writeOutputFile(fileName, rtlContent, true);
	}

	private File _unzipImport(File importFile) throws IOException {
		Path portalCommonCssDirPath = Files.createTempDirectory(
			"cssBuilderImport");

		try (ZipFile zipFile = new ZipFile(importFile)) {
			Enumeration<? extends ZipEntry> enumeration = zipFile.entries();

			while (enumeration.hasMoreElements()) {
				ZipEntry zipEntry = enumeration.nextElement();

				String name = zipEntry.getName();

				if (name.endsWith(StringPool.SLASH) ||
					!name.startsWith("META-INF/resources/")) {

					continue;
				}

				name = name.substring(19);

				Path path = portalCommonCssDirPath.resolve(name);

				Files.createDirectories(path.getParent());

				Files.copy(
					zipFile.getInputStream(zipEntry), path,
					StandardCopyOption.REPLACE_EXISTING);
			}
		}

		System.out.println(portalCommonCssDirPath);

		return portalCommonCssDirPath.toFile();
	}

	private void _writeOutputFile(String fileName, String content, boolean rtl)
		throws Exception {

		if (_cssBuilderArgs.isAppendCssImportTimestamps()) {
			content = CSSBuilderUtil.parseCSSImports(content);
		}

		String outputFileName;

		boolean absoluteOutputDir = false;
		String outputFileDirName = _cssBuilderArgs.getOutputDirName();

		if (FileUtil.isAbsolute(outputFileDirName)) {
			absoluteOutputDir = true;
			outputFileDirName = StringPool.BLANK;
		}

		if (rtl) {
			String rtlFileName = CSSBuilderUtil.getRtlCustomFileName(fileName);

			outputFileName = CSSBuilderUtil.getOutputFileName(
				rtlFileName, outputFileDirName, StringPool.BLANK);
		}
		else {
			outputFileName = CSSBuilderUtil.getOutputFileName(
				fileName, outputFileDirName, StringPool.BLANK);
		}

		File outputFile;

		if (absoluteOutputDir) {
			outputFile = new File(
				_cssBuilderArgs.getOutputDirName(), outputFileName);
		}
		else {
			outputFile = new File(_cssBuilderArgs.getBaseDir(), outputFileName);
		}

		FileUtil.write(outputFile, content);

		File file = new File(_cssBuilderArgs.getBaseDir(), fileName);

		outputFile.setLastModified(file.lastModified());
	}

	private static final String[] _EXCLUDES = {
		"**/_diffs/**", "**/.sass-cache*/**", "**/.sass_cache_*/**",
		"**/_sass_cache_*/**", "**/_styled/**", "**/_unstyled/**",
		"**/css/aui/**", "**/tmp/**"
	};

	private static RTLCSSConverter _rtlCSSConverter;

	private final boolean _cleanImportDir;
	private final CSSBuilderArgs _cssBuilderArgs;
	private final String _importDirName;
	private final Pattern[] _rtlExcludedPathPatterns;
	private SassCompiler _sassCompiler;

}