package com.addisonlima.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.addisonlima.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (getIntent().hasExtra(EXTRA_MOVIE)) {
            Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE);
            populateUi(movie);
        } else {
            Toast.makeText(this, getString(R.string.more_info_error),
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void populateUi(Movie movie) {
        ImageView ivPoster = findViewById(R.id.iv_poster);

        Picasso.with(this)
                .load(movie.getPosterPath())
                .error(R.color.colorPrimary)
                .into(ivPoster);
    }
}
