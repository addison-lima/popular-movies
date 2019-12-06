package com.addisonlima.popularmovies;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.addisonlima.popularmovies.model.Game;
import com.addisonlima.popularmovies.repository.TMDbRepository;
import com.github.leonardoxh.livedatacalladapter.Resource;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TMDbRepository test = TMDbRepository.getInstance();
        test.getGames().observe(this, new Observer<Resource<List<Game>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Game>> listResource) {
            }
        });
    }
}
