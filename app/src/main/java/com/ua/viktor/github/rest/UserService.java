package com.ua.viktor.github.rest;

import com.ua.viktor.github.model.Users;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by viktor on 26.01.16.
 */
public interface UserService {
    @GET("/user")
    Call<Users> getUserA(
            @Query("access_token") String token
    );

    @GET("/users/{username}")
     Call<Users> getSingleUser(
            @Path("username") String username,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret
     );

    @GET("/user/following/{username}")
    Call<Users> checkUserFollowing(
            @Path("username") String username,
            @Query("access_token") String token
    );

    @PUT("/user/following/{username}")
    @Headers("Content-Length: 0")
    Call<Users> followUser(
            @Path("username") String username,
            @Query("access_token") String token
    );

    @DELETE("/user/following/{username}")
    @Headers("Content-Length: 0")
    Call<Users> deleteFollowUser(
            @Path("username") String username,
            @Query("access_token") String token
    );

}
