package com.liferay.account.admin.web.internal.display.context;

import com.liferay.account.admin.web.internal.display.AccountGroupDisplay;
import com.liferay.frontend.taglib.clay.servlet.taglib.display.context.SearchContainerManagementToolbarDisplayContext;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenuBuilder;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItem;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.petra.portlet.url.builder.PortletURLBuilder;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.LiferayPortletRequest;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.PortletURLUtil;

import java.util.List;

import javax.portlet.PortletURL;

import javax.servlet.http.HttpServletRequest;

public class ViewAccountEntryAccountGroupsManagementToolbarDisplayContext
	extends SearchContainerManagementToolbarDisplayContext {

	public ViewAccountEntryAccountGroupsManagementToolbarDisplayContext(
		HttpServletRequest httpServletRequest,
		LiferayPortletRequest liferayPortletRequest,
		LiferayPortletResponse liferayPortletResponse,
		SearchContainer<AccountGroupDisplay> searchContainer) {

		super(
			httpServletRequest, liferayPortletRequest, liferayPortletResponse,
			searchContainer);
	}

	@Override
	public List<DropdownItem> getActionDropdownItems() {
		return DropdownItemList.of(
			() -> {
				DropdownItem dropdownItem = new DropdownItem();

				dropdownItem.putData("action", "removeAccountGroup");

				dropdownItem.putData(
					"removeAccountGroupURL",
					PortletURLBuilder.createActionURL(
						liferayPortletResponse
					).setActionName(
						"/account_admin/remove_account_entry_account_groups"
					).setRedirect(
						getPortletURL()
					).buildString());

				dropdownItem.setIcon("times-circle");
				dropdownItem.setLabel(
					LanguageUtil.get(httpServletRequest, "remove"));
				dropdownItem.setQuickAction(true);

				return dropdownItem;
			});
	}

	@Override
	public CreationMenu getCreationMenu() {
		return CreationMenuBuilder.addPrimaryDropdownItem(
			dropdownItem -> {
				dropdownItem.putData("action", "addAccountEntryAccountGroup");
				dropdownItem.setLabel(
					LanguageUtil.get(
						httpServletRequest, "assign-account-group"));
			}
		).build();
	}

	@Override
	public PortletURL getPortletURL() {
		try {
			return PortletURLUtil.clone(currentURLObj, liferayPortletResponse);
		}
		catch (Exception exception) {
			if (_log.isWarnEnabled()) {
				_log.warn(exception, exception);
			}

			return liferayPortletResponse.createRenderURL();
		}
	}

	@Override
	public String getSortingURL() {
		return null;
	}

	@Override
	public Boolean isShowSearch() {
		return false;
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ViewAccountEntryAccountGroupsManagementToolbarDisplayContext.class);

}