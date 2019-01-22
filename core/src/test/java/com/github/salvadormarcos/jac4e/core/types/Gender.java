package com.github.salvadormarcos.jac4e.core.types;

import com.github.salvadormarcos.jac4e.core.EnumAttributeConverter;

@EnumAttributeConverter
public enum Gender {

    MALE('M'),
    FEMALE('F');

    private char value;

    Gender(char value) {
        this.value = value;
    }
}
