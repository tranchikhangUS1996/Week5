package com.example.lap60020_local.retrofit;

import retrofit2.Call;

public class PopularLoader implements MyLoader {
    @Override
    public Call<MovieResponde> load(MyApi api, int page) {
        return api.getPopularMovies(page,MyApiClient.MY_KEY);
    }
}
