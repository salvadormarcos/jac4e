package com.github.microtweak.jac4e.testing.tests.convertion;

import com.github.microtweak.jac4e.core.BaseEnumAttributeConverter;
import com.github.microtweak.jac4e.core.exception.EnumMetadataException;
import com.github.microtweak.jac4e.core.exception.EnumValueDuplicateException;
import com.github.microtweak.jac4e.core.exception.EnumValueNotPresentException;
import com.github.microtweak.jac4e.testing.beans.Country;
import com.github.microtweak.jac4e.testing.beans.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomOptionsConvertTest {

    @Test
    public void convertEnumWithCustomAttributeName() {
        BaseEnumAttributeConverter<Country, String> converter = new BaseEnumAttributeConverter<>(Country.class, String.class);

        Assertions.assertThrows(EnumMetadataException.class, () -> assertEquals(Country.AUSTRALIA, converter.convertToEntityAttribute("AU")));

        converter.setAttributeName("isoCode");

        Assertions.assertAll(
            () -> assertEquals(Country.BRAZIL, converter.convertToEntityAttribute("BR")),
            () -> Assertions.assertEquals("BR", converter.convertToDatabaseColumn(Country.BRAZIL))
        );
    }

    @Test
    public void errorOnDuplicateValues() {
        BaseEnumAttributeConverter<Country, Integer> converter = new BaseEnumAttributeConverter<>(Country.class, Integer.class);
        converter.setAttributeName("callingCode");

        Assertions.assertThrows(EnumValueDuplicateException.class, () -> assertEquals(Country.UNITED_STATES, converter.convertToEntityAttribute(1)));
    }

    @Test
    public void convertEnumWithUnknownValue() {
        BaseEnumAttributeConverter<Payment, Integer> converter = new BaseEnumAttributeConverter<>(Payment.class, Integer.class);

        Assertions.assertNull(converter.convertToEntityAttribute(100));

        converter.setErrorIfValueNotPresent(true);

        Assertions.assertThrows(EnumValueNotPresentException.class, () -> converter.convertToEntityAttribute(100));
    }

}
