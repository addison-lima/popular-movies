package com.addisonlima.popularmovies.models;

import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("key")
    private String mKey;

    @SerializedName("name")
    private String mName;

    @SerializedName("site")
    private String mSite;

    @SerializedName("type")
    private String mType;

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }

    public String getType() {
        return mType;
    }
}
