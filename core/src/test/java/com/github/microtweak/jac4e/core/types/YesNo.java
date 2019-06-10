package com.github.microtweak.jac4e.core.types;

public enum YesNo {

    YES(true),
    NO(false);

    private boolean value;

    YesNo(boolean value) {
        this.value = value;
    }
}
