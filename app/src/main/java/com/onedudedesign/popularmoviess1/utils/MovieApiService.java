package com.onedudedesign.popularmoviess1.utils;

import com.onedudedesign.popularmoviess1.Models.Movie;
import com.onedudedesign.popularmoviess1.Models.MovieDetail;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by clucier on 4/6/17.
 */

public interface MovieApiService {

        //GET statement to fetch popular movies
        @GET("/movie/popular")
        void getPopularMovies(Callback<Movie.MovieResult> cb);

        //GET statement to fetch top rated movies
        @GET("/movie/top_rated")
        void getTopRatedMovies(Callback<Movie.MovieResult> cb);

        //GET statement to fetch movie details note use of Path_Param requires its name in braces
        @GET("/movie/{movie_id}")
        void getMovie(Callback<MovieDetail> cb);
    }

