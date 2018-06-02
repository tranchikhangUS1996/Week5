package com.example.lap60020_local.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApiClient {

    private static Retrofit INSTANCE = null;
    private static final String BASE_URL = "http://api.themoviedb.org/3/";
    public static final String MY_KEY = "4a622a21346c1f3e6aca84e7dfefe1ad";
    public static final String IMAGE_PATH = "https://image.tmdb.org/t/p/original";

    public static Retrofit getInstance() {
        if(INSTANCE==null) {
            INSTANCE = new Retrofit
                    .Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return INSTANCE;
    }

}
