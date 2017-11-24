package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Trailer;

import java.io.IOException;
import java.net.URL;

/**
 * Created by lsitec205.ferreira on 07/08/17.
 */

public class FetchReviewTask extends AsyncTaskLoader<Review[]> {

    private URL mURL;

    public FetchReviewTask(Context context, URL url) {
        super(context);
        mURL = url;
    }

    @Override
    public Review[] loadInBackground() {
        String  response = null;
        try {
            response = NetworkUtils.getResponseFromHttpUrl(mURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MoviesJsonUtils.getReviewsFromJson(response);
    }


}