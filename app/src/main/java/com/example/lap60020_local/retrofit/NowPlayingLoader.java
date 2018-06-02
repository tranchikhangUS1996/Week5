package com.example.lap60020_local.retrofit;

import retrofit2.Call;

public class NowPlayingLoader implements MyLoader {
    @Override
    public Call<MovieResponde> load(MyApi api, int page) {
        return api.getNowPlayingMovies(page,MyApiClient.MY_KEY);
    }
}
