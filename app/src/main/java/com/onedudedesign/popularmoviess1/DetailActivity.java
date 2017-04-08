package com.onedudedesign.popularmoviess1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.onedudedesign.popularmoviess1.Models.MovieDetail;
import com.onedudedesign.popularmoviess1.utils.MovieApiService;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity {

    private MovieDetail mDetail = new MovieDetail();
    private String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

        TextView title = (TextView) findViewById(R.id.detailMovieTitle);
        title.setText(mDetail.getMovieTitle());

        TextView year = (TextView) findViewById(R.id.detailYearReleased);
        String base = mDetail.getMovieReleaseDate();
        String yearString = base.substring(0,4);
        year.setText(yearString);

        TextView runtime = (TextView) findViewById(R.id.detailRunTime);
        runtime.setText(mDetail.getMovieRunTime() + getString(R.string.detail_act_minute_format));

        TextView rating = (TextView) findViewById(R.id.detailRating);
        rating.setText(mDetail.getMovieTmdbRating() + getString(R.string.detail_act_rating_format));

        TextView overview = (TextView) findViewById(R.id.detailSynopsis);
        overview.setText(mDetail.getMovieSynopsis());

        ImageView poster = (ImageView) findViewById(R.id.detailPosterImage);
        //using Picasso to deal with image retrieval and caching in background thread
        Picasso.with(this)
                .load(getString(R.string.detail_tmdb_image_path) + mDetail.getMoviePoster())
                .placeholder(R.drawable.placeholder) //displays temop image while loading
                .error(R.drawable.error) //displays an error image if the load fails
                .into(poster);
     }
}
