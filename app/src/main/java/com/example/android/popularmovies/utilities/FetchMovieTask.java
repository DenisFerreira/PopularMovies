package com.example.android.popularmovies.utilities;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.popularmovies.data.Movie;

import java.net.URL;

/**
 * Created by lsitec205.ferreira on 07/08/17.
 */

public class FetchMovieTask extends AsyncTaskLoader<Movie[]> {

    private URL mURL;
    private Movie[] mLastMoviesResult;

    public FetchMovieTask(Context context, URL url) {
        super(context);
        mURL = url;
    }

    @Override
    public Movie[] loadInBackground() {
        try {
            String  response = NetworkUtils.getResponseFromHttpUrl(mURL);
            return MoviesJsonUtils.getMoviesFromJson(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deliverResult(Movie[] json) {
        mLastMoviesResult = json;
        super.deliverResult(json);
    }


    @Override
    protected void onStartLoading() {
        /* If no arguments were passed, we don't have a query to perform.
        Simply return. */
        if (mURL == null) {
            return;
        }

        /*
         * When we initially begin loading in the background, we want to
         * display the
         * loading indicator to the user
         */

        /*
         * If we already have cached results, just deliver them now. If we
         * don't have any
         * cached results, force a load.
         */
        if (mLastMoviesResult != null) {
            deliverResult(mLastMoviesResult);
        } else {
            forceLoad();
        }
    }
}