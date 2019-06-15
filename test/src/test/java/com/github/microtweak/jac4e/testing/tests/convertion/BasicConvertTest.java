package com.github.microtweak.jac4e.testing.tests.convertion;

import com.github.microtweak.jac4e.core.BaseEnumAttributeConverter;
import com.github.microtweak.jac4e.testing.beans.Gender;
import com.github.microtweak.jac4e.testing.beans.Payment;
import com.github.microtweak.jac4e.testing.beans.YesNo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasicConvertTest {

    @Test
    public void convertEnumCharacter() {
        BaseEnumAttributeConverter<Gender, Character> converter = new BaseEnumAttributeConverter<>(Gender.class, Character.class);

        Assertions.assertAll(
            () -> assertEquals(Gender.MALE, converter.convertToEntityAttribute('M')),
            () -> Assertions.assertEquals((Character) 'M', converter.convertToDatabaseColumn(Gender.MALE))
        );
    }

    @Test
    public void convertEnumBoolean() {
        BaseEnumAttributeConverter<YesNo, Boolean> converter = new BaseEnumAttributeConverter<>(YesNo.class, Boolean.class);

        Assertions.assertAll(
            () -> assertEquals(YesNo.YES, converter.convertToEntityAttribute(true)),
            () -> Assertions.assertEquals(true, converter.convertToDatabaseColumn(YesNo.YES))
        );
    }

    @Test
    public void convertEnumInteger() {
        BaseEnumAttributeConverter<Payment, Integer> converter = new BaseEnumAttributeConverter<>(Payment.class, Integer.class);

        Assertions.assertAll(
            () -> assertEquals(Payment.CREDIT_CARD, converter.convertToEntityAttribute(1)),
            () -> Assertions.assertEquals((Integer) 1, converter.convertToDatabaseColumn(Payment.CREDIT_CARD))
        );
    }

    @Test
    public void convertFromNull() {
        BaseEnumAttributeConverter<Payment, Integer> converter = new BaseEnumAttributeConverter<>(Payment.class, Integer.class);

        Assertions.assertAll(
            () -> Assertions.assertNull(converter.convertToDatabaseColumn(null)),
            () -> Assertions.assertNull(converter.convertToEntityAttribute(null))
        );
    }

}
