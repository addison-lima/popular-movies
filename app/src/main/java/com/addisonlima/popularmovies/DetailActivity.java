package com.addisonlima.popularmovies;

import android.arch.lifecycle.Observer;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.addisonlima.popularmovies.models.Movie;
import com.addisonlima.popularmovies.models.Review;
import com.addisonlima.popularmovies.models.ReviewsResponse;
import com.addisonlima.popularmovies.models.VideosResponse;
import com.addisonlima.popularmovies.repository.TMDbRepository;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_MOVIE = "movie";
    public static final String EXTRA_FAVORITE = "favorite";

    private TMDbRepository mRepository = TMDbRepository.getInstance(this.getApplication());

    private Movie mMovie;
    private boolean mIsFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            populateUi(mMovie);
        } else {
            Toast.makeText(this, getString(R.string.more_info_error),
                    Toast.LENGTH_LONG).show();
            finish();
        }

        Button btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setOnClickListener(this);

        if (getIntent().hasExtra(EXTRA_FAVORITE)) {
            mIsFavorite = getIntent().getBooleanExtra(EXTRA_FAVORITE, false);
        }

        updateFavoriteButton();

        mRepository.getVideosResponse().observe(this, getVideosResponseObserver());
        mRepository.getReviewsResponse().observe(this, getReviewsResponseObserver());

        mRepository.getVideosById(mMovie.getId());
        mRepository.getReviewsById(mMovie.getId());
    }

    @Override
    public void onClick(View view) {
        if (mIsFavorite) {
            mRepository.unmarkAsFavorite(mMovie);
        } else {
            mRepository.markAsFavorite(mMovie);
        }
        mIsFavorite = !mIsFavorite;
        updateFavoriteButton();
    }

    private Observer<VideosResponse> getVideosResponseObserver() {
        return new Observer<VideosResponse>() {
            @Override
            public void onChanged(@Nullable VideosResponse videosResponse) {
                if (videosResponse != null && videosResponse.getVideos().length > 0) {
                }
            }
        };
    }

    private void startYouTube(String videoId) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("vnd.youtube:" + videoId));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + videoId));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }

    private Observer<ReviewsResponse> getReviewsResponseObserver() {
        return new Observer<ReviewsResponse>() {
            @Override
            public void onChanged(@Nullable ReviewsResponse reviewsResponse) {
                TextView tvReviews = findViewById(R.id.tv_reviews);
                if (reviewsResponse != null && reviewsResponse.getReviews().length > 0) {
                    String reviews = "";
                    for (Review review : reviewsResponse.getReviews()) {
                        reviews += "(" + review.getAuthor() + ")\n";
                        reviews += review.getContent() + "\n\n";
                    }
                    tvReviews.setText(reviews);
                } else {
                    tvReviews.setText(R.string.no_review_available);
                }
            }
        };
    }

    private void populateUi(Movie movie) {
        ImageView ivPoster = findViewById(R.id.iv_poster);

        Picasso.with(this)
                .load(movie.getPosterFullPath())
                .error(R.color.colorPrimary)
                .into(ivPoster);

        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText(movie.getTitle());

        TextView tvOriginalTitle = findViewById(R.id.tv_original_title);
        tvOriginalTitle.setText(movie.getOriginalTitle());

        TextView tvReleaseDate = findViewById(R.id.tv_release_date);
        tvReleaseDate.setText(movie.getReleaseDate());

        TextView tvUserRating = findViewById(R.id.tv_user_rating);
        tvUserRating.setText(movie.getVoteAverage());

        TextView tvOverview = findViewById(R.id.tv_overview);
        tvOverview.setText(movie.getOverview());
    }

    private void updateFavoriteButton() {
        Button btnFavorite = findViewById(R.id.btn_favorite);
        btnFavorite.setText(mIsFavorite ? R.string.unmark_as_favorite : R.string.mark_as_favorite);
    }
}
