package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.text.format.DateUtils;

import com.example.android.popularmovies.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lsitec205.ferreira on 03/08/17.
 */

public class MoviesJsonUtils {

    public static Movie[] getMoviesFromJson(String moviesResultJson) throws JSONException {

        final String STATUS_CODE = "status_code";
        final String OWN_RESULT = "results";

        JSONObject movieJson = new JSONObject(moviesResultJson);

        Movie[] parsedMovies = null;
        if(movieJson.has(STATUS_CODE)) {
            int errorCode = movieJson.getInt(STATUS_CODE);

            switch (errorCode) {
                case 7:
                    break;
                case 34:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray moviesArray = movieJson.getJSONArray(OWN_RESULT);
        parsedMovies = new Movie[moviesArray.length()];
        for(int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieObject =  moviesArray.getJSONObject(i);

            Movie movie = new Movie();
            movie.setId(movieObject.getInt("id"));
            movie.setOriginal_language(movieObject.getString("original_language"));
            movie.setOriginal_title(movieObject.getString("original_title"));
            movie.setOverview(movieObject.getString("overview"));
            movie.setPopularity(movieObject.getDouble("popularity"));
            movie.setPoster_path(movieObject.getString("poster_path"));
            movie.setVote_average(movieObject.getDouble("vote_average"));
            movie.setTitle(movieObject.getString("title"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date date = null;
            try {
                date = format.parse(movieObject.getString("release_date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movie.setRelease_date(date);

            parsedMovies[i] = movie;
        }

        return parsedMovies;
    }

}
