package com.onedudedesign.popularmoviess1.utils;

import com.onedudedesign.popularmoviess1.Movie;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by clucier on 4/6/17.
 */

public interface MovieApiService {


        @GET("/movie/popular")
        void getPopularMovies(Callback<Movie.MovieResult> cb);

        @GET("/movie/top_rated")
        void getTopRatedMovies(Callback<Movie.MovieResult> cb);
    }

