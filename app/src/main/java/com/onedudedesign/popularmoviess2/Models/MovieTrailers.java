package com.onedudedesign.popularmoviess2.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by clucier on 5/2/17.
 */

public class MovieTrailers {


    //Setup the data variables
    @SerializedName("name")
    private String trailerName;

    @SerializedName("key")
    private String youtubeKey;

    @SerializedName("site")
    private String siteLocation;

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public void setYoutubeKey(String youtubeKey) {
        this.youtubeKey = youtubeKey;
    }

    public String getSiteLocation() {
        return siteLocation;
    }

    public void setSiteLocation(String siteLocation) {
        this.siteLocation = siteLocation;
    }

    public static class MovieTrailerResult {
        private List<MovieTrailers> results;

        public List<MovieTrailers> getResults() {
            return results;
        }
    }

}
