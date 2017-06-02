package com.onedudedesign.popularmoviess2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


import com.onedudedesign.popularmoviess2.Cupboard.CupboardDbHelper;
import com.onedudedesign.popularmoviess2.Cupboard.MovieFavorite;
import com.onedudedesign.popularmoviess2.Models.MovieDetail;
import com.onedudedesign.popularmoviess2.Models.MovieReviews;
import com.onedudedesign.popularmoviess2.Models.MovieTrailers;
import com.onedudedesign.popularmoviess2.utils.MovieApiService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;


import static com.onedudedesign.popularmoviess2.R.drawable.error;
import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class DetailConstraint extends AppCompatActivity {

    private MovieDetail mDetail = new MovieDetail();
    private String movieID;
    private List<MovieTrailers> mMovieTrailerList;
    private List<MovieReviews> mMovieReviewList;
    private SQLiteDatabase mDB;  //open and close the DB in onResume and onPause to free resources
    private static final int trailer1 = 0;
    private static final int trailer2 = 1;
    private static final int trailer3 = 2;
    private static final int trailer4 = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_constraint);

        getSupportActionBar().setTitle(R.string.detail_activity_title);
        //receiving the Movie ID from the clicked item from MainActivity
        Intent intent = this.getIntent();
        movieID = intent.getStringExtra(getString(R.string.intent_movie_id));

        final String api_key = getResources().getString(R.string.TMDB_API_KEY);

         /* we get the movie ID and make the call directly to the movie information in the API
        because we will need additional information not returned in the Popular and Toprated
        queries */
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.tmdb_api_endpoint))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addPathParam(getString(R.string.tmdb_movie_id_path_param), movieID);
                        request.addEncodedQueryParam(getString(R.string.tmdb_api_key_query_param), api_key);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        MovieApiService service = restAdapter.create(MovieApiService.class);
        //callback to the MovieDetail model, using a single API Service with the get requests
        //will look to combine the adapter and service calls into a class
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

    //populate the detail activity screen
    private void populateData() {

        //check if the movie is favorite and set the checkbox
        checkIfFavorite();

        ImageView backdrop = (ImageView) findViewById(R.id.detailConstraintImageViewBackdrop);
        //using Picasso to deal with image retrieval and caching in background thread
        Picasso.with(this)
                .load(getString(R.string.detail_tmdb_backdrop_path) + mDetail.getMovieBackdrop())
                .placeholder(R.drawable.placeholder) //displays temp image while loading
                .error(error) //displays an error image if the load fails
                .into(backdrop);

        TextView title = (TextView) findViewById(R.id.detailConstraintTextViewMovieTitle);
        title.setText(mDetail.getMovieTitle());


        TextView year = (TextView) findViewById(R.id.detailConstraintYearReleased);
        String base = mDetail.getMovieReleaseDate();
        String yearString = base.substring(0, 4);
        year.setText(yearString);

        TextView runtime = (TextView) findViewById(R.id.detailConstraintRunTime);
        runtime.setText(mDetail.getMovieRunTime() + getString(R.string.detail_act_minute_format));

        TextView rating = (TextView) findViewById(R.id.detailConstraintRating);
        rating.setText(mDetail.getMovieTmdbRating() + getString(R.string.detail_act_rating_format));

        TextView overview = (TextView) findViewById(R.id.detailConstraintSynopsis);
        overview.setText(mDetail.getMovieSynopsis());

        ImageView poster = (ImageView) findViewById(R.id.detailConstraintPosterImage);
        //using Picasso to deal with image retrieval and caching in background thread
        Picasso.with(this)
                .load(getString(R.string.detail_tmdb_image_path) + mDetail.getMoviePoster())
                .placeholder(R.drawable.placeholder) //displays temop image while loading
                .error(error) //displays an error image if the load fails
                .into(poster);

        //fetch the trailer data nd put it into an array
        fetchTrailers();

        //fetch the review data and put into Array
        fetchReviews();


    }
        //TODO:Similar method for doing the reviews do a maximum 4 reviews, trying to keep this clean
        //TODO: add a link to go to TMDB. org for even more info

    private void fetchReviews () {
        //fetching the review info for the selected movie id

        final String api_key = getResources().getString(R.string.TMDB_API_KEY);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.tmdb_api_endpoint_v3))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addPathParam(getString(R.string.tmdb_movie_id_path_param), movieID);
                        request.addEncodedQueryParam(getString(R.string.tmdb_api_key_query_param), api_key);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        MovieApiService reviewService = restAdapter.create(MovieApiService.class);

        reviewService.getMovieReviews(new Callback<MovieReviews.MovieReviewsResult>() {
            @Override
            public void success(MovieReviews.MovieReviewsResult movieReviewsResult, Response response) {
                mMovieReviewList = new ArrayList<>();
                mMovieReviewList.clear();
                mMovieReviewList.addAll(movieReviewsResult.getResults());

                int reviewArraySize = mMovieReviewList.size();

                //confirm array size
                Log.d("Review Array Size", Integer.toString(reviewArraySize));

                if (reviewArraySize > 0) {
                    populateReview1();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void fetchTrailers() {

        //fetching the trailer info from TMDB this is in a different endpoint
        // defined in the MovieAPIService

        final String api_key = getResources().getString(R.string.TMDB_API_KEY);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getString(R.string.tmdb_api_endpoint_v3))
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addPathParam(getString(R.string.tmdb_movie_id_path_param), movieID);
                        request.addEncodedQueryParam(getString(R.string.tmdb_api_key_query_param), api_key);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();


        MovieApiService service = restAdapter.create(MovieApiService.class);


        service.getMovieTrailers(new Callback<MovieTrailers.MovieTrailerResult>() {
            /* in many cases the movie has a few different trailers, in some cases there were
            more than 18 attached in order to keep the interface cleaner decided to only use
            from 1 to 4 trailers instead of a list view otherwise the user has to scroll a
            long way to get to reviews
             */
            @Override
            public void success(MovieTrailers.MovieTrailerResult movieTrailerResult, Response response) {
                mMovieTrailerList = new ArrayList<>();
                mMovieTrailerList.clear();
                mMovieTrailerList.addAll(movieTrailerResult.getResults());

                int arraySize = mMovieTrailerList.size();

                //confirm array size
                Log.d("Array Size", Integer.toString(arraySize));


                if (arraySize > 0) {
                    switch (arraySize) {
                        case 1:
                            populateTrailer1();
                            break;
                        case 2:
                            populateTrailer1();
                            populateTrailer2();
                            break;
                        case 3:
                            populateTrailer1();
                            populateTrailer2();
                            populateTrailer3();
                            break;
                        default:
                            populateTrailer1();
                            populateTrailer2();
                            populateTrailer3();
                            populateTrailer4();
                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void populateReview1() {
        final MovieReviews mr = mMovieReviewList.get(0);
        View v = findViewById(R.id.dcview4);
        v.setVisibility(View.VISIBLE);
        TextView t = (TextView) findViewById(R.id.reviewHeader);
        t.setVisibility(View.VISIBLE);
        TextView review = (TextView) findViewById(R.id.movieReviewTV1);
        review.setVisibility(View.VISIBLE);
        review.setText(mr.getReviewContent());
    }

    private void populateTrailer1() {
        final MovieTrailers mt = mMovieTrailerList.get(trailer1);
        ImageView iVTrailer1 = (ImageView) findViewById(R.id.trailerImageView1);

        Picasso.with(this)
                .load(getString(R.string.detail_yt_trailerimage_httphead) + mt.getYoutubeKey()
                        + getString(R.string.detail_yt_trailerimage_quality))
                .placeholder(R.drawable.placeholder) //displays temop image while loading
                .error(error) //displays an error image if the load fails
                .into(iVTrailer1);
        TextView tVTrailer1 = (TextView) findViewById(R.id.trailerTextview1);
        tVTrailer1.setText(mt.getTrailerName());
        iVTrailer1.setVisibility(View.VISIBLE);
        tVTrailer1.setVisibility(View.VISIBLE);

        tVTrailer1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchYoutubeVideo(mt.getYoutubeKey());
            }
        });
        iVTrailer1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchYoutubeVideo(mt.getYoutubeKey());
            }
        });

    }

    private void populateTrailer2() {

        final MovieTrailers mt = mMovieTrailerList.get(trailer2);
        ImageView iVTrailer2 = (ImageView) findViewById(R.id.trailerImageView2);

        Picasso.with(this)
                .load(getString(R.string.detail_yt_trailerimage_httphead) + mt.getYoutubeKey()
                        + getString(R.string.detail_yt_trailerimage_quality))
                .placeholder(R.drawable.placeholder) //displays temop image while loading
                .error(error) //displays an error image if the load fails
                .into(iVTrailer2);
        TextView tVTrailer2 = (TextView) findViewById(R.id.trailerTextview2);
        tVTrailer2.setText(mt.getTrailerName());
        iVTrailer2.setVisibility(View.VISIBLE);
        tVTrailer2.setVisibility(View.VISIBLE);

        tVTrailer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchYoutubeVideo(mt.getYoutubeKey());
            }
        });
        iVTrailer2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchYoutubeVideo(mt.getYoutubeKey());
            }
        });

    }

    private void populateTrailer3() {

        final MovieTrailers mt = mMovieTrailerList.get(trailer3);
        ImageView iVTrailer3 = (ImageView) findViewById(R.id.trailerImageView3);

        Picasso.with(this)
                .load(getString(R.string.detail_yt_trailerimage_httphead) + mt.getYoutubeKey()
                        + getString(R.string.detail_yt_trailerimage_quality))
                .placeholder(R.drawable.placeholder) //displays temop image while loading
                .error(error) //displays an error image if the load fails
                .into(iVTrailer3);
        TextView tVTrailer3 = (TextView) findViewById(R.id.trailerTextview3);
        tVTrailer3.setText(mt.getTrailerName());
        iVTrailer3.setVisibility(View.VISIBLE);
        tVTrailer3.setVisibility(View.VISIBLE);

        tVTrailer3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchYoutubeVideo(mt.getYoutubeKey());
            }
        });
        iVTrailer3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchYoutubeVideo(mt.getYoutubeKey());
            }
        });

    }

    private void populateTrailer4() {

        final MovieTrailers mt = mMovieTrailerList.get(trailer4);
        ImageView iVTrailer4 = (ImageView) findViewById(R.id.trailerImageView4);

        Picasso.with(this)
                .load(getString(R.string.detail_yt_trailerimage_httphead) + mt.getYoutubeKey()
                        + getString(R.string.detail_yt_trailerimage_quality))
                .placeholder(R.drawable.placeholder) //displays temop image while loading
                .error(error) //displays an error image if the load fails
                .into(iVTrailer4);
        TextView tVTrailer4 = (TextView) findViewById(R.id.trailerTextview4);
        tVTrailer4.setText(mt.getTrailerName());
        iVTrailer4.setVisibility(View.VISIBLE);
        tVTrailer4.setVisibility(View.VISIBLE);
        tVTrailer4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchYoutubeVideo(mt.getYoutubeKey());
            }
        });
        iVTrailer4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                watchYoutubeVideo(mt.getYoutubeKey());
            }
        });

    }

    public void watchYoutubeVideo(String id) {
        //try to launch the Youtube app and if not available launch the trailer in web intent
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Instantiate the favorites database
        CupboardDbHelper dbHelper = new CupboardDbHelper(this);
        mDB = dbHelper.getWritableDatabase();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDB.close();
    }

    public void updateFavoritesDb(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        if (checked) {

            //insert the db record using mDetail info if favorite is checked
            MovieFavorite favorite = new MovieFavorite(movieID,
                    mDetail.getMovieTitle(),
                    mDetail.getMoviePoster(),
                    mDetail.getMovieSynopsis(),
                    mDetail.getMovieTmdbRating(),
                    mDetail.getMovieReleaseDate(),
                    mDetail.getMovieBackdrop(),
                    true);

            long id = cupboard().withDatabase(mDB).put(favorite);

        } else {
            //remove the favorite from the DB if favorite is unchecked
            cupboard().withDatabase(mDB).delete(MovieFavorite.class, "movie_id = ?", movieID);

        }
    }

    //method to check if this movie is in the favorites DB and set the favorite
    // checkbox when the moviedetail loads
    private void checkIfFavorite() {
        MovieFavorite favorite = cupboard().withDatabase(mDB).query(MovieFavorite.class)
                .withSelection("movie_id = ?", movieID).get();
        if (favorite != null) {
            CheckBox cb = (CheckBox) findViewById(R.id.favoriteCheckbox);
            cb.setChecked(true);
        }

    }
}
