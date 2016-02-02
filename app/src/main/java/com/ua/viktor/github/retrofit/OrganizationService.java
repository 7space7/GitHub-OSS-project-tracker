package com.ua.viktor.github.retrofit;


import com.ua.viktor.github.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by viktor on 02.02.16.
 */
public interface OrganizationService {
    @GET("/users/{owner}/orgs")
    Call<List<Event>> getOrgs(
            @Path("owner") String owner
    );

    @GET("/users/{owner}/following")
    Call<List<Event>> getFollowing(
            @Path("owner") String owner
    );

    @GET("/users/{owner}/followers")
    Call<List<Event>> getFollowers(
            @Path("owner") String owner
    );
}
