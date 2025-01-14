<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String tabs1 = ParamUtil.getString(request, "tabs1", "customized");

PortletURL portletURL = PortletURLBuilder.createRenderURL(
	renderResponse
).setTabs1(
	tabs1
).buildPortletURL();
%>

<clay:container-fluid>
	<liferay-ui:tabs
		names="customized,minimum"
		url="<%= portletURL.toString() %>"
	>
		<liferay-ui:section>
			<c:if test='<%= tabs1.equals("customized") %>'>
				<liferay-util:include page="/partials/customized.jsp" servletContext="<%= pageContext.getServletContext() %>" />
			</c:if>
		</liferay-ui:section>

		<liferay-ui:section>
			<c:if test='<%= tabs1.equals("minimum") %>'>
				<liferay-util:include page="/partials/minimum.jsp" servletContext="<%= pageContext.getServletContext() %>" />
			</c:if>
		</liferay-ui:section>
	</liferay-ui:tabs>
</clay:container-fluid>