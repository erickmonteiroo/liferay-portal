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

package com.liferay.account.internal.service;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.ProjectionFactoryUtil;
import com.liferay.portal.kernel.dao.orm.Property;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Role;
import com.liferay.portal.kernel.model.role.RoleConstants;
import com.liferay.portal.kernel.service.RoleLocalService;
import com.liferay.portal.kernel.service.RoleLocalServiceWrapper;
import com.liferay.portal.kernel.service.ServiceWrapper;
import com.liferay.portal.kernel.service.UserGroupRoleLocalService;

import java.util.List;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Bruno Queiroz
 * @author Erick Monteiro
 */
@Component(immediate = true, service = ServiceWrapper.class)
public class AccountRoleAssigneesCountWrapper extends RoleLocalServiceWrapper {

	public AccountRoleAssigneesCountWrapper(RoleLocalService roleLocalService) {
		super(roleLocalService);
	}
	public AccountRoleAssigneesCountWrapper() {
		super(null);
	}


	@Override
	public int getAssigneesTotal(long roleId) throws PortalException {
		Role role = getRole(roleId);

		if (role.getType() == RoleConstants.TYPE_ACCOUNT) {
			int assigneesTotal = 0;

			DynamicQuery userGroupRoleDynamicQuery =
				_userGroupRoleLocalService.dynamicQuery();

			Property property = PropertyFactoryUtil.forName("roleId");

			userGroupRoleDynamicQuery.add(property.eq(roleId));

			userGroupRoleDynamicQuery.setProjection(
				ProjectionFactoryUtil.countDistinct("userId"));

			List<?> list = _userGroupRoleLocalService.dynamicQuery(
				userGroupRoleDynamicQuery);

			Long count = (Long)list.get(0);

			assigneesTotal += count.intValue();

			return assigneesTotal;
		}

		return super.getAssigneesTotal(roleId);
	}

	@Reference
	private UserGroupRoleLocalService _userGroupRoleLocalService;

}