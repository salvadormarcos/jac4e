package com.github.microtweak.jac4e.core.impl;

import com.github.microtweak.jac4e.core.EnumConverter;
import com.github.microtweak.jac4e.core.exception.EnumMetadataException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.Serializable;
import java.lang.reflect.Field;

public class SinglePropertyEnumConverter<E extends Enum, V extends Serializable> extends EnumConverter<E, V> {

    private String propertyName;

    public SinglePropertyEnumConverter(Class<E> enumType, Class<V> valueType, String propertyName) {
        super(enumType, valueType);
        this.propertyName = propertyName;
    }

    @Override
    protected void initializeConverter() {
        if (isInitialized()) {
            return;
        }

        try {
            Field property = FieldUtils.getDeclaredField(getEnumType(), propertyName, true);

            if (property == null) {
                final String msg = String.format("There is no \"%s\" attribute declared in enum \"%s\"!", propertyName, getEnumType().getName());
                throw new EnumMetadataException(msg);
            }

            validateValueTypeOfConverter(property.getType());

            for (E constant : getEnumType().getEnumConstants()) {
                V value = (V) property.get(constant);
                putConstant(constant, value);
            }
        } catch (IllegalAccessException e) {
            final String msg = String.format("Unable to read \"%s\" attribute of enum \"%s\"!", propertyName, getEnumType().getName());
            throw new EnumMetadataException(msg);
        }
    }

    private void validateValueTypeOfConverter(Class<?> propertyType) {
        if (propertyType.isPrimitive()) {
            setValueType( (Class<V>) ClassUtils.wrapperToPrimitive(getValueType()) );
        }

        final String property = getEnumType().getName() + "." + propertyName;

        if (!getValueType().equals(propertyType)) {
            final String msg = String.format(
                "The type (%s) property \"%s\" is different from the type (%s) informed the AttributeConverter!",
                propertyType.getName(), property, getValueType().getName()
            );
            throw new EnumMetadataException(msg);
        }
    }

}
