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
        super.deliverResult(json);
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}