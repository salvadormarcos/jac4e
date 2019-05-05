package com.github.salvadormarcos.jac4e.core;

import com.github.salvadormarcos.jac4e.core.exception.EnumMetadataException;
import com.github.salvadormarcos.jac4e.core.exception.EnumValueDuplicateException;
import com.github.salvadormarcos.jac4e.core.exception.EnumValueNotPresentException;
import com.github.salvadormarcos.jac4e.core.types.Country;
import com.github.salvadormarcos.jac4e.core.types.Payment;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CustomOptionsConvertTest {

    @Test
    public void convertEnumWithCustomAttributeName() {
        BaseEnumAttributeConverter<Country, String> converter = new BaseEnumAttributeConverter<>(Country.class, String.class);

        assertThrows(EnumMetadataException.class, () -> assertEquals(Country.AUSTRALIA, converter.convertToEntityAttribute("AU")));

        converter.setAttributeName("isoCode");

        assertAll(
            () -> assertEquals(Country.BRAZIL, converter.convertToEntityAttribute("BR")),
            () -> assertEquals("BR", converter.convertToDatabaseColumn(Country.BRAZIL))
        );
    }

    @Test
    public void errorOnDuplicateValues() {
        BaseEnumAttributeConverter<Country, Integer> converter = new BaseEnumAttributeConverter<>(Country.class, Integer.class);
        converter.setAttributeName("callingCode");

        assertThrows(EnumValueDuplicateException.class, () -> assertEquals(Country.UNITED_STATES, converter.convertToEntityAttribute(1)));
    }

    @Test
    public void convertEnumWithUnknownValue() {
        BaseEnumAttributeConverter<Payment, Integer> converter = new BaseEnumAttributeConverter<>(Payment.class, Integer.class);

        assertNull(converter.convertToEntityAttribute(100));

        converter.setErrorIfValueNotPresent(true);

        assertThrows(EnumValueNotPresentException.class, () -> converter.convertToEntityAttribute(100));
    }

}
