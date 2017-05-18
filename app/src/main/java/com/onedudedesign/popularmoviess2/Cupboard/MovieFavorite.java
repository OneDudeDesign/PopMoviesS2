package com.onedudedesign.popularmoviess2.Cupboard;

import static android.R.attr.name;

/**
 * Created by clucier on 5/17/17.
 * This is the POJO for CUpboard to create the database for the Movie Favorites
 */

public class MovieFavorite {

    public Long _id; // SQL row ID (for Cupboard to instantiate)
    public String movie_id; // TMDB Movie ID
    public String original_title; // original title
    public String poster_path; //poster image path
    public String overview; //movie overview
    public String vote_average; //TMDB user votes average
    public String release_date; //movie release date
    public String backdrop_path; //TMDB backdrop image path
    public Boolean favorite; //Is a user favorite


    public MovieFavorite() {
        this.movie_id = "noID";
        this.original_title = "Wassup nada";
        this.poster_path = "path to nowhere";
        this.overview = "A whole lot of nada";
        this.vote_average = "0.0 x 10 -32";
        this.release_date = "2999";
        this.backdrop_path = "return from nowhere";
        this.favorite = false;
    }

    public MovieFavorite(String mID, String oT, String pPath, String oView, String vAve,
                         String rDate, String bPath, Boolean fav) {
        this.movie_id = mID;
        this.original_title = oT;
        this.poster_path = pPath;
        this.overview = oView;
        this.vote_average = vAve;
        this.release_date = rDate;
        this.backdrop_path = bPath;
        this.favorite = fav;
    }

}
