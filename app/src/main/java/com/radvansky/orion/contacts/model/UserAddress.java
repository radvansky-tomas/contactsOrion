package com.radvansky.orion.contacts.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by tomasradvansky on 04/01/2017.
 * "address": {
                "street": "Kulas Light",
                "suite": "Apt. 556",
                "city": "Gwenborough",
                "zipcode": "92998-3874",
                "geo": {
                    "lat": "-37.3159",
                    "lng": "81.1496"
                }
 }
 */

public class UserAddress implements Serializable {

    @Expose
    public String street;

    @Expose
    public String suite;

    @Expose
    public String city;

    @Expose
    public String zipcode;

    @Expose
    public UserAddressGeo geo;



    public String getFriendlyAddress()
    {
        return replaceNull(suite) + ", " + replaceNull(street) + "\n" + replaceNull(city) + ", " + replaceNull(zipcode);
    }

    private static String replaceNull(String input) {
        return input == null ? "" : input;
    }
}
