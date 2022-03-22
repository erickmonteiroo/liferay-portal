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

import org.osgi.annotation.versioning.ProviderType;

/**
 * @author Erick Monteiro
 */
@ProviderType
public interface MailSettings {

	public String getMailAdvancedProperties(long companyId);

	public String getMailPOP3Host(long companyId);

	public String getMailPOP3Password(long companyId);

	public int getMailPOP3Port(long companyId);

	public boolean getMailPOP3Secure(long companyId);

	public String getMailPOP3User(long companyId);

	public boolean getMailSessionMail(long companyId);

	public String getMailSMTPHost(long companyId);

	public String getMailSMTPPassword(long companyId);

	public int getMailSMTPPort(long companyId);

	public boolean getMailSMTPSecure(long companyId);

	public boolean getMailSMTPStartTLSEnable(long companyId);

	public String getMailSMTPUser(long companyId);

	public String getMailStoreProtocol(long companyId);

	public String getMailTransportProtocol(long companyId);

}