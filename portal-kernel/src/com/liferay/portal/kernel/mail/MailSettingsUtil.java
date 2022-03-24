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

package com.liferay.portal.kernel.mail;

import com.liferay.portal.kernel.util.ServiceProxyFactory;

/**
 * @author Erick Monteiro
 */
public class MailSettingsUtil {

	public static String getMailAdvancedProperties(long companyId) {
		return _mailSettings.getMailAdvancedProperties(companyId);
	}

	public static String getMailPOP3Host(long companyId) {
		return _mailSettings.getMailPOP3Host(companyId);
	}

	public static String getMailPOP3Password(long companyId) {
		return _mailSettings.getMailPOP3Password(companyId);
	}

	public static int getMailPOP3Port(long companyId) {
		return _mailSettings.getMailPOP3Port(companyId);
	}

	public static boolean getMailPOP3Secure(long companyId) {
		return _mailSettings.getMailPOP3Secure(companyId);
	}

	public static String getMailPOP3User(long companyId) {
		return _mailSettings.getMailPOP3User(companyId);
	}

	public static boolean getMailSessionMail(long companyId) {
		return _mailSettings.getMailSessionMail(companyId);
	}

	public static String getMailSMTPHost(long companyId) {
		return _mailSettings.getMailSMTPHost(companyId);
	}

	public static String getMailSMTPPassword(long companyId) {
		return _mailSettings.getMailSMTPPassword(companyId);
	}

	public static int getMailSMTPPort(long companyId) {
		return _mailSettings.getMailSMTPPort(companyId);
	}

	public static boolean getMailSMTPSecure(long companyId) {
		return _mailSettings.getMailSMTPSecure(companyId);
	}

	public static boolean getMailSMTPStartTLSEnable(long companyId) {
		return _mailSettings.getMailSMTPStartTLSEnable(companyId);
	}

	public static String getMailSMTPUser(long companyId) {
		return _mailSettings.getMailSMTPUser(companyId);
	}

	public static String getMailStoreProtocol(long companyId) {
		return _mailSettings.getMailStoreProtocol(companyId);
	}

	public static String getMailTransportProtocol(long companyId) {
		return _mailSettings.getMailTransportProtocol(companyId);
	}

	private static volatile MailSettings _mailSettings =
		ServiceProxyFactory.newServiceTrackedInstance(
			MailSettings.class, MailSettingsUtil.class, "_mailSettings", false);

}