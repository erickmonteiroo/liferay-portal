/**
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
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

package com.liferay.portlet.blogs.workflow;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.workflow.BaseWorkflowHandler;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.blogs.asset.BlogsEntryAssetRenderer;
import com.liferay.portlet.blogs.model.BlogsEntry;
import com.liferay.portlet.blogs.service.BlogsEntryLocalServiceUtil;

import java.io.Serializable;

import java.util.Map;

/**
 * <a href="BlogsEntryWorkflowHandler.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Jorge Ferrer
 */
public class BlogsEntryWorkflowHandler extends BaseWorkflowHandler {

	public static final String CLASS_NAME = BlogsEntry.class.getName();

	public String getClassName() {
		return CLASS_NAME;
	}

	public String getType() {
		return TYPE_CONTENT;
	}

	public BlogsEntry updateStatus(
			int status, Map<String, Serializable> workflowContext)
		throws PortalException, SystemException {

		long groupId = (Long)workflowContext.get(
			WorkflowConstants.CONTEXT_GROUP_ID);
		long userId = (Long)workflowContext.get(
			WorkflowConstants.CONTEXT_USER_ID);
		long classPK = (Long)workflowContext.get(
			WorkflowConstants.CONTEXT_ENTRY_CLASS_PK);

		String[] trackbacks = (String[])workflowContext.get("trackbacks");

		Boolean pingOldTrackbacks = (Boolean)workflowContext.get(
			"pingOldTrackbacks");

		if (pingOldTrackbacks == null) {
			pingOldTrackbacks = Boolean.FALSE;
		}

		ServiceContext serviceContext = new ServiceContext();

		serviceContext.setScopeGroupId(groupId);

		return BlogsEntryLocalServiceUtil.updateStatus(
			userId, classPK, trackbacks, pingOldTrackbacks, status,
			serviceContext);
	}

	protected AssetRenderer getAssetRenderer(long classPK)
		throws PortalException, SystemException {

		BlogsEntry entry = BlogsEntryLocalServiceUtil.getEntry(classPK);

		return new BlogsEntryAssetRenderer(entry);
	}

	protected String getIconPath(ThemeDisplay themeDisplay) {
		return themeDisplay.getPathThemeImages() + "/common/page.png";
	}

}