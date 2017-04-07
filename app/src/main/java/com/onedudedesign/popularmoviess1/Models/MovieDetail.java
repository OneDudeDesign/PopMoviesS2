package com.onedudedesign.popularmoviess1.Models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by clucier on 4/7/17.
 */

public class MovieDetail {

    public static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w500";

    //Setup the data variables
    @SerializedName("title")
    private String movieTitle;

    @SerializedName("id")
    private int movieID;

    @SerializedName("poster_path")
    private String moviePoster;

    @SerializedName("overview")
    private String movieSynopsis;

    @SerializedName("vote_average")
    private String movieTmdbRating;

    @SerializedName("release_date")
    private String movieReleaseDate;

    @SerializedName("backdrop_path")
    private String movieBackdrop;

    @SerializedName("runtime")
    private String movieRunTime;

    public String getMovieTitle() {
        return movieTitle;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getMovieSynopsis() {
        return movieSynopsis;
    }

    public void setMovieSynopsis(String movieSynopsis) {
        this.movieSynopsis = movieSynopsis;
    }

    public String getMovieTmdbRating() {
        return movieTmdbRating;
    }

    public void setMovieTmdbRating(String movieTmdbRating) {
        this.movieTmdbRating = movieTmdbRating;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public void setMovieReleaseDate(String movieReleaseDate) {
        this.movieReleaseDate = movieReleaseDate;
    }

    public String getMovieBackdrop() {
        return movieBackdrop;
    }

    public void setMovieBackdrop(String movieBackdrop) {
        this.movieBackdrop = movieBackdrop;
    }

    public String getMovieRunTime() {
        return movieRunTime;
    }

    public void setMovieRunTime(String movieRunTime) {
        this.movieRunTime = movieRunTime;
    }
}
