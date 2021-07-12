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

package com.liferay.user.associated.data.anonymizer.test;

import com.liferay.arquillian.extension.junit.bridge.junit.Arquillian;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.test.rule.AggregateTestRule;
import com.liferay.portal.kernel.test.util.UserTestUtil;
import com.liferay.portal.test.rule.Inject;
import com.liferay.portal.test.rule.LiferayIntegrationTestRule;
import com.liferay.user.associated.data.anonymizer.UADAnonymousUserProvider;
import com.liferay.user.associated.data.configuration.AnonymousUserConfigurationRetriever;

import java.util.Dictionary;
import java.util.Optional;

import org.junit.Assert;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.osgi.service.cm.Configuration;

/**
 * @author Erick Monteiro
 */
@RunWith(Arquillian.class)
public class UADAnonymousUserProviderTest {

	@ClassRule
	@Rule
	public static final AggregateTestRule aggregateTestRule =
		new LiferayIntegrationTestRule();

	@Test
	public void testGetAnonymousUser() throws Exception {
		User user1 = UserTestUtil.addUser();

		Assert.assertFalse(_uadAnonymousUserProvider.isAnonymousUser(user1));

		Optional<Configuration> configurationOptional =
			_anonymousUserConfigurationRetriever.getOptional(
				user1.getCompanyId());

		Assert.assertTrue(configurationOptional.isPresent());

		Configuration configuration = configurationOptional.get();

		Dictionary<String, Object> properties = configuration.getProperties();

		properties.put("userId", user1.getUserId());

		configuration.update(properties);

		Assert.assertTrue(_uadAnonymousUserProvider.isAnonymousUser(user1));
	}

	@Inject
	private AnonymousUserConfigurationRetriever
		_anonymousUserConfigurationRetriever;

	@Inject
	private UADAnonymousUserProvider _uadAnonymousUserProvider;

}