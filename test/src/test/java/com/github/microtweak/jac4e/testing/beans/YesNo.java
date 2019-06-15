package com.github.microtweak.jac4e.testing.beans;

import com.github.microtweak.jac4e.core.EnumAttributeConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnumAttributeConverter
public enum YesNo {

    YES(true),
    NO(false);

    private boolean value;

}
