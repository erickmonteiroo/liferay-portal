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

package com.liferay.depot.test.util;

import com.liferay.depot.model.DepotEntry;
import com.liferay.depot.service.DepotEntryLocalServiceUtil;
import com.liferay.exportimport.kernel.configuration.ExportImportConfigurationParameterMapFactoryUtil;
import com.liferay.exportimport.kernel.lar.PortletDataHandlerKeys;
import com.liferay.exportimport.kernel.service.StagingLocalServiceUtil;
import com.liferay.exportimport.kernel.staging.StagingUtil;
import com.liferay.exportimport.kernel.staging.constants.StagingConstants;
import com.liferay.petra.reflect.ReflectionUtil;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.persistence.GroupUtil;
import com.liferay.portal.kernel.test.util.ServiceContextTestUtil;
import com.liferay.portal.kernel.test.util.TestPropsValues;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.util.PropsValues;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import java.util.Map;

/**
 * @author Alicia Garcia
 */
public class DepotStagingTestUtil {

	public static DepotEntry enableLocalStaging(DepotEntry depotEntry)
		throws Exception {

		Group stagingGroup = enableLocalStaging(depotEntry.getGroup());

		return DepotEntryLocalServiceUtil.fetchGroupDepotEntry(
			stagingGroup.getGroupId());
	}

	public static Group enableLocalStaging(Group group) throws PortalException {
		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext(group.getGroupId());

		_addStagingAttributes(serviceContext);

		serviceContext.setAttribute("staging", Boolean.TRUE);

		StagingLocalServiceUtil.enableLocalStaging(
			TestPropsValues.getUserId(), group, false, false, serviceContext);

		return group.getStagingGroup();
	}

	public static DepotEntry enableRemoteStaging(
			DepotEntry remoteLiveDepotEntry, DepotEntry remoteStagingDepotEntry)
		throws Exception {

		_setPortalProperty(
			"TUNNELING_SERVLET_SHARED_SECRET",
			"F0E1D2C3B4A5968778695A4B3C2D1E0F");

		_setPortalProperty("TUNNELING_SERVLET_SHARED_SECRET_HEX", true);

		ServiceContext serviceContext =
			ServiceContextTestUtil.getServiceContext();

		serviceContext.setAddGroupPermissions(true);
		serviceContext.setAddGuestPermissions(true);
		serviceContext.setScopeGroupId(remoteStagingDepotEntry.getGroupId());

		_addStagingAttribute(
			serviceContext,
			StagingUtil.getStagedPortletId(_DEPOT_ADMIN_PORTLET_ID), true);
		_addStagingAttribute(
			serviceContext, PortletDataHandlerKeys.PORTLET_DATA_ALL, false);
		_addStagingAttribute(
			serviceContext, PortletDataHandlerKeys.PORTLET_SETUP_ALL, false);

		UserTestUtil.setUser(TestPropsValues.getUser());

		StagingLocalServiceUtil.enableRemoteStaging(
			TestPropsValues.getUserId(), remoteStagingDepotEntry.getGroup(),
			false, false, "localhost", PortalUtil.getPortalServerPort(false),
			PortalUtil.getPathContext(), false,
			remoteLiveDepotEntry.getGroupId(), serviceContext);

		GroupUtil.clearCache();

		return DepotEntryLocalServiceUtil.fetchGroupDepotEntry(
			remoteLiveDepotEntry.getGroupId());
	}

	public static void publishLayouts(Group stagingGroup, Group liveGroup)
		throws PortalException {

		Map<String, String[]> parameters =
			ExportImportConfigurationParameterMapFactoryUtil.
				buildFullPublishParameterMap();

		StagingUtil.publishLayouts(
			TestPropsValues.getUserId(), stagingGroup.getGroupId(),
			liveGroup.getGroupId(), false, parameters);
	}

	private static void _addStagingAttribute(
		ServiceContext serviceContext, String key, Object value) {

		serviceContext.setAttribute(
			StagingConstants.STAGED_PREFIX + key + StringPool.DOUBLE_DASH,
			String.valueOf(value));
	}

	private static void _addStagingAttributes(ServiceContext serviceContext) {
		_addStagingAttribute(
			serviceContext, PortletDataHandlerKeys.DATA_STRATEGY_MIRROR, true);
		_addStagingAttribute(
			serviceContext, PortletDataHandlerKeys.PORTLET_CONFIGURATION_ALL,
			true);
		_addStagingAttribute(
			serviceContext, PortletDataHandlerKeys.PORTLET_DATA_ALL, true);
		_addStagingAttribute(
			serviceContext, PortletDataHandlerKeys.PORTLET_SETUP_ALL, true);
	}

	private static void _setPortalProperty(String propertyName, Object value)
		throws Exception {

		Field field = ReflectionUtil.getDeclaredField(
			PropsValues.class, propertyName);

		field.setAccessible(true);

		Field modifiersField = Field.class.getDeclaredField("modifiers");

		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

		field.set(null, value);
	}

	private static final String _DEPOT_ADMIN_PORTLET_ID =
		"com_liferay_depot_web_portlet_DepotAdminPortlet";

}