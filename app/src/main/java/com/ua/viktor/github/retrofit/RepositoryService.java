package com.ua.viktor.github.retrofit;

import com.ua.viktor.github.model.Repositories;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by viktor on 28.01.16.
 */
public interface RepositoryService {
    @GET("users/{owner}/repos")
    Call<ArrayList<Repositories>> userRepositories(
            @Path("owner") String owner,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret
    );
    @GET("users/{owner}/starred")
    Call<ArrayList<Repositories>> repoStarred(
            @Path("owner") String owner,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret
    );
    @GET("users/{owner}/watched")
    Call<ArrayList<Repositories>> repoWatched(
            @Path("owner") String owner,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret
    );
}
