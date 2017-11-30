package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.utilities.FetchMovieTask;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks, MovieAdapter.MovieAdapterClickListener {

    private static final String SAVED_LAYOUT_MANAGER = "saved_layout_manager";
    private Parcelable layoutManagerSavedState;
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;
    private MovieAdapter mAdapter;

    private static final int ID_LOADER_MAIN = 101;
    private static final int ID_LOADER_FAVORITES = 102;

    private static int mActualDisplayType;
    private static final String EXTRA_DISPLAY_TYPE = "display_type";
    private static final int MOST_POPULAR_DISPLAY_TYPE = 33;
    private static final int TOP_RATED_DISPLAY_TYPE = 34;
    private static final int FAVORITE_DISPLAY_TYPE = 35;

    public static final String[] MAIN_MOVIE_PROJECTION = {
            MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW
    };

    public static final int INDEX_TMDB_MOVIE_ID = 0;
    public static final int INDEX_TMDB_TITLE = 1;
    public static final int INDEX_TMDB_POSTER_PATH = 2;
    public static final int INDEX_TMDB_RELEASE_DATE = 3;
    public static final int INDEX_TMDB_VOTE_AVERAGE = 4;
    public static final int INDEX_TMDB_OVERVIEW = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rc_movie_grid);
        mErrorMessageDisplay  = (TextView) findViewById(R.id.tv_error_message);
        mProgressBar = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mAdapter);


        if(savedInstanceState != null ){
            layoutManagerSavedState = savedInstanceState.getParcelable(SAVED_LAYOUT_MANAGER);
            mActualDisplayType = savedInstanceState.getInt(EXTRA_DISPLAY_TYPE);
        }else {
            mActualDisplayType = MOST_POPULAR_DISPLAY_TYPE;
        }
        loadMovieData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loadMovieData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        layoutManagerSavedState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(SAVED_LAYOUT_MANAGER, layoutManagerSavedState);
        outState.putInt(EXTRA_DISPLAY_TYPE, mActualDisplayType);
        super.onSaveInstanceState(outState);
    }

    private void restoreLayoutManagerPosition() {
        if (layoutManagerSavedState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(layoutManagerSavedState);
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int widthDivider = 500;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorView() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_sort_most_popular) {
            mActualDisplayType = MOST_POPULAR_DISPLAY_TYPE;
        }

        else if(id == R.id.action_sort_top_rated)
            mActualDisplayType =  TOP_RATED_DISPLAY_TYPE;

        else if(id == R.id.action_sort_favorite)
            mActualDisplayType = FAVORITE_DISPLAY_TYPE;
        mRecyclerView.getLayoutManager().scrollToPosition(0);
        loadMovieData();
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(){
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        Bundle loaderBundle = new Bundle();
        loaderBundle.putInt(EXTRA_DISPLAY_TYPE, mActualDisplayType);
        switch (mActualDisplayType) {
            case MOST_POPULAR_DISPLAY_TYPE:
            case TOP_RATED_DISPLAY_TYPE:
                Loader mainLoader = getSupportLoaderManager().getLoader(ID_LOADER_MAIN);
                if (mainLoader == null) {
                    getSupportLoaderManager().initLoader(ID_LOADER_MAIN, loaderBundle,
                            this);
                } else
                    getSupportLoaderManager().restartLoader(ID_LOADER_MAIN, loaderBundle,
                            this);
                break;

            case FAVORITE_DISPLAY_TYPE:
                Loader favoritesLoader = getSupportLoaderManager().getLoader
                        (ID_LOADER_FAVORITES);
                if (favoritesLoader == null) {
                    getSupportLoaderManager().initLoader(ID_LOADER_FAVORITES, loaderBundle, this);
                } else {
                    getSupportLoaderManager().restartLoader(ID_LOADER_FAVORITES, loaderBundle,
                            this);
                }
                break;
        }
    }


    private void updateData(Movie[] movies) {
        showDataView();
        mProgressBar.setVisibility(View.INVISIBLE);
        mAdapter.setMovieData(movies);
        mAdapter.notifyDataSetChanged();
        restoreLayoutManagerPosition();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_LOADER_MAIN:
                URL url_display = null;
                final int displayType = args.getInt(EXTRA_DISPLAY_TYPE);
                switch (displayType) {
                    case MOST_POPULAR_DISPLAY_TYPE:
                        setTitle(getString(R.string.most_popular_movies));
                        url_display = NetworkUtils.buildUrlMostPopular(getBaseContext());
                        break;
                    case TOP_RATED_DISPLAY_TYPE:
                        setTitle((getString(R.string.top_rated_movies)));
                        url_display = NetworkUtils.buildUrlTopRated(getBaseContext());
                        break;
                }
                return new FetchMovieTask(this, url_display);

            case ID_LOADER_FAVORITES:
                setTitle((getString(R.string.favorite_movies)));
                Uri queryUri = MovieContract.MovieEntry.CONTENT_URI;
                String sortOrder = MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " DESC";
                return new CursorLoader(this, queryUri, MAIN_MOVIE_PROJECTION, null, null, sortOrder);
            default:
                throw new RuntimeException("Not implemented Loader "+ id);
        }
    }
    @Override
    public void onLoadFinished(Loader loader, Object data) {

        int id = loader.getId();
        switch (id){
            case ID_LOADER_MAIN:
                if(data != null){
                    updateData((Movie[])data);
                }else {
                    showErrorView();
                }
                break;
            case ID_LOADER_FAVORITES:
                if(data!=null) {
                    Cursor cursor = (Cursor) data;
                    List<Movie> movies = new ArrayList<>();
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                            .moveToNext()) {
                        Movie movie = new Movie();
                        movie.setId(cursor.getInt(INDEX_TMDB_MOVIE_ID));
                        movie.setTitle(cursor.getString(INDEX_TMDB_TITLE));
                        movie.setPosterPath(cursor.getString(INDEX_TMDB_POSTER_PATH));
                        movie.setReleaseDate(new Date(cursor.getLong
                                (INDEX_TMDB_RELEASE_DATE)));
                        movie.setVoteAverage(cursor.getDouble(INDEX_TMDB_VOTE_AVERAGE));
                        movie.setOverview(cursor.getString(INDEX_TMDB_OVERVIEW));
                        movies.add(movie);
                    }
                    cursor.close();
                    Movie[] result = new Movie[movies.size()];
                    int i = 0;
                    for(Movie m : movies) {
                        result[i] = m;
                        i++;
                    }
                    updateData(result);


                } else
                    showErrorView();
                break;
            default:
                throw new RuntimeException("Not implemented Loader "+ id);


        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onMovieItemClick(View view, int position) {
        Context context = getBaseContext();
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("movie", mAdapter.getItem(position));
        startActivity(intent);
    }
}
