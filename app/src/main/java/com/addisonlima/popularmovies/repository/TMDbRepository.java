package com.addisonlima.popularmovies.repository;

import com.addisonlima.popularmovies.BuildConfig;
import com.addisonlima.popularmovies.model.Game;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private TMDbRepository() {
        mService = provideRetrofit(TMDB_END_POINT, provideOkHttpClient()).create(TMDbApi.class);
        Call<List<Game>> user = mService.getGames(GAMES_FIELD + GAMES_SORTING + GAMES_FILTERS);
        user.enqueue(new Callback<List<Game>>() {
            @Override
            public void onResponse(Call<List<Game>> call, Response<List<Game>> response) {
            }

            @Override
            public void onFailure(Call<List<Game>> call, Throwable t) {
            }
        });
    }

    private String GAMES_FIELD2 = "fields cover.url, videos.video_id, summary, name, aggregated_rating, aggregated_rating_count, platforms.name, first_release_date; ";
    private String GAMES_FIELD = "fields name, summary; ";
    private String GAMES_SORTING = "sort aggregated_rating desc; ";
    private String GAMES_FILTERS = "where aggregated_rating_count >= 10; limit 20;";

    public static TMDbRepository getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new TMDbRepository();
            }
        }
        return sInstance;
    }
}
