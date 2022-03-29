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

package com.liferay.mail.service.impl;

import com.liferay.mail.kernel.model.Account;
import com.liferay.mail.kernel.model.Filter;
import com.liferay.mail.kernel.model.MailMessage;
import com.liferay.mail.kernel.service.MailService;
import com.liferay.mail.kernel.util.Hook;
import com.liferay.portal.kernel.cluster.Clusterable;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.mail.MailSettingsUtil;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.module.framework.service.IdentifiableOSGiService;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.util.InfrastructureUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;
import com.liferay.portal.kernel.util.PropertiesUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 * @author Brian Wing Shun Chan
 */
public class MailServiceImpl implements IdentifiableOSGiService, MailService {

	@Override
	public void addForward(
		long companyId, long userId, List<Filter> filters,
		List<String> emailAddresses, boolean leaveCopy) {

		if (_log.isDebugEnabled()) {
			_log.debug("addForward");
		}

		MethodHandler methodHandler = new MethodHandler(
			_addForwardMethodKey, companyId, userId, filters, emailAddresses,
			leaveCopy);

		MessageBusUtil.sendMessage(DestinationNames.MAIL, methodHandler);
	}

	@Override
	public void addUser(
		long companyId, long userId, String password, String firstName,
		String middleName, String lastName, String emailAddress) {

		if (_log.isDebugEnabled()) {
			_log.debug("addUser");
		}

		MethodHandler methodHandler = new MethodHandler(
			_addUserMethodKey, companyId, userId, password, firstName,
			middleName, lastName, emailAddress);

		MessageBusUtil.sendMessage(DestinationNames.MAIL, methodHandler);
	}

	@Override
	public void addVacationMessage(
		long companyId, long userId, String emailAddress,
		String vacationMessage) {

		if (_log.isDebugEnabled()) {
			_log.debug("addVacationMessage");
		}

		MethodHandler methodHandler = new MethodHandler(
			_addVacationMessageMethodKey, companyId, userId, emailAddress,
			vacationMessage);

		MessageBusUtil.sendMessage(DestinationNames.MAIL, methodHandler);
	}

	@Clusterable
	@Override
	public void clearSession() {
		_session = null;
	}

	@Override
	public void deleteEmailAddress(long companyId, long userId) {
		if (_log.isDebugEnabled()) {
			_log.debug("deleteEmailAddress");
		}

		MethodHandler methodHandler = new MethodHandler(
			_deleteEmailAddressMethodKey, companyId, userId);

		MessageBusUtil.sendMessage(DestinationNames.MAIL, methodHandler);
	}

	@Override
	public void deleteUser(long companyId, long userId) {
		if (_log.isDebugEnabled()) {
			_log.debug("deleteUser");
		}

		MethodHandler methodHandler = new MethodHandler(
			_deleteUserMethodKey, companyId, userId);

		MessageBusUtil.sendMessage(DestinationNames.MAIL, methodHandler);
	}

	@Override
	public String getOSGiServiceIdentifier() {
		return MailService.class.getName();
	}

