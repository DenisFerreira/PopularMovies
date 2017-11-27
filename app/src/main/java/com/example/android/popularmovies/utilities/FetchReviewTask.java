package com.example.android.popularmovies.utilities;

import android.os.AsyncTask;
import com.example.android.popularmovies.data.Review;
import java.net.URL;

/**
 * Created by lsitec205.ferreira on 07/08/17.
 */

public class FetchReviewTask extends AsyncTask<URL, Void, Review[]> {

    public interface OnTaskCompleted{
        void onTaskStart();
        void onTaskCompleted(Review[] result);
    }

    private FetchReviewTask.OnTaskCompleted mListener;
    public FetchReviewTask(FetchReviewTask.OnTaskCompleted listener) {
        mListener = listener;
    }

    @Override
    protected Review[] doInBackground(URL... params) {
        if(params.length == 0)
            return null;
        try {
            String  response = NetworkUtils.getResponseFromHttpUrl(params[0]);
            return MoviesJsonUtils.getReviewsFromJson(response);
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
    protected void onPostExecute(Review[] result) {
        super.onPostExecute(result);
        mListener.onTaskCompleted(result);
    }
}