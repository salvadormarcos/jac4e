package com.github.microtweak.jac4e.core.types;

public enum Payment {

    CASH(0),
    CREDIT_CARD(1),
    DEBIT_CARD(2),
    DIRECT_BILL(3),
    CHECK(4),
    PAYPAL(5);

    private int value;

    Payment(int value) {
        this.value = value;
    }
}
