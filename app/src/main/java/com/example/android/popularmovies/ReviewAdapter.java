package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Trailer;

/**
 * Created by lsitec205.ferreira on 03/08/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.GridViewHolder>{

    private Review[] mReviews;
    private Context mContext;

    public void setReviewData(Review[] reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_main;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        Review review = mReviews[position];
        holder.mReviewerName.setText(review.getAuthor());
        holder.mReviewContent.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        if(mReviews == null) return 0;
        return mReviews.length;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {

        public final TextView mReviewerName;
        public final TextView mReviewContent;

        GridViewHolder(final View itemView) {
            super(itemView);
            mReviewerName = (TextView) itemView.findViewById(R.id.reviewer_name);
            mReviewContent = (TextView) itemView.findViewById(R.id.review_content);

        }
    }
}
