package com.github.microtweak.jac4e.testing.beans;

import com.github.microtweak.jac4e.core.EnumAttributeConverter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@EnumAttributeConverter
public enum Payment {

    CASH(0),
    CREDIT_CARD(1),
    DEBIT_CARD(2),
    DIRECT_BILL(3),
    CHECK(4),
    PAYPAL(5);

    private int value;

}
