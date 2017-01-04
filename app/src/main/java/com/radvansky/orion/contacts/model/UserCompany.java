package com.radvansky.orion.contacts.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by tomasradvansky on 04/01/2017.
 * "company": {
                "name": "Romaguera-Crona",
                "catchPhrase": "Multi-layered client-server neural-net",
                "bs": "harness real-time e-markets"
 }
 */

public class UserCompany implements Serializable {

    @Expose
    public String name;

    @Expose
    public String catchPhrase;

    @Expose
    public String bs;

    public String getFriendlyCompany()
    {
        return replaceNull(name) + "\n" + replaceNull(catchPhrase) + "\n" + replaceNull(bs);
    }

    private static String replaceNull(String input) {
        return input == null ? "" : input;
    }
}
