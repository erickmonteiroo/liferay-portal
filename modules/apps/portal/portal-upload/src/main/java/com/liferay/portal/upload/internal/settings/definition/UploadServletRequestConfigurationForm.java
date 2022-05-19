package com.liferay.portal.upload.internal.settings.definition;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayout;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;

@DDMForm
@DDMFormLayout(
	paginationMode = com.liferay.dynamic.data.mapping.model.DDMFormLayout.SINGLE_PAGE_MODE,
	value = {
		@DDMFormLayoutPage(
			{
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {"maxSize", "maxTries", "tempDir"}
						)
					}
				)
			}
		)
	}
)
public interface UploadServletRequestConfigurationForm {

	@DDMFormField(
		label = "%max-size",
		validationErrorMessage = "funcionou",
		validationExpression = "(maxSize >= 1000) && (maxSize <= 30000)",
		type = "numeric",
		predefinedValue = "104857600",
		tip = "descricao",
		required = true)
	public long maxSize();

	@DDMFormField(
		label = "%check-token",
		tip = "%users-image-check-token-help",
		name = "overall-maximum-unique-file-name-tries", required = false
	)
	public long maxTries();

	@DDMFormField(
		label = "%check-token",
		tip = "%users-image-check-token-help"
	)
	public String tempDir();
}
