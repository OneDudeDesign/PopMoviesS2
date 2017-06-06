package com.onedudedesign.popularmoviess2.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by clucier on 4/4/17.
 * This is the class to define a Movie Object to be used by the Adapter
 */

public class Movie {

    //note that if I put this in strings file I get an error. investigate.
    public static final String TMDB_IMAGE_PATH = "http://image.tmdb.org/t/p/w500";

    //Setup the data variables
    @SerializedName("title")
    private String movieTitle;

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

    @SerializedName("id")
    private int movieID;

    //method to get the movie title
    public String getMovieTitle() {
        return movieTitle;
    }

    //method to set the title of the movie
    public void setMovieTitle(String title) {
        this.movieTitle = title;
    }

    //method to get the string location of the movie poster
    public String getMoviePoster() {
        return TMDB_IMAGE_PATH + moviePoster;

    }

    //method to set the poster string
    public void setMoviePoster(String poster) {
        this.moviePoster = poster;
    }

    //method to get the movie synopsis
    public String getMovieSynopsis() {
        return movieSynopsis;
    }

    //method to set the movie synopsis
    public void setMovieSynopsis(String synopsis) {
        this.movieSynopsis = synopsis;
    }

    //method to set the rating from TMDB.org
    public String getMovieTmdbRating() {
        return movieTmdbRating;
    }

    //method to get the TMDB.org rating
    public void setMovieTmdbRating(String rating) {
        this.movieTmdbRating = rating;
    }

    //method to get the release date (in string ay need to look at format???
    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    //method to set the release date from the TMDB.org DB
    public void setMovieReleaseDate(String releaseDate) {
        this.movieReleaseDate = releaseDate;
    }

    //method to get the backdrop image
    public String getMovieBackdrop() {
        return TMDB_IMAGE_PATH + movieBackdrop;
    }

    //method to set the movie backdrop
    public void setMovieBackdrop(String movieBackdrop) {
        this.movieBackdrop = movieBackdrop;
    }

    //method to get the movie id
    public int getMovieID() {
        return movieID;
    }

    //method to set the movie id
    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    //results set
    public static class MovieResult {
        private List<Movie> results;

        public List<Movie> getResults() {
            return results;
        }
    }

}
