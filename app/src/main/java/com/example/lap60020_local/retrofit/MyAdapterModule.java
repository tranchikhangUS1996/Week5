package com.example.lap60020_local.retrofit;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class MyAdapterModule {

    private Context context;

    MyAdapterModule(Context context) {
        this.context = context;
    }

    @Provides
    public LinearLayoutManager provideLinearlayoutManager() {
        return new LinearLayoutManager(context);
    }

    @Provides
    @Singleton
    public Retrofit provideRetrofit() {
        return MyApiClient.getInstance();
    }

    @Provides
    public List<Movie> provideList() {
        return new ArrayList<>();
    }

    @Provides
    public MyApi provideApi(Retrofit retrofit) {
        return retrofit.create(MyApi.class);
    }
}
