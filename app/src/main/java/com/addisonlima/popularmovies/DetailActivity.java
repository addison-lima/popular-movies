package com.addisonlima.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.addisonlima.popularmovies.models.Movie;
import com.addisonlima.popularmovies.repository.TMDbRepository;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            populateUi(mMovie);

            //TEST
            markAsFavorite();
        } else {
            Toast.makeText(this, getString(R.string.more_info_error),
                    Toast.LENGTH_LONG).show();
            finish();
        }
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

    private void markAsFavorite() {
        TMDbRepository repository = TMDbRepository.getInstance(this.getApplication());
        repository.markAsFavorite(mMovie);
    }
}
