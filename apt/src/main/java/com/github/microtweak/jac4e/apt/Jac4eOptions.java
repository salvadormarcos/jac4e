package com.github.microtweak.jac4e.apt;

import com.github.microtweak.jac4e.core.EnumAttributeConverter;
import com.github.microtweak.jac4e.core.ValueNotFoundStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class Jac4eOptions {

    private static final String PREFIX = "jac4e.";

    private static final String PROPERTY_PACKAGE_NAME = PREFIX + "packageName";
    private static final String PROPERTY_ATTRIBUTE_NAME = PREFIX + "attributeName";
    private static final String PROPERTY_ERROR_IF_VALUE_NOT_FOUND = PREFIX + "errorIfValueNotFound";

    private final EnumAttributeConverter enumOpts;

    private String aptPackageName;
    private String aptAttributeName;
    private boolean aptErrorIfValueNotPresent;

    public Jac4eOptions(EnumAttributeConverter enumOpts, Map<String, String> aptOptions) {
        this.enumOpts = enumOpts;
        aptOptions.forEach(this::setProperty);
    }

    private void setProperty(String propertyName, String value) {
        switch (propertyName) {
            case PROPERTY_PACKAGE_NAME:
                aptPackageName = value;
                break;

            case PROPERTY_ATTRIBUTE_NAME:
                aptAttributeName = value;
                break;

            case PROPERTY_ERROR_IF_VALUE_NOT_FOUND:
                aptErrorIfValueNotPresent = Boolean.parseBoolean(value);
                break;

            default:
                throw new UnsupportedOperationException("The property \"" + propertyName + "\" is not supported!");
        }
    }

    public String getPackageName() {
        return StringUtils.defaultIfBlank(enumOpts.packageName(), aptPackageName);
    }

    public String getAttributeName() {
        return StringUtils.defaultIfBlank(enumOpts.attributeName(), aptAttributeName);
    }

    public boolean isAutoApply() {
        return enumOpts.autoApply();
    }

    public boolean isErrorIfValueNotPresent() {
        ValueNotFoundStrategy valueNotFound = enumOpts.ifValueNotPresent();

        if (valueNotFound != ValueNotFoundStrategy.INHERITED) {
            return (valueNotFound == ValueNotFoundStrategy.THROW_ERROR);
        }

        return aptErrorIfValueNotPresent;
    }

}
