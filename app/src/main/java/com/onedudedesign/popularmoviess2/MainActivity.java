package com.onedudedesign.popularmoviess2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

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


//Extending ListItemClick listener to handle clicks in the Grid for going to movie detail for both
//adapters
public class MainActivity extends AppCompatActivity
        implements MovieAdapter.ListItemClickListener,
        FavoriteMovieCursorAdapterRV.ListItemClickListener {

    private RecyclerView mRecyclerView;
    private MovieAdapter mAdapter;
    private FavoriteMovieCursorAdapterRV mFavoriteAdapter;
    private ArrayList<Movie> mMovieList;
    private static final int POPULAR = 0;
    private static final int TOP_RATED = 1;
    private static final int FAVORITE = 2;
    private static final int gLayoutNumCol = 2;
    private static final int rVReset = 0;
    private int mSortOrder;
    public static final String SORT_ORDER = "SortOrder";
    public static final String SAVED_RECYCLER_VIEW_STATUS_ID = "recycleViewState";
    public static final String SAVED_RECYCLER_VIEW_DATASET_ID = "movieArray";

    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        //set the Gridview Layout Manager with 2 columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, gLayoutNumCol);

        //Set the gridlayout manager and connect it with the adapter
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        //TODO WHEN RESTORING THE STATE< RESTORE THE DATA TOO IE put the Arraylist into the bundle
        //this is why you are seing a blank screen.....


        /* make the call to retrofit method to get query the TMDB api unless there is no network
        then display a message that the network is disconnected.
        Using the Static POPULAR int type to force the loading of Popular movies endpoint
        by default (used also in the case statement on the spinner to toggle between endpoints.
         Fetch the sort order preference from the shared preferences file to make the call with the
         users preferred sort order*/

        mSortOrder = getSharedPreferenceSortOrder();

        if (isNetworkConnected()) {
            if (mSortOrder == FAVORITE) {
                //even if network is connectsed if the saved sort order is FAVORITE then set the
                //correct adapter
                mFavoriteAdapter = new FavoriteMovieCursorAdapterRV(this, this);
                mRecyclerView.setAdapter(mFavoriteAdapter);
                setActivityTitle();

            } else {
                //call the data load if the sort order is not FAVORITE
                setActivityTitle();
                initRetrofit(mSortOrder);
            }
        } else {
            //if the network is off set the sort order to favorite and load the favorites list
            //the details will still not load but this forces a default to the favorites list
            // while off network, later feature will be to load the favorite details when clicked
            //but this is not yet implemented
            mSortOrder = FAVORITE;
            setActivityTitle();
            mFavoriteAdapter = new FavoriteMovieCursorAdapterRV(this, this);
            mRecyclerView.setAdapter(mFavoriteAdapter);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        // putting recyclerview position
        outState.putParcelable(SAVED_RECYCLER_VIEW_STATUS_ID, listState);
        // putting recyclerview items
        //outState.putParcelableArrayList(SAVED_RECYCLER_VIEW_DATASET_ID, mMovieList);
        outState.putSerializable(SAVED_RECYCLER_VIEW_DATASET_ID, mMovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //checking for the network connection in the options menu selection so that the Favorites
        //show if the network state changed preventing a bad screen with no data
        switch (item.getItemId()) {
            case R.id.menuPopular:
                if (isNetworkConnected()) {
                    Toast.makeText(this, R.string.pop_movies_loaded_toast, Toast.LENGTH_LONG).show();
                    //set the shared preferences to POPULAR and refresh the grid
                    setSharedPreferenceSortOrder(POPULAR);
                    mSortOrder = POPULAR;
                    initRetrofit(mSortOrder);
                    mRecyclerView.scrollToPosition(rVReset);
                    setActivityTitle();
                    return true;
                } else {
                    Toast.makeText(this, R.string.favorites_no_network, Toast.LENGTH_LONG).show();
                    //set the shared preferences to FAVORITE and refresh the grid
                    setSharedPreferenceSortOrder(FAVORITE);
                    mSortOrder = FAVORITE;
                    mFavoriteAdapter = new FavoriteMovieCursorAdapterRV(this, this);
                    mRecyclerView.setAdapter(mFavoriteAdapter);
                    mRecyclerView.scrollToPosition(rVReset);
                    setActivityTitle();
                    return true;
                }

            case R.id.menuTopRated:
                if (isNetworkConnected()) {
                    Toast.makeText(this, R.string.top_movies_loaded_toast, Toast.LENGTH_LONG).show();
                    //set the Shared Preferences to TOP_RATED and refresh the grid
                    setSharedPreferenceSortOrder(TOP_RATED);
                    mSortOrder = TOP_RATED;
                    initRetrofit(mSortOrder);
                    //used to reset the grid back to the top otherwise it loads and displays where
                    //the view was currently scrolled and maybe confusing
                    mRecyclerView.scrollToPosition(rVReset);
                    setActivityTitle();
                    return true;
                } else {
                    Toast.makeText(this, R.string.favorites_no_network, Toast.LENGTH_LONG).show();
                    //set the shared preferences to FAVORITE and refresh the grid
                    setSharedPreferenceSortOrder(FAVORITE);
                    mSortOrder = FAVORITE;
                    mFavoriteAdapter = new FavoriteMovieCursorAdapterRV(this, this);
                    mRecyclerView.setAdapter(mFavoriteAdapter);
                    mRecyclerView.scrollToPosition(rVReset);
                    setActivityTitle();
                    return true;
                }
            case R.id.menuFavorites:
                Toast.makeText(this, R.string.favoriteMoviesLoadedToast, Toast.LENGTH_LONG).show();
                //set the shared preferences to FAVORITE and refresh the grid
                setSharedPreferenceSortOrder(FAVORITE);
                mSortOrder = FAVORITE;
                mFavoriteAdapter = new FavoriteMovieCursorAdapterRV(this, this);
                mRecyclerView.setAdapter(mFavoriteAdapter);
                mRecyclerView.scrollToPosition(rVReset);
                setActivityTitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //receive callback from clicks in the Grid
    @Override
    public void onListItemClick(int clickedItemIndex, String movieid) {


        //fire the intent to the detailed activity passing the movie ID in EXTRA
        if (isNetworkConnected()) {
            Intent intent = new Intent(this, DetailConstraint.class);
            intent.putExtra(getString(R.string.intent_movie_id), String.valueOf(movieid));
            startActivity(intent);
        } else {
            //if the network is off warn the user need the network to get details
            noNetwork();
        }
    }

    private void initRetrofit(int key) {

        /* using squareup's retrofit to simplify the fetching and parsing of the JSON
        using the Callback method allows cleaner code as it handles the background threading
        keeping the networrk calls off the main thread without having to code in Async tasks

        pull the api key from the String resource file keeping it there as it is used in
        multiple places and needs to be pulled out (legally) when sharing the code and replaced
        by the reviewers own API key or permission to use a key if the app where to be deployed
        commercially needs to be obtained */

        final String api_key = BuildConfig.TMDB_API_KEY;

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
        select which service call to make depending on the type of movies the user wants to see
        from the settings menu selection (favorites goes to local db so no retrofit call) */

        if (key == TOP_RATED) {
            service.getTopRatedMovies(new Callback<Movie.MovieResult>() {
                @Override
                public void success(Movie.MovieResult movieResult, Response response) {
                    //need to change the adptar since we have both the list and cursor for the db
                    mRecyclerView.setAdapter(mAdapter);
                    mMovieList = movieResult.getResults();
                    mAdapter.setMovieList(mMovieList);
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
                    //need to change the adaptar since we have both the list and cursor for the db
                    mRecyclerView.setAdapter(mAdapter);
                    mMovieList = movieResult.getResults();
                    mAdapter.setMovieList(mMovieList);
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


    //Set shared preferences method to save the sort order
    public void setSharedPreferenceSortOrder(int sortOrder) {
        SharedPreferences.Editor editor = getSharedPreferences(SORT_ORDER, MODE_PRIVATE).edit();
        editor.putInt("sortOrder", sortOrder);
        editor.apply();
    }

    //getter for the shared preferences for sort order
    public int getSharedPreferenceSortOrder() {
        SharedPreferences prefs = getSharedPreferences(SORT_ORDER, MODE_PRIVATE);
        int restoredSortOrder = prefs.getInt(getString(R.string.sortOrderSharedPrefKey),
                POPULAR);
        if (restoredSortOrder != POPULAR) {
            int sortOrder = prefs.getInt
                    (getString(R.string.sortOrderSharedPrefKey), POPULAR);
            //POPULAR is the default value if something fails.
            return sortOrder;
        } else return POPULAR;
    }

    private void setActivityTitle() {
        switch (mSortOrder) {
            case FAVORITE:
                this.setTitle(getString(R.string.favoritesActivityTitle));
                return;
            case TOP_RATED:
                this.setTitle(getString(R.string.topRatedActivityTitle));
                return;
            case POPULAR:
                this.setTitle(getString(R.string.popularActivityTitle));
                return;
            default:
                return;
        }
    }


}
