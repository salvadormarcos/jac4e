package com.github.microtweak.jac4e.testing.beans;

import com.github.microtweak.jac4e.core.EnumAttributeConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnumAttributeConverter
public enum Gender {

    MALE('M'),
    FEMALE('F');

    private char value;

}
