package com.addisonlima.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class VideosResponse {

    @SerializedName("results")
    private Video[] mVideos;

    public Video[] getVideos() {
        return mVideos;
    }
}
