package com.onedudedesign.popularmoviess1;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.onedudedesign.popularmoviess1.utils.PopularMovieApiService;
import com.onedudedesign.popularmoviess1.utils.TopRatedMovieApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private static final String tmdbApiKey = "fd66dfefac539c5f745200aadb175e4d";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //set the Gridview Layout Manager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        List<Movie> movies = new ArrayList<>();

        mAdapter.setMovieList(movies);

        //check if the network is connected, if not fire intent to network message
        if (isNetworkConnected()) {
            initRetrofitPopular();
        } else {
            noNetwork();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuPopular:
                Toast.makeText(this,"Selected Popular", Toast.LENGTH_LONG).show();
                initRetrofitPopular();
                return true;
            case R.id.menuTopRated:
                Toast.makeText(this,"Selected Top Rated",Toast.LENGTH_LONG).show();
                initRetrofitTopRated();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //The MovieViewHolder Class extending recyyclerview viewholder to hold
    // references to the image views in the layout



    private void initRetrofitPopular() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", "fd66dfefac539c5f745200aadb175e4d");
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        PopularMovieApiService service = restAdapter.create(PopularMovieApiService.class);
        service.getPopularMovies(new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                mAdapter.setMovieList(movieResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    private void initRetrofitTopRated() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", tmdbApiKey);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        TopRatedMovieApiService service = restAdapter.create(TopRatedMovieApiService.class);
        service.getTopRatedMovies(new Callback<Movie.MovieResult>() {
            @Override
            public void success(Movie.MovieResult movieResult, Response response) {
                mAdapter.setMovieList(movieResult.getResults());
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        //the constructor
        public MovieViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.moviePosterImageView);

        }

    }

    //Check for network connection method goes here
    public boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //intent method for no network launches network warning activity
    public void noNetwork() {
        Toast.makeText(this, "No Network", Toast.LENGTH_LONG).show();
        Context context = MainActivity.this;
        Class destinationActivity = NoNetwork.class;
        Intent intent = new Intent(context, destinationActivity);
        startActivity(intent);
    }
}
