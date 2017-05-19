package com.onedudedesign.popularmoviess2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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

import com.onedudedesign.popularmoviess2.Cupboard.CupboardDbHelper;
import com.onedudedesign.popularmoviess2.Models.Movie;
import com.onedudedesign.popularmoviess2.utils.FavoriteMovieCursorAdapterRV;
import com.onedudedesign.popularmoviess2.utils.MovieAdapter;
import com.onedudedesign.popularmoviess2.utils.MovieApiService;
import com.onedudedesign.popularmoviess2.utils.NoNetwork;


import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.R.id.message;

//Extending ListItemClick listener to handle clicks in the Grid for going to movie detail
public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private FavoriteMovieCursorAdapterRV mFavoriteAdapter;
    private static final int POPULAR = 0;
    private static final int TOP_RATED = 1;
    private static final int FAVORITE = 2;
    private int mSortOrder;
    public static final String SORT_ORDER = "SortOrder";
    public static final int sortThrowAway = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //set the Gridview Layout Manager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);

        //Set the gridlayout manager and connect it with the adapter
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        //create the Movie list for the main screen and set it on the adapter
        List<Movie> movies = new ArrayList<>();
        mAdapter.setMovieList(movies);

        /* make the call to retrofit method to get query the TMDB api unless there is no network
        then display a message that the network is disconnected.
        Using the Static POPULAR int type to force the loading of Popular movies endpoint
        by default (used also in the case statement on the spinner to toggle between endpoints.
         Fetch the sort order preference from the shared preferences file to make the call with the
         users preferred sort order*/

        mSortOrder = getSharedPreferenceSortOrder();

        if (isNetworkConnected()) {
            if (mSortOrder == FAVORITE) {
                mFavoriteAdapter = new FavoriteMovieCursorAdapterRV(this);
                mRecyclerView.setAdapter(mFavoriteAdapter);

            } else {
                initRetrofit(mSortOrder);
            }
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
                Toast.makeText(this, R.string.pop_movies_loaded_toast, Toast.LENGTH_LONG).show();
                //set the shared preferences to POPULAR and refresh the grid
                setSharedPreferenceSortOrder(POPULAR);
                mSortOrder = POPULAR;
                initRetrofit(mSortOrder);
                mRecyclerView.scrollToPosition(0);
                return true;
            case R.id.menuTopRated:
                Toast.makeText(this, R.string.top_movies_loaded_toast, Toast.LENGTH_LONG).show();
                //set the Shared Preferences to TOP_RATED and refresh the grid
                setSharedPreferenceSortOrder(TOP_RATED);
                mSortOrder = TOP_RATED;
                initRetrofit(mSortOrder);
                //used to reset the grid back to the top otherwise it loads and displays where
                //the view was currently scrolled and maybe confusing
                mRecyclerView.scrollToPosition(0);
                return true;
            case R.id.menuFavorites:
                Toast.makeText(this, "Favorites Loaded", Toast.LENGTH_LONG).show();
                //set the shared preferences to FAVORITE and refresh the grid
                setSharedPreferenceSortOrder(FAVORITE);
                mSortOrder = FAVORITE;
                mFavoriteAdapter = new FavoriteMovieCursorAdapterRV(this);
                mRecyclerView.setAdapter(mFavoriteAdapter);
                mRecyclerView.scrollToPosition(0);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //receive callback from clicks in the Grid
    @Override
    public void onListItemClick(int clickedItemIndex) {

        //store the TMDB ID of the movie clicked
        int id = mAdapter.fetchMovieID(clickedItemIndex);

        //fire the intent to the detailed activity passing the movie ID in EXTRA
        Intent intent = new Intent(this, DetailConstraint.class);
        intent.putExtra(getString(R.string.intent_movie_id), String.valueOf(id));
        startActivity(intent);
    }

    private void initRetrofit(int key) {

        /* using squareup's retrofit to simplify the fetching and parsing of the JSON
        using the Callback method allows cleaner code as it handles the background threading
        keeping the networrk calls off the main thread without having to code in Async tasks

        pull the api key from the String resource file keeping it there as it is used in
        multiple places and needs to be pulled out (legally) when sharing the code and replaced
        by the reviewers own API key or permission to use a key if the app where to be deployed
        commercially needs to be obtained */

        final String api_key = getResources().getString(R.string.TMDB_API_KEY);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.tmdb_api_endpoint_v3))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addEncodedQueryParam(getString(R.string.tmdb_api_key_query_param), api_key);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();


        MovieApiService service = restAdapter.create(MovieApiService.class);

        /* using the passed in key that comes from the static POPULAR and TOPRATED variables to
        select which service call to make depending on the typoe of movies the user wants to see
        from the settings menu selection (look to clean this up later with a class for the service
        call in Stage 2 when i understand more regarding Retrofit
        (also look at going to v2 from 1.9) */

        if (key == 1) {
            service.getTopRatedMovies(new Callback<Movie.MovieResult>() {
                @Override
                public void success(Movie.MovieResult movieResult, Response response) {
                    //need to change the adptar since we have both the list and cursor for the db
                    mRecyclerView.setAdapter(mAdapter);
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
                    //need to change the adptar since we have both the list and cursor for the db
                    mRecyclerView.setAdapter(mAdapter);
                    mAdapter.setMovieList(movieResult.getResults());
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                }
            });
        }
    }


    //Check for network connection and warn the user if not available
    public boolean isNetworkConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    //intent method for no network launches network warning activity
    public void noNetwork() {
        Context context = MainActivity.this;
        Class destinationActivity = NoNetwork.class;
        Intent intent = new Intent(context, destinationActivity);
        startActivity(intent);
    }

    //bundle the sort order into the saved instance state
    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putInt("sortOrder", mSortOrder);
        super.onSaveInstanceState(outState);
    }

    //Set shared preferences method to save the sort order
    public void setSharedPreferenceSortOrder(int sortOrder) {
        SharedPreferences.Editor editor = getSharedPreferences(SORT_ORDER, MODE_PRIVATE).edit();
        editor.putInt("sortOrder", sortOrder);
        editor.commit();
    }

    //getter for the shared preferences for sort order
    public int getSharedPreferenceSortOrder() {
        SharedPreferences prefs = getSharedPreferences(SORT_ORDER, MODE_PRIVATE);
        int restoredSortOrder = prefs.getInt("sortOrder", sortThrowAway);
        if (restoredSortOrder != sortThrowAway) {
            int sortOrder = prefs.getInt("sortOrder", POPULAR); //POPULAR is the default value.
            return sortOrder;
        } else return POPULAR;
    }
}
