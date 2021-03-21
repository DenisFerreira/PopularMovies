package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.Trailer;

/**
 * Created by lsitec205.ferreira on 03/08/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.GridViewHolder>{

    private Trailer[] mTrailers;
    private Context mContext;

    public void setTrailerData(Trailer[] trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailer_main;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        Trailer trailer = mTrailers[position];
        holder.mYoutubeIntent = trailer.getYoutubeIntent();
        holder.mWebIntent = trailer.getWebIntent();
        holder.mTrailerName.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        if(mTrailers == null) return 0;
        return mTrailers.length;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {

        final ImageView mPlayVideoImage;
        public final TextView mTrailerName;
        public Intent mYoutubeIntent;
        public Intent mWebIntent;

        GridViewHolder(final View itemView) {
            super(itemView);
            mPlayVideoImage = (ImageView) itemView.findViewById(R.id.play_video_image_view);
            mTrailerName = (TextView) itemView.findViewById(R.id.trailer_name);
            mPlayVideoImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    try {
                        context.startActivity(mYoutubeIntent);
                    }catch (ActivityNotFoundException ex) {
                        context.startActivity(mWebIntent);
                    }
                }
            });

        }
    }
}
