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

package com.liferay.user.associated.data.configuration;

import java.io.IOException;

import java.util.Optional;

import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.cm.Configuration;

/**
 * Provides a way to retrieve an Anonymous User Configuration
 * @author Erick Monteiro
 */
public interface AnonymousUserConfigurationRetriever {

	/**
	 * Get a Configuration Optional for the given company
	 * @param  companyId the company ID
	 * @return the anonymous user
	 * @throws IOException if an exception occurred
	 * @throws InvalidSyntaxException if an exception occurred
	 */
	public Optional<Configuration> getOptional(long companyId)
		throws InvalidSyntaxException, IOException;

	/**
	 * Get a Configuration Optional for the given company
	 * @param  companyId the company ID
	 * @return the anonymous user
	 * @throws IOException if an exception occurred
	 * @throws InvalidSyntaxException if an exception occurred
	 */
	public Optional<Configuration> getOptional(long companyId, long userId)
		throws InvalidSyntaxException, IOException;

}