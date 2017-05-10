package com.onedudedesign.popularmoviess2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.onedudedesign.popularmoviess2.Models.Movie;
import com.onedudedesign.popularmoviess2.Models.MovieDetail;
import com.onedudedesign.popularmoviess2.Models.MovieTrailers;
import com.onedudedesign.popularmoviess2.utils.MovieApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.R.attr.key;
import static android.R.attr.privateImeOptions;

public class DetailConstraint extends AppCompatActivity {

    private MovieDetail mDetail = new MovieDetail();
    private String movieID;
    private List<MovieTrailers> mMovieTrailerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_constraint);

        getSupportActionBar().setTitle(R.string.detail_activity_title);
        //receiving the Movie ID from the clicked item from MainActivity
        Intent intent = this.getIntent();
        movieID = intent.getStringExtra(getString(R.string.intent_movie_id));

        final String api_key = getResources().getString(R.string.TMDB_API_KEY);

         /* we get the movie ID and make the call directly to the movie information in the API
        because we will need additional information not returned in the Popular and Toprated
        queries */
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.tmdb_api_endpoint))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addPathParam(getString(R.string.tmdb_movie_id_path_param), movieID);
                        request.addEncodedQueryParam(getString(R.string.tmdb_api_key_query_param), api_key);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        MovieApiService service = restAdapter.create(MovieApiService.class);
        //callback to the MovieDetail model, using a single API Service with the get requests
        //will look to combine the adapter and service calls into a class
        service.getMovie(new Callback<MovieDetail>() {
            @Override
            public void success(MovieDetail movieDetail, Response response) {
                mDetail = movieDetail;
                populateData();
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }

    //populate the detail activity screen
    private void populateData () {

        ImageView backdrop = (ImageView) findViewById(R.id.detailConstraintImageViewBackdrop);
        //using Picasso to deal with image retrieval and caching in background thread
        Picasso.with(this)
                .load(getString(R.string.detail_tmdb_backdrop_path) + mDetail.getMovieBackdrop())
                .placeholder(R.drawable.placeholder) //displays temop image while loading
                .error(R.drawable.error) //displays an error image if the load fails
                .into(backdrop);

        TextView title = (TextView) findViewById(R.id.detailConstraintTextViewMovieTitle);
        title.setText(mDetail.getMovieTitle());


        TextView year = (TextView) findViewById(R.id.detailConstraintYearReleased);
        String base = mDetail.getMovieReleaseDate();
        String yearString = base.substring(0,4);
        year.setText(yearString);

        TextView runtime = (TextView) findViewById(R.id.detailConstraintRunTime);
        runtime.setText(mDetail.getMovieRunTime() + getString(R.string.detail_act_minute_format));

        TextView rating = (TextView) findViewById(R.id.detailConstraintRating);
        rating.setText(mDetail.getMovieTmdbRating() + getString(R.string.detail_act_rating_format));

        TextView overview = (TextView) findViewById(R.id.detailConstraintSynopsis);
        overview.setText(mDetail.getMovieSynopsis());

        ImageView poster = (ImageView) findViewById(R.id.detailConstraintPosterImage);
        //using Picasso to deal with image retrieval and caching in background thread
        Picasso.with(this)
                .load(getString(R.string.detail_tmdb_image_path) + mDetail.getMoviePoster())
                .placeholder(R.drawable.placeholder) //displays temop image while loading
                .error(R.drawable.error) //displays an error image if the load fails
                .into(poster);

        fetchTrailers();

        //try getting data from the arraylist





    }

    private void fetchTrailers () {

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
                        request.addPathParam(getString(R.string.tmdb_movie_id_path_param), movieID);
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

        service.getMovieTrailers(new Callback<MovieTrailers.MovieTrailerResult>() {
            @Override
            public void success(MovieTrailers.MovieTrailerResult movieTrailerResult, Response response) {
                mMovieTrailerList = new ArrayList<>();
                mMovieTrailerList.clear();
                mMovieTrailerList.addAll(movieTrailerResult.getResults());

                for (int i = 0; i < mMovieTrailerList.size(); i++) {
                    MovieTrailers mt = mMovieTrailerList.get(i);
                    Log.d("Trailer Name..  ",mt.getTrailerName());

                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }


}
