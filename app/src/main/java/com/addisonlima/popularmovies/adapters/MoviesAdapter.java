package com.addisonlima.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.addisonlima.popularmovies.R;
import com.addisonlima.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private final MoviesAdapterOnClickHandler mClickHandler;

    private Context mContext;
    private Movie[] mMoviesData;

    public interface MoviesAdapterOnClickHandler {

        void onClick(Movie movie);
    }

    public MoviesAdapter(Context context, MoviesAdapterOnClickHandler clickHandler) {

        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutId = R.layout.movie_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, viewGroup, shouldAttachToParentImmediately);
        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesAdapterViewHolder moviesAdapterViewHolder, int i) {

        Movie movie = mMoviesData[i];

        Picasso.with(mContext)
                .load(movie.getPosterPath())
                .error(R.color.colorPrimary)
                .into(moviesAdapterViewHolder.ivPoster);
    }

    @Override
    public int getItemCount() {

        if (mMoviesData == null) {
            return 0;
        }
        return mMoviesData.length;
    }

    public void setMoviesData(Movie[] moviesData) {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView ivPoster;

        public MoviesAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            ivPoster = itemView.findViewById(R.id.ivPoster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mMoviesData[adapterPosition]);
        }
    }
}
