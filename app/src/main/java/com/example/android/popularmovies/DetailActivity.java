package com.example.android.popularmovies;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Trailer;
import com.example.android.popularmovies.utilities.FetchReviewTask;
import com.example.android.popularmovies.utilities.FetchTrailerTask;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class DetailActivity extends AppCompatActivity implements FetchTrailerTask.OnTaskCompleted, FetchReviewTask.OnTaskCompleted{

    private Movie mMovie;
    private TextView mTitleTextView;
    private TextView mYearTextView;
    private TextView mRatingTextView;
    private TextView mOverwiewTextView;
    private ImageView mPosterImageView;
    private Button mFavoriteButton;
    private TrailerAdapter mAdapter;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private ContentValues values;

    private static final int ID_INSERT_MOVIE_LOADER = 201;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mOverwiewTextView = (TextView) findViewById(R.id.movie_overview);
        mTitleTextView = (TextView) findViewById(R.id.movie_title);
        mYearTextView = (TextView) findViewById(R.id.movie_year);
        mRatingTextView = (TextView) findViewById(R.id.movie_rating);
        mPosterImageView = (ImageView) findViewById(R.id.iv_banner_main);
        mFavoriteButton = (Button) findViewById(R.id.favorite_button);

        mRecyclerView = (RecyclerView) findViewById(R.id.rc_trailer_grid);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DetailActivity.this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new TrailerAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rc_review_grid);
        LinearLayoutManager anotherlayoutManager = new LinearLayoutManager(DetailActivity.this);
        mReviewRecyclerView.setLayoutManager(anotherlayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);

        if(getIntent().hasExtra("movie")) {
            mMovie = (Movie) getIntent().getSerializableExtra("movie");
            setTitle(getString(R.string.details)) ;
            mOverwiewTextView.setText(mMovie.getOverview());
            mTitleTextView.setText(mMovie.getTitle());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            mYearTextView.setText(format.format(mMovie.getReleaseDate()));
            mRatingTextView.setText(String.valueOf(mMovie.getVoteAverage())+"/10");
            Picasso.with(this).load(NetworkUtils.buildURLGetPoster(mMovie.getPosterPath())).into(mPosterImageView);
            new FetchTrailerTask(this).execute(NetworkUtils.buildUrlGetMovieTraillers(mMovie.getId(), getBaseContext()));
            new FetchReviewTask(this).execute(NetworkUtils.buildUrlGetMovieReviews(mMovie.getId(), getBaseContext()));
            values = new ContentValues();
            values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
            values.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, mMovie.getOverview());
            values.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
            values.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
            values.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate().getTime());
            values.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());

            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, values);
                    if(uri != null) {
                        Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onTaskStart() {

    }

    @Override
    public void onTaskCompleted(Review[] result) {
        mReviewAdapter.setReviewData(result);
    }

    @Override
    public void onTaskCompleted(Trailer[] result) {
        mAdapter.setTrailerData(result);
    }

}
