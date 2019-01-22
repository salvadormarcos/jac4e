package com.github.salvadormarcos.jac4e.core.types;

import com.github.salvadormarcos.jac4e.core.EnumAttributeConverter;

@EnumAttributeConverter
public enum YesNo {

    YES(true),
    NO(false);

    private boolean value;

    YesNo(boolean value) {
        this.value = value;
    }
}
