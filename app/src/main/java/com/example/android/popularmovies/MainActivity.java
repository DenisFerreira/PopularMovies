package com.example.android.popularmovies;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks {

    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mProgressBar;
    private MovieAdapter mAdapter;

    private static final int ID_LOADER_MAIN = 101;
    private static final int ID_LOADER_FAVORITES = 102;

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

        mAdapter = new MovieAdapter();
        mRecyclerView.setAdapter(mAdapter);
        loadMovieData(R.id.action_sort_most_popular);
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
        loadMovieData(id);
        return super.onOptionsItemSelected(item);
    }

    private void loadMovieData(int id){
        Bundle loaderBundle = new Bundle();
        int loaderId = ID_LOADER_MAIN;
        if(id == R.id.action_sort_most_popular){
            loaderBundle.putInt(EXTRA_DISPLAY_TYPE, MOST_POPULAR_DISPLAY_TYPE);
        }
        else if(id == R.id.action_sort_top_rated){
            loaderBundle.putInt(EXTRA_DISPLAY_TYPE, TOP_RATED_DISPLAY_TYPE);
        }
        else if(id == R.id.action_sort_favorite){
            loaderId = ID_LOADER_FAVORITES;
        }
        getSupportLoaderManager().initLoader(loaderId, loaderBundle, this);
        getSupportLoaderManager().restartLoader(loaderId, loaderBundle,
                this);

    }
    private void updateData(Movie[] movies) {
        showDataView();
        mProgressBar.setVisibility(View.INVISIBLE);
        mAdapter.setMovieData(movies);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_LOADER_MAIN:
                final int displayType = args.getInt(EXTRA_DISPLAY_TYPE);
                URL url_display = null;
                switch (displayType) {
                    case MOST_POPULAR_DISPLAY_TYPE:
                            setTitle(getString(R.string.most_popular_movies)) ;
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
                Cursor cursor = (Cursor) data;
                if (cursor != null) {
                    List<Movie> movies = new ArrayList<>();
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                            .moveToNext()) {
                        Movie movie = new Movie();
                        movie.setId(cursor.getInt(INDEX_TMDB_MOVIE_ID));
                        movie.setTitle(cursor.getString(INDEX_TMDB_TITLE));
                        movie.setPoster_path(cursor.getString(INDEX_TMDB_POSTER_PATH));
                        movie.setRelease_date(new Date(cursor.getLong
                                (INDEX_TMDB_RELEASE_DATE)));
                        movie.setVote_average(cursor.getDouble(INDEX_TMDB_VOTE_AVERAGE));
                        movie.setOverview(cursor.getString(INDEX_TMDB_OVERVIEW));
                        movies.add(movie);
                    }
                    Movie[] result = new Movie[movies.size()];
                    int i = 0;
                    for(Movie m : movies) {
                        result[i] = m;
                        i++;
                    }
                    updateData(result);

                    cursor.close();
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
}
