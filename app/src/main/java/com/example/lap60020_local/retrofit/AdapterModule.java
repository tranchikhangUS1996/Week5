package com.example.lap60020_local.retrofit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import javax.inject.Named;
import dagger.Module;
import dagger.Provides;


@Module
public class AdapterModule {
    private Context context;
    private RecyclerView recyclerView;

    public AdapterModule(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Provides
    @Named(value = "popular")
    public MyLoader providePopularloader() {
        PopularLoader popularLoader = new PopularLoader();
        return popularLoader;
    }

    @Provides
    @Named(value = "top_rated")
    public MyLoader provideTopRatedLoader() {
        TopRatedLoader topRatedLoader = new TopRatedLoader();
        return topRatedLoader;
    }

    @Provides
    @Named(value = "up_coming")
    public MyLoader provideUpcommingLoader() {
        UpcomingLoader upcomingLoader = new UpcomingLoader();
        return upcomingLoader;
    }

    @Provides
    @Named(value = "now_playing")
    public MyLoader provideNowPlayingLoader() {
        NowPlayingLoader nowPlayingLoader = new NowPlayingLoader();
        return nowPlayingLoader;
    }

    @Provides
    public MyAdapter provideAdapater() {
        return new MyAdapter(context,recyclerView);
    }


}
