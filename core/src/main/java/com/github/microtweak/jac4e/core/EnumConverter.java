package com.github.microtweak.jac4e.core;

import com.github.microtweak.jac4e.core.exception.EnumValueDuplicateException;
import com.github.microtweak.jac4e.core.exception.EnumValueNotPresentException;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public abstract class EnumConverter<E extends Enum, V extends Serializable> {

    @NonNull @Getter
    private Class<E> enumType;

    @NonNull @Getter @Setter
    private Class<V> valueType;

    @Getter @Setter
    private boolean errorIfValueNotPresent;

    protected final Map<E, V> values = new HashMap<>();
    protected final Map<V, E> constants = new HashMap<>();

    protected void putConstant(E contant, V value) {
        if (constants.containsKey(value)) {
            throw new EnumValueDuplicateException(
                "There are one or more constants in the " + enumType.getName() + " enum whose \"" + 1 + "\" attribute has the value \"" + value + "\"!"
            );
        }

        values.put(contant, value);
        constants.put(value, contant);
    }

    public boolean isInitialized() {
        return !values.isEmpty() && !constants.isEmpty();
    }

    protected abstract void initializeConverter();

    public V toValue(E constant) {
        initializeConverter();
        return constant == null ? null : values.get(constant);
    }

    protected void checkValueNotPresent(E constant, V value) {
        if (!errorIfValueNotPresent || constant != null) {
            return;
        }

        throw new EnumValueNotPresentException(
            "The \"" + value + "\" value is not present in any constant of " + enumType.getName() + " enum!"
        );
    }

    public E fromValue(V value) {
        initializeConverter();

        if (value == null) {
            return null;
        }

        E constant = constants.get(value);

        checkValueNotPresent(constant, value);

        return constant;
    }

}
