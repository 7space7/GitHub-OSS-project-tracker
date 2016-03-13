package com.ua.viktor.github.rest;


import com.ua.viktor.github.model.Organizations;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by viktor on 02.02.16.
 */
public interface OrganizationService {
    @GET("/users/{owner}/orgs")
    Call<ArrayList<Organizations>> getOrgs(
            @Path("owner") String owner,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret
    );

    @GET("/users/{owner}/following")
    Call<ArrayList<Organizations>> getFollowing(
            @Path("owner") String owner,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret
    );

    @GET("/users/{owner}/followers")
    Call<ArrayList<Organizations>> getFollowers(
            @Path("owner") String owner,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret
    );
}
