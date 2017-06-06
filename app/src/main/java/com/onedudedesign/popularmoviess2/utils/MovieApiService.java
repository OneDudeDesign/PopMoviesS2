package com.onedudedesign.popularmoviess2.utils;

import com.onedudedesign.popularmoviess2.Models.Movie;
import com.onedudedesign.popularmoviess2.Models.MovieDetail;
import com.onedudedesign.popularmoviess2.Models.MovieReviews;
import com.onedudedesign.popularmoviess2.Models.MovieTrailers;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by clucier on 4/6/17.
 * This file is to reference the various callbacks used by Retrofit for the different JSON datasets
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

        //GET statement to fetch movie trailers note use of Path_Param requires its name in braces
        @GET("/movie/{movie_id}/videos")
        void getMovieTrailers(Callback<MovieTrailers.MovieTrailerResult> cb);

        //GET statement to fetch movie reviews note use of Path_Param requires its name in braces
        @GET("/movie/{movie_id}/reviews")
        void getMovieReviews(Callback<MovieReviews.MovieReviewsResult> cb);
    }

