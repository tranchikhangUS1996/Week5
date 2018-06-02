package com.example.lap60020_local.retrofit;

import dagger.Component;

@Component(modules = AdapterModule.class)
public interface FragmentComponent {
    void inject(PopularFragment fragment);
    void inject(TopRatedFragment fragment);
    void inject(UpComingFragment fragment);
    void inject(NowPlayingFragment fragment);
}
