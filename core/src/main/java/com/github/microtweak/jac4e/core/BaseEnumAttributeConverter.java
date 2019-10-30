package com.github.microtweak.jac4e.core;

import com.github.microtweak.jac4e.core.exception.EnumMetadataException;
import com.github.microtweak.jac4e.core.exception.EnumValueDuplicateException;
import com.github.microtweak.jac4e.core.exception.EnumValueNotPresentException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.AttributeConverter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BaseEnumAttributeConverter<E, V> implements AttributeConverter<E, V> {

    public static final String DEFAULT_ATTRIBUTE_NAME = "value";

    private Class<E> enumType;
    private Class<V> valueType;

    private String attributeName;
    private boolean errorIfValueNotPresent;

    private Map<E, V> values;
    private Map<V, E> constants;

    private boolean initialized;

    public BaseEnumAttributeConverter(Class<E> enumType, Class<V> valueType) {
        this.enumType = enumType;
        this.valueType = valueType;

        this.attributeName = DEFAULT_ATTRIBUTE_NAME;
    }

    private synchronized void checkAndInitializeConverter() {
        if (initialized) {
            return;
        }

        try {
            Field enumValueAttribute = enumType.getDeclaredField(attributeName);

            validateValueTypeOfConverter(enumValueAttribute.getType());

            enumValueAttribute.setAccessible(true);

            values = new HashMap<>();
            constants = new HashMap<>();

            for (E enumConstant : enumType.getEnumConstants()) {
                V enumValue = (V) enumValueAttribute.get(enumConstant);

                if (constants.containsKey(enumValue)) {
                    throw new EnumValueDuplicateException(
                        "There are one or more constants in the " + enumType.getName() + " enum whose \"" + attributeName + "\" attribute has the value \"" + enumValue + "\"!"
                    );
                }

                values.put(enumConstant, enumValue);
                constants.put(enumValue, enumConstant);
            }

            initialized = true;
        } catch (NoSuchFieldException e) {
            throw new EnumMetadataException(
                "There is no \"" + attributeName + "\" attribute declared in enum \"" + enumType.getName() + "\"!"
            );
        } catch (IllegalAccessException e) {
            throw new EnumMetadataException(
                "Unable to read \"" + attributeName + "\" attribute of enum " + enumType.getName() + "\"!"
            );
        }
    }

    private void validateValueTypeOfConverter(Class<?> enumValueAttrType) {
        if (enumValueAttrType.isPrimitive()) {
            valueType = (Class<V>) ClassUtils.wrapperToPrimitive(valueType);
        }

        final String property = enumType.getName() + "." + attributeName;

        if (!valueType.equals(enumValueAttrType)) {
            throw new EnumMetadataException(
                "The type (" + enumValueAttrType.getName() + ") property \"" + property + "\" is different from the type (" + valueType.getName() + ") informed the AttributeConverter!"
            );
        }
    }

    @Override
    public V convertToDatabaseColumn(E attribute) {
        checkAndInitializeConverter();
        return attribute == null ? null : values.get(attribute);
    }

    @Override
    public E convertToEntityAttribute(V dbData) {
        checkAndInitializeConverter();

        if (dbData == null) {
            return null;
        }

        E constant = constants.get(dbData);

        if (errorIfValueNotPresent && constant == null) {
            throw new EnumValueNotPresentException(
                "The \"" + dbData + "\" value is not present in any constant of " + enumType.getName() + " enum!"
            );
        }

        return constant;
    }

    public void setAttributeName(String attributeName) {
        if (StringUtils.isBlank(attributeName)) {
            return;
        }
        this.attributeName = attributeName.trim();
    }

    public void setErrorIfValueNotPresent(boolean errorIfValueNotPresent) {
        this.errorIfValueNotPresent = errorIfValueNotPresent;
    }

}