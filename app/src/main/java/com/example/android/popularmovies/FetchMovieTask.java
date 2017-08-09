package com.example.android.popularmovies;

import android.os.AsyncTask;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.MoviesJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by lsitec205.ferreira on 07/08/17.
 */

public class FetchMovieTask extends AsyncTask<URL, Void, Movie[]> {

    public interface OnTaskCompleted{
        void onTaskStart();
        void onTaskCompleted(Movie[] result);
    }

    private OnTaskCompleted mListener;
    public FetchMovieTask(OnTaskCompleted listener) {
        mListener = listener;
    }

    @Override
    protected Movie[] doInBackground(URL... params) {
        if(params.length == 0)
            return null;
        try {
            String  response = NetworkUtils.getResponseFromHttpUrl(params[0]);
            return MoviesJsonUtils.getMoviesFromJson(response);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mListener.onTaskStart();
    }

    @Override
    protected void onPostExecute(Movie[] result) {
        super.onPostExecute(result);
        mListener.onTaskCompleted(result);
    }
}