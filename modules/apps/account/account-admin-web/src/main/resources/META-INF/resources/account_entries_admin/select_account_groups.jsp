<%@ page import="com.liferay.account.admin.web.internal.dao.search.AccountEntryAccountGroupSearchContainerFactory" %>

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
SearchContainer<AccountGroupDisplay> accountGroupDisplaySearchContainer = AccountGroupDisplaySearchContainerFactory.create(liferayPortletRequest, liferayPortletResponse);
%>

<clay:container-fluid>
	<liferay-ui:search-container
		headerNames="name,description"
		id="accountEntryAccountGroups"
		searchContainer="<%= accountGroupDisplaySearchContainer %>"
	>
		<liferay-ui:search-container-row
			className="com.liferay.account.admin.web.internal.display.AccountGroupDisplay"
			keyProperty="accountGroupId"
			modelVar="accountGroupDisplay"
		>
			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand table-title"
				name="name"
				value="<%= HtmlUtil.escape(accountGroupDisplay.getName()) %>"
			/>

			<liferay-ui:search-container-column-text
				cssClass="table-cell-expand"
				name="description"
				value="<%= HtmlUtil.escape(accountGroupDisplay.getDescription()) %>"
			/>

			<liferay-ui:search-container-column-jsp
				path="/account_entries_admin/account_entry_account_groups_action.jsp"
			/>
		</liferay-ui:search-container-row>

		<liferay-ui:search-iterator
			markupView="lexicon"
		/>
	</liferay-ui:search-container>
</clay:container-fluid>