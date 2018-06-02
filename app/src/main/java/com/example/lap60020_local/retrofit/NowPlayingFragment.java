package com.example.lap60020_local.retrofit;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;
import javax.inject.Named;



/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment {


    @Inject
    MyAdapter adapter;
    @Inject
    @Named(value = "now_playing")
    MyLoader nowPlayingLoader;

    public NowPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_now_playing, container, false);
        ProgressBar progressBar = v.findViewById(R.id.nowplaying_progresssbar);
        progressBar.setVisibility(View.VISIBLE);
        RecyclerView recyclerView = v.findViewById(R.id.nowplaying_recyclerView);
        FragmentComponent component = DaggerFragmentComponent
                .builder()
                .adapterModule(new AdapterModule(getContext(),recyclerView))
                .build();
        component.inject(this);
        adapter.setLoader(nowPlayingLoader);
        recyclerView.setAdapter(adapter);
        adapter.loaddata();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true)
    public void sendAdapter(Integer type) {
        if(type==3) {
            MessageEvent event = new MessageEvent(adapter);
            EventBus.getDefault().post(event);
        }
    }

}
