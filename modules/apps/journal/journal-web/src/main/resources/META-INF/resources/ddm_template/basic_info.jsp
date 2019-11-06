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
JournalEditDDMTemplateDisplayContext journalEditDDMTemplateDisplayContext = new JournalEditDDMTemplateDisplayContext(request);

DDMTemplate ddmTemplate = journalEditDDMTemplateDisplayContext.getDDMTemplate();

DDMStructure ddmStructure = journalEditDDMTemplateDisplayContext.getDDMStructure();
%>

<aui:model-context bean="<%= ddmTemplate %>" model="<%= DDMTemplate.class %>" />

<%
StringBundler sb = new StringBundler(6);

sb.append(LanguageUtil.get(request, journalEditDDMTemplateDisplayContext.getLanguage() + "[stands-for]"));
sb.append(StringPool.SPACE);
sb.append(StringPool.OPEN_PARENTHESIS);
sb.append(StringPool.PERIOD);
sb.append(journalEditDDMTemplateDisplayContext.getLanguage());
sb.append(StringPool.CLOSE_PARENTHESIS);
%>

<p class="article-structure">
	<b><liferay-ui:message key="language" /></b>: <%= sb.toString() %>
</p>

<aui:input helpMessage="structure-help" name="structure" type="resource" value="<%= (ddmStructure != null) ? ddmStructure.getName(locale) : StringPool.BLANK %>" wrapperCssClass='<%= ((ddmTemplate == null) || (ddmTemplate.getClassPK() == 0)) ? "mb-2" : StringPool.BLANK %>' />

<c:if test="<%= (ddmTemplate == null) || (ddmTemplate.getClassPK() == 0) %>">
	<div class="form-group">
		<aui:button id="selectDDMStructure" value="select" />
	</div>
</c:if>

<c:if test="<%= !journalEditDDMTemplateDisplayContext.autogenerateDDMTemplateKey() && (ddmTemplate == null) %>">
	<aui:input helpMessage="template-key-help" name="ddmTemplateKey" type="text" />
</c:if>

<aui:input defaultLanguageId="<%= (ddmTemplate == null) ? LocaleUtil.toLanguageId(LocaleUtil.getSiteDefault()): ddmTemplate.getDefaultLanguageId() %>" name="description" />

<c:if test="<%= ddmTemplate != null %>">
	<aui:input helpMessage="template-key-help" name="ddmTemplateKey" type="resource" value="<%= ddmTemplate.getTemplateKey() %>" />

	<portlet:resourceURL id="/journal/get_ddm_template" var="getDDMTemplateURL">
		<portlet:param name="ddmTemplateId" value="<%= String.valueOf(ddmTemplate.getTemplateId()) %>" />
	</portlet:resourceURL>

	<aui:input name="url" type="resource" value="<%= getDDMTemplateURL %>" />

	<%
	Portlet portlet = PortletLocalServiceUtil.getPortletById(portletDisplay.getId());
	%>

	<aui:input name="webDavURL" type="resource" value="<%= ddmTemplate.getWebDavURL(themeDisplay, WebDAVUtil.getStorageToken(portlet)) %>" />
</c:if>

<aui:input helpMessage="journal-template-cacheable-help" name="cacheable" value="<%= journalEditDDMTemplateDisplayContext.isCacheable() %>" />

<c:if test="<%= (ddmTemplate == null) || (ddmTemplate.getClassPK() == 0) %>">
	<aui:script>
		var selectDDMStructure = document.getElementById(
			'<portlet:namespace />selectDDMStructure'
		);

		if (selectDDMStructure) {
			selectDDMStructure.addEventListener('click', function(event) {
				Liferay.Util.selectEntity(
					{
						dialog: {
							constrain: true,
							modal: true
						},
						eventName: '<portlet:namespace />selectDDMStructure',
						title: '<%= UnicodeLanguageUtil.get(request, "structures") %>',
						uri:
							'<portlet:renderURL windowState="<%= LiferayWindowState.POP_UP.toString() %>"><portlet:param name="mvcPath" value="/select_ddm_structure.jsp" /></portlet:renderURL>'
					},
					function(event) {
						if (
							document.<portlet:namespace />fm
								.<portlet:namespace />classPK.value !=
							event.ddmstructureid
						) {
							document.<portlet:namespace />fm.<portlet:namespace />classPK.value =
								event.ddmstructureid;

							Liferay.fire('<portlet:namespace />refreshEditor');
						}
					}
				);
			});
		}
	</aui:script>
</c:if>