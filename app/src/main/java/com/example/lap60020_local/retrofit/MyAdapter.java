package com.example.lap60020_local.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {


    private final int LOADING = 1001;
    private final int CARD = 1002;
    private final int MAXIMUM = 10;
    private Context context;
    @Inject
    List<Movie> Movies;
    @Inject
    List<Movie> MoviesFilterd;
    @Inject
    LinearLayoutManager linearLayoutManager;
    private boolean isLoading = false;
    private MyLoader myLoader;
    @Inject
    MyApi api;
    private int page = 1;
    private int MaxPage = 1;
    private int Threshold = 0;


    // filter
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = (constraint.toString().toLowerCase());
                List<Movie> queryList;
                if(query.isEmpty()) {
                    queryList = Movies;
                } else {
                    queryList = new ArrayList<>();
                    for(Movie m : Movies) {
                        if(m.getTitle().toLowerCase().contains(query)) {
                            queryList.add(m);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = queryList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                MoviesFilterd = (List<Movie>) results.values;
                Threshold = MoviesFilterd.size()> MAXIMUM ? MAXIMUM : MoviesFilterd.size();
                notifyDataSetChanged();
            }
        };
    }

    class LoadingHolder extends RecyclerView.ViewHolder{
        LoadingHolder(View itemView) {
            super(itemView);
            ProgressBar progressBar = itemView.findViewById(R.id.loading_progressbar);
            progressBar.setIndeterminate(true);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder{

        public ImageView poster;
        TextView release;
        TextView movieTitle;
        public TextView overView;
        public CardView card;
        public TextView rate;

        MyHolder(CardView itemView) {
            super(itemView);
            card =  itemView;
            poster = itemView.findViewById(R.id.poster);
            release = itemView.findViewById(R.id.date);
            movieTitle = itemView.findViewById(R.id.title);
            overView = itemView.findViewById(R.id.overView);
            rate = itemView.findViewById(R.id.rate);
        }
    }

    public MyAdapter(Context context,RecyclerView recyclerView) {
        this.context = context;
          Component component = DaggerComponent
                  .builder()
                  .myAdapterModule(new MyAdapterModule(context))
                  .build();
          component.inject(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView irecyclerView, int newState) {
                super.onScrollStateChanged(irecyclerView, newState);
                // tinh so item trong list
                int totalItem = Threshold;
                // tinh item cuoi cung nhin thay
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                // neu con 1 item o cuoi thi tien hanh load
                if(totalItem>0 && !isLoading && totalItem <= lastVisibleItem+1) {
                    // load them data tang so trang len de load trang ke tiep tu web
                    if(MoviesFilterd == Movies && Threshold == Movies.size()) {
                        page++;
                        // them item null de bao hieu tao loading
                        if(page <= MaxPage) {
                            MoviesFilterd.add(null);
                            notifyItemInserted(Threshold);
                        }
                        // goi load data
                        loaddata();
                    }
                    // show them data
                    else {
                        int oldThres = Threshold;
                        Threshold = (MoviesFilterd.size() - Threshold)> MAXIMUM ? Threshold + MAXIMUM : MoviesFilterd.size();
                        notifyItemRangeInserted(oldThres,Threshold-oldThres);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        MoviesFilterd.add(null);
        Threshold = 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(MoviesFilterd.get(position)==null) return LOADING;
        return CARD;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==LOADING) {
            View v = LayoutInflater.from(context)
                    .inflate(R.layout.loading_layout,parent,false);
            return new LoadingHolder(v);
        } else {
            CardView cardView = (CardView) LayoutInflater.from(context)
                    .inflate(R.layout.card_view, parent, false);
            return new MyHolder(cardView);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderinput, int position) {
        Movie movie = MoviesFilterd.get(position);
        // set data card
        if(holderinput instanceof MyHolder) {
            MyHolder holder = (MyHolder) holderinput;
            holder.movieTitle.setText(movie.getTitle());
            holder.release.setText(movie.getReleaseDate());
            String over = movie.getOverview();
            int length = over.length();
            try {
                if(length > 100) {
                    String description = over.substring(0, 100 - 1).concat("...");
                    holder.overView.setText(description);
                } else {
                    holder.overView.setText(over);
                }
            } catch(Exception e) {
                Log.d("Error",e.getMessage()
                        + movie.getTitle()
                        + " length = "
                        + String.valueOf(over.length()));
            }
            holder.rate.setText("Rate: " + String.valueOf(movie.getVoteAverage()));
            //
            String imagePath = MyApiClient.IMAGE_PATH + movie.getPosterPath();
            GlideApp.with(context)
                    .load(Uri.parse(imagePath))
                    .placeholder(R.drawable.placeholder)
                    .into(holder.poster);
            // sets clicklistener for each items
            holder.card.setOnClickListener(new MyclickListener(movie.getId()));
        }
    }

    @Override
    public int getItemCount() {
        return Threshold;
    }

    public void loaddata() {
        // danh dau dang load data
        if(!isLoading && page<=MaxPage) {
            Log.d("ghi log", String.valueOf(page)+"trang");
            isLoading = true;
            Call<MovieResponde> call = myLoader.load(api, page);
            call.enqueue(new Callback<MovieResponde>() {
                // load completed or failed
                @Override
                public void onResponse(@NonNull Call<MovieResponde> call, Response<MovieResponde> response) {
                    try {
                        List<Movie> list = response.body().getResults();
                        MaxPage = response.body().getTotalPages();
                        if (Movies == null || Movies.size() == 0) {
                            // load moi
                            Movies = list;
                            MoviesFilterd = Movies;
                            Threshold = MoviesFilterd.size() >= MAXIMUM ? MAXIMUM : MoviesFilterd.size();
                            notifyDataSetChanged();
                        } else {
                            //load them data
                            int size = MoviesFilterd.size() - 1;
                            // bo item null ra
                            MoviesFilterd.remove(size);
                            notifyItemRemoved(size);

                            Movies.addAll(list);
                            MoviesFilterd = Movies;
                            int oldThres = Threshold;
                            Threshold = list.size() > MAXIMUM ? Threshold + MAXIMUM : Threshold + list.size();
                            notifyItemRangeInserted(oldThres, Threshold - oldThres);
                        }
                    } catch (NullPointerException e) {
                            Log.e("Error",e.getMessage());
                    }
                    isLoading = false;
                }

                @Override
                public void onFailure(@NonNull Call<MovieResponde> call, @NonNull Throwable t) {
                    Log.d("onFailure", t.getMessage());
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                    // fail thi load lai
                    isLoading = false;
                    loaddata();
                }
            });
        }
    }

    public void setLoader(MyLoader loader) {
        this.myLoader = loader;
    }

}
