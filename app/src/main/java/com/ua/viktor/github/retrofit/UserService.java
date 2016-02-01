package com.ua.viktor.github.retrofit;

import com.ua.viktor.github.model.Users;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * Created by viktor on 26.01.16.
 */
public interface UserService {
    @GET("/user")
    Call<Users> contributors(
            @Query("access_token") String token
    );
}
