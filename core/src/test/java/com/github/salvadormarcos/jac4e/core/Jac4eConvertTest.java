package com.github.salvadormarcos.jac4e.core;

import com.github.salvadormarcos.jac4e.core.types.Gender;
import com.github.salvadormarcos.jac4e.core.types.YesNo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Jac4eConvertTest {

    @Test
    public void convertToEnumInstance() {
        Gender male = (Gender) Jac4eMetadata.of(Gender.class).convertToEnumInstance('M');

        assertEquals(Gender.MALE, male);

        YesNo yes = (YesNo) Jac4eMetadata.of(YesNo.class).convertToEnumInstance(true);

        assertEquals(YesNo.YES, yes);
    }

    @Test
    public void convertToValue() {
        char male = (char) Jac4eMetadata.of(Gender.class).convertToValue(Gender.MALE);

        assertEquals('M', male);

        boolean yes = (boolean) Jac4eMetadata.of(YesNo.class).convertToValue(YesNo.YES);

        assertEquals(true, yes);
    }

}
