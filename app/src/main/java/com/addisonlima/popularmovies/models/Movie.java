package com.addisonlima.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Movie implements Parcelable {

    @SerializedName("id")
    private String mId;

    @SerializedName("title")
    private String mTitle;

    @SerializedName("original_title")
    private String mOriginalTitle;

    @SerializedName("poster_path")
    private String mPosterPath;

    @SerializedName("overview")
    private String mOverview;

    @SerializedName("vote_average")
    private String mVoteAverage;

    @SerializedName("release_date")
    private String mReleaseDate;

    protected Movie(Parcel in) {
        mId = in.readString();
        mTitle = in.readString();
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readString();
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getPosterPath() {
        return "http://image.tmdb.org/t/p/w500" + mPosterPath;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mTitle);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mOverview);
        parcel.writeString(mVoteAverage);
        parcel.writeString(mReleaseDate);
    }
}
