package com.example.lap60020_local.retrofit;

import android.app.SearchManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    private MyAdapter adapter;

    @Override
    public boolean onQueryTextSubmit(String query) {
        if(adapter!=null) {
            adapter.getFilter().filter(query);
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(adapter!=null) {
            adapter.getFilter().filter(newText);
        }
        return false;
    }

    class MyPagerAdpter extends FragmentPagerAdapter{

        FragmentManager fm;
        MyPagerAdpter(FragmentManager fm) {
            super(fm);
            this.fm = fm;
        }


        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new PopularFragment();
                case 1:
                    return new TopRatedFragment();
                case 2:
                    return new UpComingFragment();
                case 3:
                    return new NowPlayingFragment();
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return getResources().getString(R.string.popular);
                case 1:
                    return getResources().getString(R.string.top_rated);
                case 2:
                    return getResources().getString(R.string.upcoming);
                case 3:
                    return getResources().getString(R.string.nowplaying);
                default:
                    return null;
            }
        }


        @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ViewPager viewPager = findViewById(R.id.pager);
        TabLayout tabLayout = findViewById(R.id.tab);
        MyPagerAdpter pagerAdpter = new MyPagerAdpter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdpter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 0:
                        EventBus.getDefault().post(0);
                        break;
                    case 1:
                        EventBus.getDefault().post(1);
                        break;
                    case 2:
                        EventBus.getDefault().post(2);
                        break;
                    case 3:
                        EventBus.getDefault().post(3);
                        break;
                       default:
                           break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        EventBus.getDefault().postSticky(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search_menu);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setOnQueryTextListener(this);
        searchItem.expandActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setAdapter(MessageEvent event) {
        this.adapter = event.adapter;
    }


}
