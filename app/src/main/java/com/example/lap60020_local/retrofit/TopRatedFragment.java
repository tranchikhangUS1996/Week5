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
public class TopRatedFragment extends Fragment {


    @Inject
    MyAdapter adapter;
    @Inject
    @Named(value = "top_rated")
    MyLoader topratedLoader;

    public TopRatedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_top_rated, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.toprated_recyclerView);
        FragmentComponent component = DaggerFragmentComponent
                .builder()
                .adapterModule(new AdapterModule(getContext(),recyclerView))
                .build();
        component.inject(this);
        adapter.setLoader(topratedLoader);
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
        if(type==1) {
            MessageEvent event = new MessageEvent(adapter);
            EventBus.getDefault().post(event);
        }
    }
}
