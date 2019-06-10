package com.github.microtweak.jac4e.core.types;

public enum Country {

    AUSTRALIA("AU",61),
    BRAZIL("BR",55),
    CANADA("CA",1),
    SPAIN("ES",34),
    UNITED_STATES("US",1);

    private String isoCode; // ISO 3166 Code
    private int callingCode;

    Country(String isoCode, int callingCode) {
        this.isoCode = isoCode;
        this.callingCode = callingCode;
    }

}
