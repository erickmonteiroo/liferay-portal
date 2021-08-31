package com.liferay.account.admin.web.internal.dao.search;

import com.liferay.account.model.AccountEntry;
import com.liferay.account.model.AccountGroupRel;
import com.liferay.account.model.AccountGroupRelModel;
import com.liferay.account.service.AccountGroupRelLocalServiceUtil;
import com.liferay.portal.kernel.dao.search.EmptyOnClickRowChecker;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.List;

import javax.portlet.PortletResponse;

public class SelectAccountAccountGroupsRowChecker
	extends EmptyOnClickRowChecker {

	public SelectAccountAccountGroupsRowChecker(
		PortletResponse portletResponse, long accountEntryId) {

		super(portletResponse);

		_accountEntryId = accountEntryId;
	}

	@Override
	public boolean isChecked(Object object) {
		List<Long> accountGroupsIds = _getAccountGroupsIds();

		if (accountGroupsIds.contains(_accountEntryId)) {
			return true;
		}

		return false;
	}

	@Override
	public boolean isDisabled(Object object) {
		return isChecked(object);
	}

	private List<Long> _getAccountGroupsIds() {
		List<AccountGroupRel> accountGroupRels =
			AccountGroupRelLocalServiceUtil.getAccountGroupRels(
				AccountEntry.class.getName(), _accountEntryId);

		return TransformUtil.transform(
			accountGroupRels, AccountGroupRelModel::getAccountGroupId);
	}

	private final long _accountEntryId;

}