	@Override
	public Session getSession() {
		if (_session != null) {
			return _session;
		}

		Session session = InfrastructureUtil.getMailSession();

		long companyId = CompanyThreadLocal.getCompanyId();

		if (!MailSettingsUtil.getMailSessionMail(companyId)) {
			_session = session;

			return _session;
		}

		String advancedPropertiesString =
			MailSettingsUtil.getMailAdvancedProperties(companyId);
		String pop3Host = MailSettingsUtil.getMailPOP3Host(companyId);
		String pop3Password = MailSettingsUtil.getMailPOP3Password(companyId);
		int pop3Port = MailSettingsUtil.getMailPOP3Port(companyId);
		String pop3User = MailSettingsUtil.getMailPOP3User(companyId);
		String smtpHost = MailSettingsUtil.getMailSMTPHost(companyId);
		String smtpPassword = MailSettingsUtil.getMailSMTPPassword(companyId);
		int smtpPort = MailSettingsUtil.getMailSMTPPort(companyId);
		boolean smtpStartTLSEnable = MailSettingsUtil.getMailSMTPStartTLSEnable(
			companyId);
		String smtpUser = MailSettingsUtil.getMailSMTPUser(companyId);
		String storeProtocol = MailSettingsUtil.getMailStoreProtocol(companyId);
		String transportProtocol = MailSettingsUtil.getMailTransportProtocol(
			companyId);

		Properties properties = session.getProperties();

		// Incoming

		if (!storeProtocol.equals(Account.PROTOCOL_POPS)) {
			storeProtocol = Account.PROTOCOL_POP;
		}

		properties.setProperty("mail.store.protocol", storeProtocol);

		String storePrefix = "mail." + storeProtocol + ".";

		properties.setProperty(storePrefix + "host", pop3Host);
		properties.setProperty(storePrefix + "password", pop3Password);
		properties.setProperty(storePrefix + "port", String.valueOf(pop3Port));
		properties.setProperty(storePrefix + "user", pop3User);

		// Outgoing

		if (!transportProtocol.equals(Account.PROTOCOL_SMTPS)) {
			transportProtocol = Account.PROTOCOL_SMTP;
		}

		properties.setProperty("mail.transport.protocol", transportProtocol);

		String transportPrefix = "mail." + transportProtocol + ".";

		boolean smtpAuth = false;

		if (Validator.isNotNull(smtpPassword) ||
			Validator.isNotNull(smtpUser)) {

			smtpAuth = true;
		}

		properties.setProperty(
			transportPrefix + "auth", String.valueOf(smtpAuth));
		properties.setProperty(transportPrefix + "host", smtpHost);
		properties.setProperty(transportPrefix + "password", smtpPassword);
		properties.setProperty(
			transportPrefix + "port", String.valueOf(smtpPort));
		properties.setProperty(
			transportPrefix + "starttls.enable",
			String.valueOf(smtpStartTLSEnable));
		properties.setProperty(transportPrefix + "user", smtpUser);

		// Advanced

		try {
			if (Validator.isNotNull(advancedPropertiesString)) {
				Properties advancedProperties = PropertiesUtil.load(
					advancedPropertiesString);

				for (Map.Entry<Object, Object> entry :
						advancedProperties.entrySet()) {

					String key = (String)entry.getKey();
					String value = (String)entry.getValue();

					properties.setProperty(key, value);
				}
			}
		}
		catch (IOException ioException) {
			if (_log.isWarnEnabled()) {
				_log.warn(ioException);
			}
		}

		if (smtpAuth) {
			_session = Session.getInstance(
				properties,
				new Authenticator() {

					protected PasswordAuthentication
						getPasswordAuthentication() {

						return new PasswordAuthentication(
							smtpUser, smtpPassword);
					}

				});
		}
		else {
			_session = Session.getInstance(properties);
		}

		return _session;
	}

	@Override
	public void sendEmail(MailMessage mailMessage) {
		if (_log.isDebugEnabled()) {
			_log.debug("sendEmail");
		}

		MessageBusUtil.sendMessage(DestinationNames.MAIL, mailMessage);
	}

	@Override
	public void updateBlocked(
		long companyId, long userId, List<String> blocked) {

		if (_log.isDebugEnabled()) {
			_log.debug("updateBlocked");
		}

		MethodHandler methodHandler = new MethodHandler(
			_updateBlockedMethodKey, companyId, userId, blocked);

		MessageBusUtil.sendMessage(DestinationNames.MAIL, methodHandler);
	}

	@Override
	public void updateEmailAddress(
		long companyId, long userId, String emailAddress) {

		if (_log.isDebugEnabled()) {
			_log.debug("updateEmailAddress");
		}

		MethodHandler methodHandler = new MethodHandler(
			_updateEmailAddressMethodKey, companyId, userId, emailAddress);

		MessageBusUtil.sendMessage(DestinationNames.MAIL, methodHandler);
	}

	@Override
	public void updatePassword(long companyId, long userId, String password) {
		if (_log.isDebugEnabled()) {
			_log.debug("updatePassword");
		}

		MethodHandler methodHandler = new MethodHandler(
			_updatePasswordMethodKey, companyId, userId, password);

		MessageBusUtil.sendMessage(DestinationNames.MAIL, methodHandler);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		MailServiceImpl.class);

	private static final MethodKey _addForwardMethodKey = new MethodKey(
		Hook.class, "addForward", long.class, long.class, List.class,
		List.class, boolean.class);
	private static final MethodKey _addUserMethodKey = new MethodKey(
		Hook.class, "addUser", long.class, long.class, String.class,
		String.class, String.class, String.class, String.class);
	private static final MethodKey _addVacationMessageMethodKey = new MethodKey(
		Hook.class, "addVacationMessage", long.class, long.class, String.class,
		String.class);
	private static final MethodKey _deleteEmailAddressMethodKey = new MethodKey(
		Hook.class, "deleteEmailAddress", long.class, long.class);
	private static final MethodKey _deleteUserMethodKey = new MethodKey(
		Hook.class, "deleteUser", long.class, long.class);
	private static final MethodKey _updateBlockedMethodKey = new MethodKey(
		Hook.class, "updateBlocked", long.class, long.class, List.class);
	private static final MethodKey _updateEmailAddressMethodKey = new MethodKey(
		Hook.class, "updateEmailAddress", long.class, long.class, String.class);
	private static final MethodKey _updatePasswordMethodKey = new MethodKey(
		Hook.class, "updatePassword", long.class, long.class, String.class);

	private Session _session;

}