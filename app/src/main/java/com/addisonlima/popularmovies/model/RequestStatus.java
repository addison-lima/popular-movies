package com.addisonlima.popularmovies.model;

public class RequestStatus {

    private final SortType mSortType;
    private final RequestState mRequestState;

    public RequestStatus(SortType sortType, RequestState requestState) {
        mSortType = sortType;
        mRequestState = requestState;
    }

    public SortType getSortType() {
        return mSortType;
    }

    public RequestState getRequestState() {
        return mRequestState;
    }

    public enum SortType {
        POPULAR,
        TOP_RATED
    }

    public enum RequestState {
        LOADING,
        SUCCESS,
        FAILURE
    }
}
