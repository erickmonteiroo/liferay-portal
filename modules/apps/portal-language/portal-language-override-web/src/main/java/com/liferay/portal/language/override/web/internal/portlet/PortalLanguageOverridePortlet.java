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

package com.liferay.portal.language.override.web.internal.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.language.override.web.internal.constants.PortalLanguageOverridePortletKeys;
import com.liferay.portal.language.override.web.internal.display.ViewDisplayContextFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

/**
 * @author Drew Brokke
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.hidden",
		"com.liferay.portlet.preferences-owned-by-group=true",
		"com.liferay.portlet.private-request-attributes=false",
		"com.liferay.portlet.private-session-attributes=false",
		"com.liferay.portlet.single-page-application=false",
		"com.liferay.portlet.use-default-template=true",
		"javax.portlet.display-name=Portal Language Override",
		"javax.portlet.expiration-cache=0",
		"javax.portlet.init-param.template-path=/META-INF/resources/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + PortalLanguageOverridePortletKeys.PORTAL_LANGUAGE_OVERRIDE,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=administrator"
	},
	service = Portlet.class
)
public class PortalLanguageOverridePortlet extends MVCPortlet {

	@Override
	protected void doDispatch(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {

		_setAttributes(renderRequest, renderResponse);

		super.doDispatch(renderRequest, renderResponse);
	}

	private void _setAttributes(RenderRequest renderRequest, RenderResponse renderResponse) {
		Object portletDisplayContext = _getPortletDisplayContext(renderRequest, renderResponse);

		if (portletDisplayContext != null) {
			renderRequest.setAttribute(WebKeys.PORTLET_DISPLAY_CONTEXT, portletDisplayContext);
		}
	}

	private Object _getPortletDisplayContext(RenderRequest renderRequest, RenderResponse renderResponse) {
		String path = getPath(renderRequest, renderResponse);

		if (path == null || path.equals("/view.jsp")) {
			return _viewDisplayContextFactory.create(renderRequest, renderResponse);
		}

		return null;
	}

	@Reference
	private ViewDisplayContextFactory _viewDisplayContextFactory;

}