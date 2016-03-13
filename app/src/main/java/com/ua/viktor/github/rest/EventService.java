package com.ua.viktor.github.rest;

import com.ua.viktor.github.model.Event;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


/**
 * Created by viktor on 28.01.16.
 */
public interface EventService {
    @GET("users/{owner}/received_events/public")
    Call<List<Event>> userEvent(
            @Path("owner") String owner,
            @Query("client_id") String client_id,
            @Query("client_secret") String client_secret

    );
}
