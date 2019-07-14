package com.github.microtweak.jac4e.core.impl;

import com.github.microtweak.jac4e.core.EnumConverter;

public class FirstLetterConstantEnumConverter<E extends Enum> extends EnumConverter<E, String> {

    public FirstLetterConstantEnumConverter(Class<E> enumType) {
        super(enumType, String.class);
    }

    @Override
    protected void initializeConverter() {
        if (isInitialized()) {
            return;
        }

        for (E constant : getEnumType().getEnumConstants()) {
            final String value = constant.name().substring(0, 1);
            putConstant(constant, value);
        }
    }

}
