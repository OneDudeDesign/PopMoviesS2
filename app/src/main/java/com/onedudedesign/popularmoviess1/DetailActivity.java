package com.onedudedesign.popularmoviess1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.onedudedesign.popularmoviess1.Models.MovieDetail;
import com.onedudedesign.popularmoviess1.utils.MovieApiService;

import org.w3c.dom.Text;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TMDB_API_KEY = "fd66dfefac539c5f745200aadb175e4d";
    MovieDetail mDetail = new MovieDetail();
    private String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();
        movieID = intent.getStringExtra("movieID");

        //Test to see if I get the correct ID from the intent
        TextView sentID = (TextView) findViewById(R.id.movieIDSent);
        sentID.setText("Movie ID Sent : " + movieID);

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

    private void populateData () {
        TextView title = (TextView) findViewById(R.id.movieTitle);
        title.setText("Movie Title : " + mDetail.getMovieTitle());

        TextView id = (TextView) findViewById(R.id.movieID);
        id.setText("Movie ID : " + mDetail.getMovieID());

        TextView poster = (TextView) findViewById(R.id.moviePosterPath);
        poster.setText("Movie Poster Path : " + mDetail.getMoviePoster());

        TextView overview = (TextView) findViewById(R.id.movieOverview);
        overview.setText("Movie Overview : " + mDetail.getMovieSynopsis());

        TextView vote = (TextView) findViewById(R.id.movieVoteAverage);
        vote.setText("Movie Vote Average : " + mDetail.getMovieTmdbRating());

        TextView release = (TextView) findViewById(R.id.movieReleaseDate);
        release.setText("Movie Release Date : " + mDetail.getMovieReleaseDate());

        TextView runtime = (TextView) findViewById(R.id.movieRuntime);
        runtime.setText("Movie Runtime : " + mDetail.getMovieRunTime());
    }
}
