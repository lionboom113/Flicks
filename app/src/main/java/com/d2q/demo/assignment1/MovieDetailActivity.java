package com.d2q.demo.assignment1;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.Locale;

import Adapter.MovieItemAdapter;
import Adapter.MovieItemAdapterHorizontal;
import Model.Genre;
import Model.Movie;
import Model.MovieDetail;
import Model.MyApiEndpointInterface;
import Model.ResultWrapper;
import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDetailActivity extends Activity {
    private final String API = "https://api.themoviedb.org/3/";
    @BindView(R.id.txtDescriptionDetail)
    TextView tvDescription;

    @BindView(R.id.txtFilmRating)
    TextView tvFilmRating;

    @BindView(R.id.txtBudget)
    TextView tvBudget;

    @BindView(R.id.txtGenre)
    TextView tvGenre;

    @BindView(R.id.txtTotalVote)
    TextView tvTotalVote;

    @BindView(R.id.ivPoster)
    ImageView ivPoster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
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
        Call<MovieDetail> call = apiService.getMovieDetail(getIntent().getIntExtra("movieId",0));
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                int statusCode = response.code();
                final MovieDetail mvDetail = response.body();
                setTitle(mvDetail.getTitle());
                tvBudget.setText(NumberFormat.getNumberInstance(Locale.US).format(mvDetail.getBudget()));
                tvDescription.setText(mvDetail.getOverview());
                tvFilmRating.setText(""+mvDetail.getVote_average());
                tvTotalVote.setText(""+mvDetail.getVote_count() + " vote(s)");
                StringBuilder sb = new StringBuilder("| ");
                for (Genre genre: mvDetail.getGenres()) {
                    sb.append(genre.getName() + " | ");
                }
                tvGenre.setText(sb.toString());
                Picasso.with(getBaseContext()).load("https://image.tmdb.org/t/p/w500"+mvDetail.getPoster_path()).transform(new RoundedCornersTransformation(10, 10)).placeholder(R.drawable.poster_placeholder).into(ivPoster);
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {

            }
        });
    }

}
