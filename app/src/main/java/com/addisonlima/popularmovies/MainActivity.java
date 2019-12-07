package com.addisonlima.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.addisonlima.popularmovies.repository.TMDbRepository;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TMDbRepository test = TMDbRepository.getInstance();
    }
}
