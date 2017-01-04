package com.radvansky.orion.contacts.interfaces;

import com.radvansky.orion.contacts.model.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by tomasradvansky on 07/01/16.
 */
public interface WS {
    @GET("/users")
    Call<List<User>> GetUsers();
}
