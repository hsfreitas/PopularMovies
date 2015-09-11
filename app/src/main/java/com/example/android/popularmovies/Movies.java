package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by hamilton.freitas on 2015-09-08.
 */
public class Movies implements Parcelable {

    private String mOriginal_title;
    private String release_date;
    private String mVote_average;
    private String mOverview;
    private JSONObject[] mJsonListMovies;
    private String mPoster_path;
    private String[] mMovielist;
    private String[] mPosterPathStr;



    public Movies(){}

    public Movies(String[] posterPathStr, String title, String release_d, String vote_v, String overview){
        this.mPosterPathStr = posterPathStr;
        this.mOriginal_title = title;
        this.release_date = release_d;
        this.mVote_average = vote_v;
        this.mOverview = overview;
    }


    public Movies(Parcel in){
        this.mOriginal_title = in.readString();
        this.release_date = in.readString();
        this.mVote_average = in.readString();
        this.mOverview =in.readString();
    }

    public JSONObject[] getmJsonListMovies() {
        return mJsonListMovies;
    }

    public void setmJsonListMovies(JSONObject[] mJsonListMovies) {
        this.mJsonListMovies = mJsonListMovies;
    }

    public String[] getmMovielist() {
        return mMovielist;
    }

    public void setmMovielist(String[] mMovielist) {
        this.mMovielist = mMovielist;
    }

    public String getmOriginal_title() {
        return mOriginal_title;
    }

    public void setmOriginal_title(String mOriginal_title) {
        this.mOriginal_title = mOriginal_title;
    }

    public String getmPoster_path() {
        return mPoster_path;
    }

    public void setmPoster_path(String mPoster_path) {
        this.mPoster_path = mPoster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getmVote_average() {
        return mVote_average;
    }

    public void setmVote_average(String mVote_average) {
        this.mVote_average = mVote_average;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String[] getmPosterPathStr() {
        return mPosterPathStr;
    }

    public void setmPosterPathStr(String[] mPosterPathStr) {
        this.mPosterPathStr = mPosterPathStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public String[] getPosterAddress(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.


        //First node one API. this retrives result LIST
        JSONObject resultsJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = resultsJson.getJSONArray(Constants.MOV_RESULTS);

        // MOV returns list of results from themoviedb API


        mPosterPathStr = new String[moviesArray.length()];
        mJsonListMovies = new JSONObject[moviesArray.length()];
        for(int i = 0; i < moviesArray.length(); i++) {



            // Get the JSON object representing the movie
            JSONObject movie = moviesArray.getJSONObject(i);


            //This get movie poster path
            mPoster_path = movie.getString(Constants.MOV_POSTER_PATH);

            mPosterPathStr[i] = mPoster_path;

            mJsonListMovies[i] = movie;

        }


        for (int i = 0 ; i < mPosterPathStr.length; i++) {
            //Log.v(TAG, "Movies entry: " + s);
            mPosterPathStr[i] = "http://image.tmdb.org/t/p/w185/" + mPosterPathStr[i];


        }

        return mPosterPathStr;

    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {;
        dest.writeString(this.mOriginal_title);
        dest.writeString(this.release_date);
        dest.writeString(this.mVote_average);
        dest.writeString(this.mOverview);

    }

    public static final Parcelable.Creator<Movies> CREATOR
            = new Parcelable.Creator<Movies>() {
        public Movies createFromParcel(Parcel in) {

            return new Movies(in);
        }

        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };
}
