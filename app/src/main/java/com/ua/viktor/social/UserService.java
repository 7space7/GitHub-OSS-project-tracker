package com.ua.viktor.social;

import com.ua.viktor.social.model.Users;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by viktor on 26.01.16.
 */
public interface UserService {
    @GET("/user")
    Call<Users> contributors(
            @Query("access_token") String token
    );
}
