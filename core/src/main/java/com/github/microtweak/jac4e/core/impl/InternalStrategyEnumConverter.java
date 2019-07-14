package com.github.microtweak.jac4e.core.impl;

import com.github.microtweak.jac4e.core.EnumConverter;
import com.github.microtweak.jac4e.core.exception.EnumMetadataException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;

public class InternalStrategyEnumConverter<E extends Enum, V extends Serializable> extends EnumConverter<E, V> {

    private Method toValueMethod;
    private Method fromValueStaticMethod;

    private final Function<E, V> invokeToValueFunction;
    private final Function<V, E> invokeFromValueFunction;

    public InternalStrategyEnumConverter(Class<E> enumType, Class<V> valueType) {
        super(enumType, valueType);

        invokeToValueFunction = (enumConstant) -> {
            try {
                return (V) toValueMethod.invoke(enumConstant);
            } catch (IllegalAccessException e) {
                return ExceptionUtils.rethrow(e);
            } catch (InvocationTargetException e) {
                return ExceptionUtils.rethrow(e.getTargetException());
            }
        };

        invokeFromValueFunction = (valueParam) -> {
            try {
                return (E) fromValueStaticMethod.invoke(null, valueParam);
            } catch (IllegalAccessException e) {
                return ExceptionUtils.rethrow(e);
            } catch (InvocationTargetException e) {
                return ExceptionUtils.rethrow(e.getTargetException());
            }
        };
    }

    @Override
    protected void initializeConverter() {
        /*
         * Find and validate "toValue" method
         */
        toValueMethod = MethodUtils.getMatchingMethod(getEnumType(), "toValue");

        checkMethodPresent(toValueMethod, "Method \"public %s toValue()\" was not found!", getValueType().getName());

        checkMethodDeclaration(toValueMethod, false, getValueType());

        /*
         * Find and validate "fromValue" method
         */
        fromValueStaticMethod = MethodUtils.getAccessibleMethod(getEnumType(), "fromValue", getValueType());

        checkMethodPresent(fromValueStaticMethod, "Method \"public static %s fromValue(%s value))\" was not found!", getEnumType().getName(), getValueType().getName());

        checkMethodDeclaration(fromValueStaticMethod, true, getEnumType());
    }

    private void checkMethodPresent(Method method, String message, Object... args) {
        if (method == null) {
            throw new EnumMetadataException(message, args);
        }
    }

    private void checkMethodDeclaration(Method method, boolean shouldStatic, Class<?> returnType) {
        int mod = method.getModifiers();

        if (!isPublic(mod)) {
            throw new EnumMetadataException("Method \"%s\" must be public", method.getName());
        }

        if (shouldStatic && !isStatic(mod)) {
            throw new EnumMetadataException("Method \"%s\" must be static", method.getName());
        }
        else if (!shouldStatic && isStatic(mod)) {
            throw new EnumMetadataException("The \"%s\" method must be non-static", method.getName());
        }

        // TODO Review when return was primitive type
        if (!method.getReturnType().equals(returnType)) {
            throw new EnumMetadataException("The return of method \"%s\" should be \"%s\"", method.getName(), returnType.getName());
        }
    }

    @Override
    public V toValue(E constant) {
        initializeConverter();

        if (constant == null) {
            return null;
        }

        return values.computeIfAbsent(constant, invokeToValueFunction);
    }

    @Override
    public E fromValue(V value) {
        initializeConverter();

        if (value == null) {
            return null;
        }

        E constant = constants.computeIfAbsent(value, invokeFromValueFunction);

        checkValueNotPresent(constant, value);

        return constant;
    }

}