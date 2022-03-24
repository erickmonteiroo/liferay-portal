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

package com.liferay.mail.internal.configuration;

import com.liferay.mail.kernel.model.Account;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mail.MailSettings;
import com.liferay.portal.kernel.module.configuration.ConfigurationException;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.util.HashMapDictionaryBuilder;

import java.util.Collections;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Erick Monteiro
 */
@Component(service = MailSettings.class)
public class MailSettingsImpl implements MailSettings {

	@Override
	public String getMailAdvancedProperties(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.advancedProperties();
	}

	@Override
	public String getMailPOP3Host(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.pop3Host();
	}

	@Override
	public String getMailPOP3Password(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.pop3Password();
	}

	@Override
	public int getMailPOP3Port(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.pop3Port();
	}

	@Override
	public boolean getMailPOP3Secure(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.pop3Secure();
	}

	@Override
	public String getMailPOP3User(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.pop3User();
	}

	@Override
	public boolean getMailSessionMail(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.sessionMail();
	}

	@Override
	public String getMailSMTPHost(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.smtpHost();
	}

	@Override
	public String getMailSMTPPassword(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.smtpPassword();
	}

	@Override
	public int getMailSMTPPort(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.smtpPort();
	}

	@Override
	public boolean getMailSMTPSecure(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.smtpSecure();
	}

	@Override
	public boolean getMailSMTPStartTLSEnable(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.smtpStartTLSEnable();
	}

	@Override
	public String getMailSMTPUser(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		return mailConfiguration.smtpUser();
	}

	@Override
	public String getMailStoreProtocol(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		try {
			if (getMailPOP3Secure(companyId)) {
				_configurationProvider.saveCompanyConfiguration(
					MailConfiguration.class, companyId,
					HashMapDictionaryBuilder.<String, Object>put(
						"storeProtocol", Account.PROTOCOL_POP
					).build());
			}
		}
		catch (ConfigurationException configurationException) {
			if (_log.isWarnEnabled()) {
				_log.warn(configurationException);
			}
		}

		return mailConfiguration.storeProtocol();
	}

	@Override
	public String getMailTransportProtocol(long companyId) {
		MailConfiguration mailConfiguration = _getAuthLoginConfiguration(
			companyId);

		try {
			if (getMailSMTPSecure(companyId)) {
				_configurationProvider.saveCompanyConfiguration(
					MailConfiguration.class, companyId,
					HashMapDictionaryBuilder.<String, Object>put(
						"transportProtocol", Account.PROTOCOL_SMTPS
					).build());
			}
		}
		catch (ConfigurationException configurationException) {
			if (_log.isWarnEnabled()) {
				_log.warn(configurationException);
			}
		}

		return mailConfiguration.transportProtocol();
	}

	private MailConfiguration _getAuthLoginConfiguration(long companyId) {
		try {
			return _configurationProvider.getCompanyConfiguration(
				MailConfiguration.class, companyId);
		}
		catch (ConfigurationException configurationException) {
			if (_log.isWarnEnabled()) {
				_log.warn(configurationException);
			}

			return ConfigurableUtil.createConfigurable(
				MailConfiguration.class, Collections.emptyMap());
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MailSettingsImpl.class);

	@Reference
	private ConfigurationProvider _configurationProvider;

}