package com.addisonlima.popularmovies.repository;

import android.arch.lifecycle.LiveData;

import com.addisonlima.popularmovies.BuildConfig;
import com.addisonlima.popularmovies.model.Game;
import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory;
import com.github.leonardoxh.livedatacalladapter.Resource;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class TMDbRepository {

    private static final String TMDB_END_POINT = "https://api-v3.igdb.com/";
    private static final String TMDB_API_KEY_PARAM_NAME = "user-key";
    private static final String TMDB_API_KEY_VALUE = BuildConfig.IGDB_API_KEY;

    private static final Object LOCK = new Object();
    private static TMDbRepository sInstance;

    private final TMDbApi mService;

    private OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    request = request.newBuilder()
                            .addHeader(TMDB_API_KEY_PARAM_NAME, TMDB_API_KEY_VALUE)
                            .build();
                    return chain.proceed(request);
                }).build();
    }

    private Retrofit provideRetrofit(String baseUrl, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private TMDbRepository() {
        mService = provideRetrofit(TMDB_END_POINT, provideOkHttpClient()).create(TMDbApi.class);
    }

    public LiveData<Resource<List<Game>>> getGames() {
        return mService.getGames(GAMES_FIELD + GAMES_SORTING + GAMES_FILTERS);
    }

    private String GAMES_FIELD = "fields name; ";
    private String GAMES_SORTING = "sort total_rating desc; ";
    private String GAMES_FILTERS = "where total_rating_count >= 10;";

    public static TMDbRepository getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new TMDbRepository();
            }
        }
        return sInstance;
    }
}
