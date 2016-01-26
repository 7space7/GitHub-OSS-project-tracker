package com.ua.viktor.social;

import com.squareup.okhttp.OkHttpClient;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by viktor on 26.01.16.
 */
public class ServiceGenerator {
    public static final String API_BASE_URL = "https://api.github.com";

    private static OkHttpClient httpClient = new OkHttpClient();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
