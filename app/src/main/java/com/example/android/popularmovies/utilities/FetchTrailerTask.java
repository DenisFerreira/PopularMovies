package com.example.android.popularmovies.utilities;

import android.os.AsyncTask;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.Trailer;
import com.example.android.popularmovies.utilities.MoviesJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;

/**
 * Created by lsitec205.ferreira on 07/08/17.
 */

public class FetchTrailerTask extends AsyncTask<URL, Void, Trailer[]> {

    public interface OnTaskCompleted{
        void onTaskStart();
        void onTaskCompleted(Trailer[] result);
    }

    private OnTaskCompleted mListener;
    public FetchTrailerTask(OnTaskCompleted listener) {
        mListener = listener;
    }

    @Override
    protected Trailer[] doInBackground(URL... params) {
        if(params.length == 0)
            return null;
        try {
            String  response = NetworkUtils.getResponseFromHttpUrl(params[0]);
            return MoviesJsonUtils.getTrailersFromJson(response);
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
    protected void onPostExecute(Trailer[] result) {
        super.onPostExecute(result);
        mListener.onTaskCompleted(result);
    }
}