package com.example.lap60020_local.retrofit;

import retrofit2.Call;

public interface MyLoader {
    Call<MovieResponde> load(MyApi api ,int page);
}
