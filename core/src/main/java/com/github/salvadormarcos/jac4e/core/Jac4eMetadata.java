package com.github.salvadormarcos.jac4e.core;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Jac4eMetadata<E extends Enum> {

    private static Map<Class<? extends Enum>, Jac4eMetadata> CACHE = new HashMap<>();

    private String attributeName;

    private Class<E> enumType;
    private Class<?> valueType;

    private Map<E, Object> valuesByEnumConstants = new HashMap<>();
    private Map<Object, E> enumConstantsByValues = new HashMap<>();

    private Jac4eMetadata(Class<E> enumType) {
        this.enumType = enumType;

        EnumAttributeConverter jpaEnumAnnotation = enumType.getAnnotation(EnumAttributeConverter.class);

        if (jpaEnumAnnotation == null) {
            throw new Jac4eException("The enum " + enumType.getName() + " should be annotated with @" + EnumAttributeConverter.class.getName());
        }

        try {
            Field valueAttribute = enumType.getDeclaredField(jpaEnumAnnotation.attributeName());

            attributeName = valueAttribute.getName();

            valueAttribute.setAccessible(true);

            valueType = valueAttribute.getType();

            for (E instance : enumType.getEnumConstants()) {
                Object value = valueAttribute.get(instance);

                valuesByEnumConstants.put(instance, value);
                enumConstantsByValues.put(value, instance);
            }
        } catch (NoSuchFieldException e) {
            throw new Jac4eException("There is no " + attributeName + " attribute declared in enum " + EnumAttributeConverter.class.getName());
        } catch (IllegalAccessException e) {
            throw new Jac4eException("Unable to read " + attributeName + " attribute of enum " + enumType.getName());
        }
    }

    public synchronized static Jac4eMetadata of(Class<? extends Enum> clazz) {
        if (!clazz.isEnum()) {
            throw new Jac4eException("The class " + clazz.getName() + " is not an Enum!");
        }

        Jac4eMetadata metadata = CACHE.get(clazz);

        if (metadata == null) {
            metadata = new Jac4eMetadata<>(clazz);
            CACHE.put(clazz, metadata);
        }

        return metadata;
    }

    public Object convertToValue(E instance) {
        return valuesByEnumConstants.get(instance);
    }

    public E convertToEnumInstance(Object value) {
        return enumConstantsByValues.get(value);
    }

    public String getAttributeName() {
        return attributeName;
    }

    public Class<E> getEnumType() {
        return enumType;
    }

    public Class<?> getValueType() {
        return valueType;
    }

}
