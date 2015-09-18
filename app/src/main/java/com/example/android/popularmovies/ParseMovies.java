package com.example.android.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hamilton.freitas on 2015-09-15.
 */
public class ParseMovies {
    private Movies mMovie;
    private Movies[] mMovieArray;
    private String[] mPosterPathStr;
    private JSONObject[] mJsonListMovies;

    public Movies[] getPosterAddress(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.


        //First node one API. this retrives result LIST
        JSONObject resultsJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = resultsJson.getJSONArray(Constants.MOV_RESULTS);

        // MOV returns list of results from themoviedb API

        mMovieArray = new Movies[moviesArray.length()];
        mPosterPathStr = new String[moviesArray.length()];
        mJsonListMovies = new JSONObject[moviesArray.length()];


        for(int i = 0; i < moviesArray.length(); i++) {

            mMovie = new Movies();

            // Get the JSON object representing the movie
            JSONObject movie = moviesArray.getJSONObject(i);

            mMovie.setmMovieId(movie.getString(Constants.MOV_ID));
            mMovie.setmPoster_path(movie.getString(Constants.MOV_POSTER_PATH));
            mMovie.setmOriginal_title(movie.getString(Constants.MOV_ORIGINAL_TITLE));
            mMovie.setmRelease_date(movie.getString(Constants.MOV_RELEASE_DATE));
            mMovie.setmVote_average(movie.getString(Constants.MOV_VOTE_AVERAGE));
            mMovie.setmOverview(movie.getString(Constants.MOV_OVERVIEW));

            //mPosterPathStr[i] = mPoster_path;

            mJsonListMovies[i] = movie;


            mMovieArray[i] = mMovie;

        }

        return mMovieArray;

    }



}
