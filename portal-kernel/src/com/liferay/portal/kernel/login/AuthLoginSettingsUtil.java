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

package com.liferay.portal.kernel.login;

import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ServiceProxyFactory;

/**
 * @author Erick Monteiro
 */
public class AuthLoginSettingsUtil {

	public static boolean isPromptEnabled(long groupId) {
		return GetterUtil.getBoolean(
			_authLoginSettings.isPromptEnabled(groupId));
	}

	private static volatile AuthLoginSettings _authLoginSettings =
		ServiceProxyFactory.newServiceTrackedInstance(
			AuthLoginSettings.class, AuthLoginSettingsUtil.class,
			"_authLoginSettings", false);

}