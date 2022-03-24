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

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Erick Monteiro
 */
@ExtendedObjectClassDefinition(
	category = "email", scope = ExtendedObjectClassDefinition.Scope.COMPANY
)
@Meta.OCD(
	id = "com.liferay.mail.internal.configuration.MailConfiguration",
	localization = "content/Language", name = "mail-configuration-name"
)
public interface MailConfiguration {

	@Meta.AD(deflt = "localhost", name = "pop3-host-name", required = false)
	public String pop3Host();

	@Meta.AD(deflt = "110", name = "pop3-port-name", required = false)
	public int pop3Port();

	@Meta.AD(deflt = "false", name = "pop3-secure-name", required = false)
	public boolean pop3Secure();

	@Meta.AD(deflt = "", name = "pop3-user-name", required = false)
	public String pop3User();

	@Meta.AD(deflt = "", name = "pop3-password-name", required = false)
	public String pop3Password();

	@Meta.AD(deflt = "localhost", name = "smtp-host-name", required = false)
	public String smtpHost();

	@Meta.AD(deflt = "25", name = "smtp-port-name", required = false)
	public int smtpPort();

	@Meta.AD(deflt = "false", name = "smtp-secure-name", required = false)
	public boolean smtpSecure();

	@Meta.AD(deflt = "true", name = "start-tls-name", required = false)
	public boolean smtpStartTLSEnable();

	@Meta.AD(deflt = "", name = "smtp-user-name", required = false)
	public String smtpUser();

	@Meta.AD(deflt = "", name = "smtp-password-name", required = false)
	public String smtpPassword();

	@Meta.AD(
		deflt = "", description = "mail-advanced-properties-description",
		name = "mail-advanced-properties-name", required = false
	)
	public String advancedProperties();

	@Meta.AD(deflt = "false", name = "session-mail-name", required = false)
	public boolean sessionMail();

	@Meta.AD(deflt = "pop3", name = "store-protocol-name", required = false)
	public String storeProtocol();

	@Meta.AD(deflt = "smtp", name = "transport-protocol-name", required = false)
	public String transportProtocol();

}