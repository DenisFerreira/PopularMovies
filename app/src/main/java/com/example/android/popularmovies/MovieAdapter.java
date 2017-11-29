package com.example.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by lsitec205.ferreira on 03/08/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.GridViewHolder>{

    private Movie[] mMovies;
    private Context mContext;
    private MovieAdapterClickListener mListener;


    public interface MovieAdapterClickListener {
        void onMovieItemClick(View view, int position);
    }

    public Movie getItem(int id) {
        return mMovies == null ? null : mMovies[id];
    }

    MovieAdapter(MovieAdapterClickListener listener){
        mListener = listener;
    }

    public void setMovieData(Movie[] movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public GridViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int layoutIdForListItem = R.layout.banner_main;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new GridViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GridViewHolder holder, int position) {
        Movie movie = mMovies[position];
        holder.setActualMovie(movie);
        Picasso.with(mContext).load(NetworkUtils.buildURLGetPoster(movie.getPosterPath()))
                .placeholder(R.drawable.place_holder)
                .into(holder.mBannerImage);
    }

    @Override
    public int getItemCount() {
        if(mMovies == null) return 0;
        return mMovies.length;
    }

    class GridViewHolder extends RecyclerView.ViewHolder {

        final ImageView mBannerImage;
        private Movie mActualMovie;

        GridViewHolder(final View itemView) {
            super(itemView);
            mBannerImage = (ImageView) itemView.findViewById(R.id.iv_banner_main);
            mBannerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onMovieItemClick(itemView, getAdapterPosition());
                }
            });
        }

        void setActualMovie(Movie movie) {
            mActualMovie = movie;
        }
    }
}
