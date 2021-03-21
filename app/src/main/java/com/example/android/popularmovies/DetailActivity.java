package com.example.android.popularmovies;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
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

    private static final String SAVED_TRAILER_LAYOUT_MANAGER_STATE = "trailer_layout_state";
    private static final String SAVED_REVIEW_LAYOUT_MANAGER_STATE = "review_layout_state";
    private static final String DETAIL_SCROLL_POSITION = "detail_scroll_position";
    private Movie mMovie;
    private TextView mTitleTextView;
    private TextView mYearTextView;
    private TextView mRatingTextView;
    private TextView mOverwiewTextView;
    private ImageView mPosterImageView;
    private Button mFavoriteButton;
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private LinearLayoutManager mTrailerLayoutManager;
    private LinearLayoutManager mReviewLayoutManager;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;
    private Parcelable mTrailerLayoutManagerState;
    private Parcelable mReviewLayoutManagerState;
    private ScrollView mDetailScrollView;

    private ContentValues values;

    private static final String MOVIE_EXTRA = "movie";

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
        mDetailScrollView = (ScrollView) findViewById(R.id.detail_scroll);

        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rc_trailer_grid);
        mTrailerLayoutManager = new LinearLayoutManager(DetailActivity.this);
        mTrailerRecyclerView.setLayoutManager(mTrailerLayoutManager);
        mTrailerRecyclerView.setHasFixedSize(true);
        mTrailerAdapter = new TrailerAdapter();
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rc_review_grid);
        mReviewLayoutManager = new LinearLayoutManager(DetailActivity.this);
        mReviewRecyclerView.setLayoutManager(mReviewLayoutManager);
        mReviewRecyclerView.setHasFixedSize(true);
        mReviewAdapter = new ReviewAdapter();
        mReviewRecyclerView.setAdapter(mReviewAdapter);
/*
        if(savedInstanceState != null){
            mTrailerLayoutManagerState = savedInstanceState.getParcelable(SAVED_TRAILER_LAYOUT_MANAGER_STATE);
            mReviewLayoutManagerState = savedInstanceState.getParcelable(SAVED_REVIEW_LAYOUT_MANAGER_STATE);
            final int[] position = savedInstanceState.getIntArray(DETAIL_SCROLL_POSITION);
            if(position != null) {
                mDetailScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        mDetailScrollView.scrollTo(position[0], position[1]);
                    }
                });
            }
        }
*/
        if(getIntent().hasExtra(MOVIE_EXTRA)) {
            mMovie =  getIntent().getParcelableExtra(MOVIE_EXTRA);
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
        mReviewAdapter.notifyDataSetChanged();
        if (mReviewLayoutManagerState != null) {
            mReviewRecyclerView.getLayoutManager().onRestoreInstanceState(mReviewLayoutManagerState);
        }
    }

    @Override
    public void onTaskCompleted(Trailer[] result) {
        mTrailerAdapter.setTrailerData(result);
        mTrailerAdapter.notifyDataSetChanged();
        if (mTrailerLayoutManagerState != null) {
            mTrailerRecyclerView.getLayoutManager().onRestoreInstanceState(mTrailerLayoutManagerState);
        }
    }
/*
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        mTrailerLayoutManagerState = mTrailerRecyclerView.getLayoutManager().onSaveInstanceState();
        mReviewLayoutManagerState = mReviewRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SAVED_TRAILER_LAYOUT_MANAGER_STATE, mTrailerLayoutManagerState);
        outState.putParcelable(SAVED_REVIEW_LAYOUT_MANAGER_STATE, mReviewLayoutManagerState);
        outState.putIntArray(DETAIL_SCROLL_POSITION,
                new int[]{mDetailScrollView.getScrollX(), mDetailScrollView.getScrollY()});
        super.onSaveInstanceState(outState, outPersistentState);
    }
*/
}
