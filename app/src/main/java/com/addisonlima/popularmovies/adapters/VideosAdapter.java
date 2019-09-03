package com.addisonlima.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.addisonlima.popularmovies.R;
import com.addisonlima.popularmovies.models.Video;
import com.squareup.picasso.Picasso;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosAdapterViewHolder> {

    private final VideosAdapterOnClickHandler mClickHandler;

    private Context mContext;
    private Video[] mVideosData;

    public interface VideosAdapterOnClickHandler {

        void onClick(Video video);
    }

    public VideosAdapter(Context context, VideosAdapterOnClickHandler clickHandler) {

        mContext = context;
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public VideosAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        int layoutId = R.layout.movie_item;
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutId, viewGroup, shouldAttachToParentImmediately);
        return new VideosAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapterViewHolder videosAdapterViewHolder, int i) {

        Video video = mVideosData[i];

        Picasso.with(mContext)
                .load(video.getThumbnailPath())
                .error(R.color.colorPrimary)
                .into(videosAdapterViewHolder.ivThumbnail);
    }

    @Override
    public int getItemCount() {

        if (mVideosData == null) {
            return 0;
        }
        return mVideosData.length;
    }

    public void setVideosData(Video[] videosData) {
        mVideosData = videosData;
        notifyDataSetChanged();
    }

    public class VideosAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView ivThumbnail;

        public VideosAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            ivThumbnail = itemView.findViewById(R.id.iv_item);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mVideosData[adapterPosition]);
        }
    }
}
