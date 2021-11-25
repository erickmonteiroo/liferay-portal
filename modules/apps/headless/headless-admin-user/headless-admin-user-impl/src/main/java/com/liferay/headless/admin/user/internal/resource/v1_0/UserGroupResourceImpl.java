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

package com.liferay.headless.admin.user.internal.resource.v1_0;

import com.liferay.headless.admin.user.dto.v1_0.UserGroup;
import com.liferay.headless.admin.user.internal.dto.v1_0.converter.UserGroupResourceDTOConverter;
import com.liferay.headless.admin.user.resource.v1_0.UserGroupResource;
import com.liferay.portal.kernel.service.UserGroupService;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/liferay/rest/v1_0/user-group.properties",
	scope = ServiceScope.PROTOTYPE, service = UserGroupResource.class
)
public class UserGroupResourceImpl extends BaseUserGroupResourceImpl {

	@Override
	public UserGroup postUserGroup(UserGroup userGroup) throws Exception {
		return _toUserGroup(
			_userGroupService.addUserGroup(
				userGroup.getName(), userGroup.getDescription(), null));
	}

	private UserGroup _toUserGroup(
			com.liferay.portal.kernel.model.UserGroup userGroup)
		throws Exception {

		return _userGroupResourceDTOConverter.toDTO(userGroup);
	}

	@Reference
	private UserGroupResourceDTOConverter _userGroupResourceDTOConverter;

	@Reference
	private UserGroupService _userGroupService;

}