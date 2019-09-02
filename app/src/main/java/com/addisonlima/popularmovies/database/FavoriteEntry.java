package com.addisonlima.popularmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "task")
public class FavoriteEntry {

    @PrimaryKey
    private String id;
    private String title;
    @ColumnInfo(name = "original_title")
    private String originalTitle;
    @ColumnInfo(name = "poster_path")
    private String posterPath;
    private String overview;
    @ColumnInfo(name = "vote_average")
    private String voteAverage;
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    public FavoriteEntry(String id, String title, String originalTitle, String posterPath,
                         String overview, String voteAverage, String releaseDate) {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.voteAverage = voteAverage;
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return this.overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return this.voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
