package com.onedudedesign.popularmoviess1.utils;

import com.onedudedesign.popularmoviess1.Movie;

import retrofit.Callback;
import retrofit.http.GET;


/**
 * Created by clucier on 4/5/17.
 */

public interface PopularMovieApiService {
    @GET("/movie/popular")
    void getPopularMovies(Callback<Movie.MovieResult> cb);
}
