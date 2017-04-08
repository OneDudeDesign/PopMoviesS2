package com.onedudedesign.popularmoviess1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.onedudedesign.popularmoviess1.Models.MovieDetail;
import com.onedudedesign.popularmoviess1.utils.MovieApiService;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TMDB_API_KEY = "fd66dfefac539c5f745200aadb175e4d";
    private MovieDetail mDetail = new MovieDetail();
    private String movieID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();
        movieID = intent.getStringExtra("movieID");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.themoviedb.org/3/")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addPathParam("movie_id", movieID);
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
        //add new populate activitiesbased on the new design

        TextView title = (TextView) findViewById(R.id.detailMovieTitle);
        title.setText(mDetail.getMovieTitle());

        TextView year = (TextView) findViewById(R.id.detailYearReleased);
        String base = mDetail.getMovieReleaseDate();
        String yearString = base.substring(0,4);
        year.setText(yearString);

        TextView runtime = (TextView) findViewById(R.id.detailRunTime);
        runtime.setText(mDetail.getMovieRunTime() + " Minutes");

        TextView rating = (TextView) findViewById(R.id.detailRating);
        rating.setText(mDetail.getMovieTmdbRating() + "/10");

        TextView overview = (TextView) findViewById(R.id.detailSynopsis);
        overview.setText(mDetail.getMovieSynopsis());

        ImageView poster = (ImageView) findViewById(R.id.detailPosterImage);

        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w300/" + mDetail.getMoviePoster())
                .placeholder(R.drawable.placeholder) //this is optional the image to display while the url image is downloading
                .error(R.drawable.error)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                .into(poster);
     }
}
