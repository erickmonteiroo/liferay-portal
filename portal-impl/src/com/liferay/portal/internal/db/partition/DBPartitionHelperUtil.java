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

package com.liferay.portal.internal.db.partition;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.db.DB;
import com.liferay.portal.kernel.dao.db.DBInspector;
import com.liferay.portal.kernel.dao.db.DBManagerUtil;
import com.liferay.portal.kernel.dao.db.DBType;
import com.liferay.portal.kernel.dao.jdbc.CurrentConnectionUtil;
import com.liferay.portal.kernel.dao.orm.ORMException;
import com.liferay.portal.kernel.model.CompanyConstants;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.spring.hibernate.DialectDetector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * @author Alberto Chaparro
 */
public class DBPartitionHelperUtil {

	public static boolean addPartition(long companyId) {
		if (!_DATABASE_PARTITION_ENABLED) {
			return false;
		}

		if (companyId == _defaultCompanyId) {
			return false;
		}

		Connection connection = CurrentConnectionUtil.getConnection(
			InfrastructureUtil.getDataSource());

		try {
			try (PreparedStatement preparedStatement =
					connection.prepareStatement(
						"create schema if not exists " +
							_getSchemaName(companyId) +
								" character set utf8")) {

				preparedStatement.executeUpdate();
			}

			DatabaseMetaData databaseMetaData = connection.getMetaData();

			DBInspector dbInspector = new DBInspector(connection);

			try (ResultSet resultSet = databaseMetaData.getTables(
					dbInspector.getCatalog(), dbInspector.getSchema(), null,
					new String[] {"TABLE"});
				Statement statement = connection.createStatement()) {

				while (resultSet.next()) {
					String tableName = resultSet.getString("TABLE_NAME");

					if (_isControlTable(dbInspector, tableName)) {
						statement.executeUpdate(
							StringBundler.concat(
								"create view ", _getSchemaName(companyId),
								StringPool.PERIOD, tableName, " as select * ",
								"from ", _defaultSchemaName, StringPool.PERIOD,
								tableName));
					}
					else {
						statement.executeUpdate(
							StringBundler.concat(
								"create table ", _getSchemaName(companyId),
								StringPool.PERIOD, tableName, " like ",
								_defaultSchemaName, StringPool.PERIOD,
								tableName));
					}
				}
			}
		}
		catch (Exception exception) {
			throw new ORMException(exception);
		}

		return true;
	}

	public static boolean removePartition(long companyId) {
		return _DATABASE_PARTITION_ENABLED;
	}

	public static void setDefaultCompanyId(long companyId) {
		if (_DATABASE_PARTITION_ENABLED) {
			_defaultCompanyId = companyId;
		}
	}

	public static void usePartition(Connection connection) {
		if (_DATABASE_PARTITION_ENABLED) {
			_usePartition(connection, CompanyThreadLocal.getCompanyId());
		}
	}

	public static DataSource wrapDataSource(DataSource dataSource)
		throws SQLException {

		if (_DATABASE_PARTITION_ENABLED) {
			DB db = DBManagerUtil.getDB(
				DBManagerUtil.getDBType(DialectDetector.getDialect(dataSource)),
				dataSource);

			if (db.getDBType() != DBType.MYSQL) {
				throw new Error("Database Partition requires MySQL");
			}

			try (Connection connection = dataSource.getConnection()) {
				_defaultSchemaName = connection.getCatalog();
			}

			dataSource = new DBPartitionDataSource(dataSource);
		}

		return dataSource;
	}

	private static String _getSchemaName(long companyId) {
		return _DATABASE_PARTITION_INSTANCE_ID + CharPool.UNDERLINE + companyId;
	}

	private static boolean _isControlTable(
			DBInspector dbInspector, String tableName)
		throws Exception {

		if (!dbInspector.hasColumn(tableName, "companyId") ||
			tableName.equals("Portlet") || tableName.equals("Company") ||
			tableName.equals("VirtualHost")) {

			return true;
		}

		return false;
	}

	private static void _usePartition(Connection connection, long companyId) {
		try {
			if (connection.isReadOnly()) {
				return;
			}

			try (Statement statement = connection.createStatement()) {
				if ((companyId == CompanyConstants.SYSTEM) ||
					(companyId == _defaultCompanyId)) {

					statement.execute("USE " + _defaultSchemaName);
				}
				else {
					statement.execute("USE " + _getSchemaName(companyId));
				}
			}
		}
		catch (SQLException sqlException) {
			throw new ORMException(sqlException);
		}
	}

	private static final boolean _DATABASE_PARTITION_ENABLED =
		GetterUtil.getBoolean(PropsUtil.get("database.partition.enabled"));

	private static final String _DATABASE_PARTITION_INSTANCE_ID = PropsUtil.get(
		"database.partition.instance.id");

	private static volatile long _defaultCompanyId;
	private static String _defaultSchemaName;

}