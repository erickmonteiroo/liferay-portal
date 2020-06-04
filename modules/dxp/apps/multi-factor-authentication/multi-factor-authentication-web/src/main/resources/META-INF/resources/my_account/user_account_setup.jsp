<%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 *
 *
 *
 */
--%>

<%@ include file="/init.jsp" %>

<%
String label = (String)request.getAttribute(MFAWebKeys.MFA_USER_ACCOUNT_LABEL);
SetupMFAChecker mfaSetupChecker = (SetupMFAChecker)request.getAttribute(SetupMFAChecker.class.getName());
long userId = user.getUserId();
%>

<portlet:actionURL name="/my_account/setup_mfa" var="actionURL">
	<portlet:param name="mvcRenderCommandName" value="/users_admin/edit_user" />
</portlet:actionURL>

<aui:form action="<%= actionURL %>" cssClass="portlet-users-admin-edit-user" data-senna-off="true" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

	<div class="sheet sheet-lg">
		<div class="sheet-header">
			<h1 class="sheet-title"><%= label %></h1>
		</div>

		<liferay-ui:error key="userAccountSetupFailed" message="user-account-setup-failed" />

		<%
		mfaSetupChecker.includeSetup(request, response, userId);
		%>

	</div>
</aui:form>