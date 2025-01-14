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

declare module 'data-engine-js-components-web' {
	function FieldFeedback({
		errorMessage,
		helpMessage,
		warningMessage,
	}: {
		errorMessage?: string;
		helpMessage?: string;
		warningMessage?: string;
	}): JSX.Element;

	function useFeatureFlag(): {
		[key in Flags]: boolean;
	};
}

type Flags = 'LPS-144957' | 'LPS-147651';
