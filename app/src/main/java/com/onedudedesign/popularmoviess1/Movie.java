package com.onedudedesign.popularmoviess1;

/**
 * Created by clucier on 4/4/17.
 * This is the class to define a Movie Object to be used by the Adapter
 */

public class Movie {
    //Setup the data variables
    private String movieTitle;
    private String moviePoster;
    private String movieSynopsis;
    private String movieTmdbRating;
    private String movieReleaseDate;

    //method to get the movie title
    public String getMovieTitle () {
        return movieTitle;
    }

    //method to set the title of the movie
    public void setMovieTitle (String title) {
        this.movieTitle = title;
    }

    //method to get the string location of the movie poster
    public String getMoviePoster () {
        return "http://t2.gstatic.com/images?q=tbn:ANd9GcQW3LbpT94mtUG1PZIIzJNxmFX399wr_NcvoppJ82k7z99Hx6in";
        //todo: fix this later but for now return an http string to set an image
        //return moviePoster;
    }

    //method to set the poster string
    public void setMoviePoster (String poster) {
        this.moviePoster = poster;
    }

    //method to get the movie synopsis
    public String getMovieSynopsis () {
        return movieSynopsis;
    }

    //method to set the movie synopsis
    public void setMovieSynopsis (String synopsis) {
        this.movieSynopsis = synopsis;
    }

    //method to set the rating from TMDB.org
    public String getMovieTmdbRating () {
        return movieTmdbRating;
    }

    //method to get the TMDB.org rating
    public void setMovieTmdbRating (String rating) {
        this.movieTmdbRating = rating;
    }

    //method to get the release date (in string ay need to look at format???
    public String getMovieReleaseDate () {
        return movieReleaseDate;
    }

    //method to set the release date from the TMDB.org DB
    public void setMovieReleaseDate (String releaseDate) {
        this.movieReleaseDate = releaseDate;
    }

}
