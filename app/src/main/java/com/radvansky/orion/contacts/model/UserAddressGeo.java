package com.radvansky.orion.contacts.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by tomasradvansky on 04/01/2017.
 * "geo": {
        "lat": "-37.3159",
        "lng": "81.1496"
 }
 */

public class UserAddressGeo implements Serializable {

    @Expose
    public String lat;
    @Expose
    public String lng;

    public UserAddressGeo() {
        super();
    }
}
