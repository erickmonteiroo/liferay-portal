package com.liferay.portal.upload.internal.settings.definition;

import com.liferay.configuration.admin.definition.ConfigurationDDMFormDeclaration;
import org.osgi.service.component.annotations.Component;

@Component(
	immediate = true,
	property = "configurationPid=com.liferay.portal.upload.internal.configuration.UploadServletRequestConfiguration",
	service = ConfigurationDDMFormDeclaration.class
)
public class UploadServletRequestConfigurationDDMFormDeclaration
	implements ConfigurationDDMFormDeclaration {

	@Override
	public Class<?> getDDMFormClass() {
		return UploadServletRequestConfigurationForm.class;
	}

}
