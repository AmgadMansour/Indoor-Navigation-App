package com.startupcompany.wireframe.util;

/**
 * Created by mohamedabdel-azeem on 3/16/16.
 */

import com.startupcompany.wireframe.model.User;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface PublicApiRoutes {
    // LOG IN
    @POST("/sessions")
    @FormUrlEncoded
    void login(@Field("session[username]") String username,
               @Field("session[password]") String password,
               Callback<User> callback);

    // SIGN UP
    @POST("/users")
    @FormUrlEncoded
    void signUp(@Field("user[first_name]") String firstName,
                @Field("user[last_name]") String lastName,
                @Field("user[day]") int day,
                @Field("user[month]") int month,
                @Field("user[year]") int year,
                @Field("user[gender]") String gender,
                @Field("user[username]") String username,
                @Field("user[password]") String password,
                Callback<User> callback);
}