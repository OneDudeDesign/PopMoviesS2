package com.onedudedesign.popularmoviess1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.onedudedesign.popularmoviess1.Models.MovieDetail;
import com.onedudedesign.popularmoviess1.utils.MovieApiService;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailActivity extends AppCompatActivity {

    private static final String TMDB_API_KEY = "fd66dfefac539c5f745200aadb175e4d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

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

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
            }
        });
    }
}
