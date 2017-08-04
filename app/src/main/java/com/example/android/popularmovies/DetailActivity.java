package com.example.android.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity {

    private Movie mMovie;
    private TextView mTitleTextView;
    private TextView mYearTextView;
    private TextView mRatingTextView;
    private TextView mOverwiewTextView;
    private ImageView mPosterImageView;
    private Button mFavoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mOverwiewTextView = (TextView) findViewById(R.id.movie_overview);
        mTitleTextView = (TextView) findViewById(R.id.movie_title);
        mYearTextView = (TextView) findViewById(R.id.movie_year);
        mRatingTextView = (TextView) findViewById(R.id.movie_rating);
        mPosterImageView = (ImageView) findViewById(R.id.movie_poster);
        mFavoriteButton = (Button) findViewById(R.id.favorite_button);

        if(getIntent().hasExtra("movie")) {
            mMovie = (Movie) getIntent().getSerializableExtra("movie");
            mOverwiewTextView.setText(mMovie.getOverview());
            mTitleTextView.setText(mMovie.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            mYearTextView.setText(format.format(mMovie.getRelease_date()));
            mRatingTextView.setText(String.valueOf(mMovie.getVote_average())+"/10");
            Picasso.with(this).load(NetworkUtils.buildURLGetPoster(mMovie.getPoster_path())).into(mPosterImageView);
        }
    }
}
