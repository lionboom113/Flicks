package com.d2q.demo.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Adapter.MovieItemAdapter;
import Adapter.MovieItemAdapterHorizontal;
import Model.Genre;
import Model.GenreWrapper;
import Model.Movie;
import Model.MyApiEndpointInterface;
import Model.ResultWrapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {
    ListView movieListView;
    private final String API = "https://api.themoviedb.org/3/";
    public HashMap<Integer, String> genreMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        genreMap = new HashMap<Integer, String>();
        setTitle("Now playing");
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();
//        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        MyApiEndpointInterface apiService =
                retrofit.create(MyApiEndpointInterface.class);
        Call<ResultWrapper> call = apiService.getAllMovie();
//        Call<GenreWrapper> getGenreInfo = apiService.getGenreInfo();
//        getGenreInfo.enqueue(new Callback<GenreWrapper>() {
//            @Override
//            public void onResponse(Call<GenreWrapper> call, Response<GenreWrapper> response) {
//                int statusCode = response.code();
//                GenreWrapper genreWrapper = response.body();
//                genreMap.clear();
//                for (Genre genre: genreWrapper.getGenres()) {
//                    genreMap.put(genre.getId(), genre.getName());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<GenreWrapper> call, Throwable t) {
//
//            }
//
//        });
        call.enqueue(new Callback<ResultWrapper>() {
            @Override
            public void onResponse(Call<ResultWrapper> call, Response<ResultWrapper> response) {
                int statusCode = response.code();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                final ResultWrapper resultWrapper = response.body();
                movieListView = (ListView) findViewById(R.id.lvItems);
                ArrayAdapter<Movie> adapter;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                    adapter = new MovieItemAdapter(getBaseContext(), R.layout.movie_item_adapter, resultWrapper.getResults());
                } else{
                    adapter = new MovieItemAdapterHorizontal(getBaseContext(), R.layout.movie_item_adapter_horizontal, resultWrapper.getResults());
                }
                movieListView.setAdapter(adapter);
                movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                        intent.putExtra("movieId",resultWrapper.getResults().get(i).getId());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onFailure(Call<ResultWrapper> call, Throwable t) {
                // Log error here since request failed
            }
        });
        final SwipeRefreshLayout swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Gson gson = new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                        .create();
//        RxJavaCallAdapterFactory rxAdapter = RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io());
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(API)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
                MyApiEndpointInterface apiService =
                        retrofit.create(MyApiEndpointInterface.class);

                Call<ResultWrapper> call = apiService.getAllMovie();
                call.enqueue(new Callback<ResultWrapper>() {
                    @Override
                    public void onResponse(Call<ResultWrapper> call, Response<ResultWrapper> response) {
                        int statusCode = response.code();
                        ResultWrapper resultWrapper = response.body();
                        movieListView = (ListView) findViewById(R.id.lvItems);
                        ArrayAdapter<Movie> adapter;
                        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                            adapter = new MovieItemAdapter(getBaseContext(), R.layout.movie_item_adapter, resultWrapper.getResults());
                        } else{
                            adapter = new MovieItemAdapterHorizontal(getBaseContext(), R.layout.movie_item_adapter_horizontal, resultWrapper.getResults());
                        }
                        movieListView.setAdapter(adapter);
                        swipeContainer.setRefreshing(false);
                        Toast toast = Toast.makeText(getBaseContext(), "Updated", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                    @Override
                    public void onFailure(Call<ResultWrapper> call, Throwable t) {
                        // Log error here since request failed
                    }
                });
            }
        });


    }
}

