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

package com.liferay.mail.internal.configuration.admin.definition;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormRule;

/**
 * @author Erick Monteiro
 */
@DDMForm(
	rules = {
		@DDMFormRule(
			actions = {
				"setVisible('sessionMail', false)",
				"setVisible('storeProtocol', false)",
				"setVisible('transportProtocol', false)"
			},
			condition = "TRUE"
		)
	}
)
public interface MailConfigurationForm {
}