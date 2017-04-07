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
import android.widget.Toast;

import com.onedudedesign.popularmoviess1.Models.Movie;
import com.onedudedesign.popularmoviess1.utils.MovieAdapter;
import com.onedudedesign.popularmoviess1.utils.MovieApiService;
import com.onedudedesign.popularmoviess1.utils.NoNetwork;


import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener{

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private static final String TMDB_API_KEY = "fd66dfefac539c5f745200aadb175e4d";
    private static final int POPULAR = 0;
    private static final int TOP_RATED = 1;

    /*
     * If we hold a reference to our Toast, we can cancel it (if it's showing)
     * to display a new Toast. If we didn't do this, Toasts would be delayed
     * in showing up if you clicked many list items in quick succession.
     */
    private Toast mToast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //set the Gridview Layout Manager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MovieAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);

        List<Movie> movies = new ArrayList<>();

        mAdapter.setMovieList(movies);

        //check if the network is connected, if not fire intent to network message
        if (isNetworkConnected()) {
            initRetrofit(POPULAR);
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
                initRetrofit(POPULAR);
                return true;
            case R.id.menuTopRated:
                Toast.makeText(this,"Selected Top Rated",Toast.LENGTH_LONG).show();
                initRetrofit(TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * This is where we receive our callback from
     * {@link MovieAdapter.ListItemClickListener}
     *
     * This callback is invoked when you click on an item in the list.
     *
     * @param clickedItemIndex Index in the list of the item that was clicked.
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        /*
         * Even if a Toast isn't showing, it's okay to cancel it. Doing so
         * ensures that our new Toast will show immediately, rather than
         * being delayed while other pending Toasts are shown.
         *
         * Comment out these three lines, run the app, and click on a bunch of
         * different items if you're not sure what I'm talking about.
         */
        if (mToast != null) {
            mToast.cancel();
        }

        /*
         * Create a Toast and store it in our Toast field.
         * The Toast that shows up will have a message similar to the following:
         *
         *                     Item #42 clicked.
         */
        String s = mAdapter.fetchMovieTitle(clickedItemIndex);
        int i = mAdapter.fetchMovieID(clickedItemIndex);

        String toastMessage = "Item #" + clickedItemIndex + " clicked. " + "Name: " + s + "TMDB.org ID: " + String.valueOf(i);
        mToast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);

        mToast.show();

        //Intent to fire the detailed activity, need to figure out how to send the id later :)
        Intent intent = new Intent(this, DetailActivity.class);
        startActivity(intent);
    }

    private void initRetrofit (int key) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam("api_key", TMDB_API_KEY);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();



        MovieApiService service = restAdapter.create(MovieApiService.class);

        if (key == 1) {
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
        } else {
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
