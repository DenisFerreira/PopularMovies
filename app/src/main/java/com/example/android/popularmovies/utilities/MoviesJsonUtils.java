package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.Review;
import com.example.android.popularmovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            movie.setOriginalLanguage(movieObject.getString("original_language"));
            movie.setOriginalTitle(movieObject.getString("original_title"));
            movie.setOverview(movieObject.getString("overview"));
            movie.setPopularity(movieObject.getDouble("popularity"));
            movie.setPosterPath(movieObject.getString("poster_path"));
            movie.setVoteAverage(movieObject.getDouble("vote_average"));
            movie.setTitle(movieObject.getString("title"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
            Date date = null;
            try {
                date = format.parse(movieObject.getString("release_date"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            movie.setReleaseDate(date);

            parsedMovies[i] = movie;
        }

        return parsedMovies;
    }

    public static Trailer[] getTrailersFromJson(String moviesResultJson) throws JSONException {

        final String STATUS_CODE = "status_code";
        final String OWN_RESULT = "results";

        JSONObject trailerJson = new JSONObject(moviesResultJson);

        Trailer[] parsedTrailers = null;
        if(trailerJson.has(STATUS_CODE)) {
            int errorCode = trailerJson.getInt(STATUS_CODE);

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

        JSONArray trailersArray = trailerJson.getJSONArray(OWN_RESULT);
        parsedTrailers = new Trailer[trailersArray.length()];
        for(int i = 0; i < trailersArray.length(); i++) {
            JSONObject trailerObject =  trailersArray.getJSONObject(i);

            Trailer trailer = new Trailer();
            trailer.setKey(trailerObject.getString("key"));
            trailer.setName(trailerObject.getString("name"));
            trailer.setSite(trailerObject.getString("site"));

            parsedTrailers[i] = trailer;
        }

        return parsedTrailers;
    }

    public static Review[] getReviewsFromJson(String response) throws JSONException{
        final String STATUS_CODE = "status_code";
        final String OWN_RESULT = "results";

        JSONObject reviewJson = new JSONObject(response);

        Review[] parsedReviews = null;
        if(reviewJson.has(STATUS_CODE)) {
            int errorCode = reviewJson.getInt(STATUS_CODE);

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

        JSONArray jsonArray = reviewJson.getJSONArray(OWN_RESULT);
        parsedReviews = new Review[jsonArray.length()];
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject trailerObject =  jsonArray.getJSONObject(i);

            Review review = new Review();
            review.setAuthor(trailerObject.getString("author"));
            review.setContent(trailerObject.getString("content"));

            parsedReviews[i] = review;
        }

        return parsedReviews;
    }
}
