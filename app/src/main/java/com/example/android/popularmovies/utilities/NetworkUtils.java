/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the weather servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185//";
    private static final String MOST_POPULAR_URL = "https://api.themoviedb.org/3/movie/popular";
    private static final String TOP_RATED_URL = "https://api.themoviedb.org/3/movie/top_rated";
    private static final String GET_MOVIE_URL = "https://api.themoviedb.org/3/movie";
    private static final String GET_VIDEO_URL = "videos";
    private static final String GET_REVIEW_URL = "reviews";


    final static String APY_KEY_PARAM = "api_key";
    final static String PAGE_PARAM = "page";
    final static String N_PAGES = "1";
    final static String MOVIE_ID_PARAM = "movie_id";

    public static URL buildUrlMostPopular(Context context) {
        Uri builtUri = Uri.parse(MOST_POPULAR_URL).buildUpon()
                .appendQueryParameter(APY_KEY_PARAM, context.getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN))
//                .appendQueryParameter(PAGE_PARAM, N_PAGES)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlTopRated(Context context) {
        Uri builtUri = Uri.parse(TOP_RATED_URL).buildUpon()
                .appendQueryParameter(APY_KEY_PARAM, context.getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN))
                .appendQueryParameter(PAGE_PARAM, N_PAGES)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlGetMovie(int movieID, Context context) {
        Uri builtUri = Uri.parse(GET_MOVIE_URL).buildUpon()
                .appendQueryParameter(APY_KEY_PARAM, context.getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN))
                .appendQueryParameter(MOVIE_ID_PARAM, String.valueOf(movieID))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String buildURLGetPoster(String path) {
        String result = IMAGE_BASE_URL+path;
        Log.v(TAG, "Built URI " + result);
        return result;
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static URL buildUrlGetMovieTraillers(int movieID, Context context) {
        Uri builtUri = Uri.parse(GET_MOVIE_URL).buildUpon()
                .appendPath(String.valueOf(movieID))
                .appendPath(GET_VIDEO_URL)
                .appendQueryParameter(APY_KEY_PARAM, context.getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlGetMovieReviews(int movieID, Context context) {
        Uri builtUri = Uri.parse(GET_MOVIE_URL).buildUpon()
                .appendPath(String.valueOf(movieID))
                .appendPath(GET_REVIEW_URL)
                .appendQueryParameter(APY_KEY_PARAM, context.getResources().getString(R.string.THE_MOVIE_DB_API_TOKEN))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }
}