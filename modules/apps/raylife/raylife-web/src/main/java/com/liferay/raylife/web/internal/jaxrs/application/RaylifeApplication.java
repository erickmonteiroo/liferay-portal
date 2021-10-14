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

package com.liferay.raylife.web.internal.jaxrs.application;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;

import java.util.Collections;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.jaxrs.whiteboard.JaxrsWhiteboardConstants;

/**
 * @author Erick Monteiro
 */
@Component(
	property = {
		JaxrsWhiteboardConstants.JAX_RS_APPLICATION_BASE + "=/raylife",
		JaxrsWhiteboardConstants.JAX_RS_NAME + "=Raylife.Application",
		"auth.verifier.guest.allowed=true",
		"liferay.access.control.disable=true", "liferay.oauth2=false"
	},
	service = Application.class
)
public class RaylifeApplication extends Application {

	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/post-user")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response confirm(
			@Context HttpServletRequest httpServletRequest,
			@Context HttpServletResponse httpServletResponse, String userJSON)
		throws PortalException {

		long companyId = _portal.getCompanyId(httpServletRequest);
		String languageId = ParamUtil.getString(
			httpServletRequest, "languageId");
		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			User.class.getName(), httpServletRequest);
		JSONObject userJSONObject = JSONFactoryUtil.createJSONObject(userJSON);

		User user = null;
		try {
			user = _userLocalService.addUser(
				_userLocalService.getDefaultUserId(companyId), companyId,
				userJSONObject.getBoolean("autoPassword"),
				userJSONObject.getString("password1"),
				userJSONObject.getString("password2"), false,
				userJSONObject.getString("screenName"),
				userJSONObject.getString("emailAddress"),
				LocaleUtil.fromLanguageId(languageId),
				userJSONObject.getString("firstName"),
				userJSONObject.getString("middleName"),
				userJSONObject.getString("lastName"),
				userJSONObject.getLong("prefixId"),
				userJSONObject.getLong("suffixId"),
				userJSONObject.getBoolean("male", true),
				userJSONObject.getInt("birthdayMonth"),
				userJSONObject.getInt("birthdayDay"),
				userJSONObject.getInt("birthdayYear"),
				userJSONObject.getString("jobTitle"), null, null, null, null,
				userJSONObject.getBoolean("sendEmail", false), serviceContext);

			return Response.ok(
				JSONFactoryUtil.serialize(user), MediaType.APPLICATION_JSON
			).build();
		}
		catch (Exception exception) {
			Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;

			return Response.ok(
				JSONFactoryUtil.serialize(exception.getMessage()), MediaType.APPLICATION_JSON
			).status(
				status.getStatusCode()
			).build();
		}
	}

	public Set<Object> getSingletons() {
		return Collections.singleton(this);
	}

	@Reference
	private Portal _portal;

	@Reference
	private UserLocalService _userLocalService;

}