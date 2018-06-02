package com.example.lap60020_local.retrofit;

import android.support.v4.app.Fragment;

import javax.inject.Singleton;


@Singleton
@dagger.Component(modules = {MyAdapterModule.class})
public interface Component {

    void inject(MyAdapter adapter);
    void inject(DetailActivity detailActivity);
}
