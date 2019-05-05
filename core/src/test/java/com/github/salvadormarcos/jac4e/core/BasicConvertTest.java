package com.github.salvadormarcos.jac4e.core;

import com.github.salvadormarcos.jac4e.core.types.Gender;
import com.github.salvadormarcos.jac4e.core.types.Payment;
import com.github.salvadormarcos.jac4e.core.types.YesNo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicConvertTest {

    @Test
    public void convertEnumCharacter() {
        BaseEnumAttributeConverter<Gender, Character> converter = new BaseEnumAttributeConverter<>(Gender.class, Character.class);

        assertAll(
            () -> assertEquals(Gender.MALE, converter.convertToEntityAttribute('M')),
            () -> assertEquals((Character) 'M', converter.convertToDatabaseColumn(Gender.MALE))
        );
    }

    @Test
    public void convertEnumBoolean() {
        BaseEnumAttributeConverter<YesNo, Boolean> converter = new BaseEnumAttributeConverter<>(YesNo.class, Boolean.class);

        assertAll(
            () -> assertEquals(YesNo.YES, converter.convertToEntityAttribute(true)),
            () -> assertEquals(true, converter.convertToDatabaseColumn(YesNo.YES))
        );
    }

    @Test
    public void convertEnumInteger() {
        BaseEnumAttributeConverter<Payment, Integer> converter = new BaseEnumAttributeConverter<>(Payment.class, Integer.class);

        assertAll(
            () -> assertEquals(Payment.CREDIT_CARD, converter.convertToEntityAttribute(1)),
            () -> assertEquals((Integer) 1, converter.convertToDatabaseColumn(Payment.CREDIT_CARD))
        );
    }

}
