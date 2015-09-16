package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hamilton.freitas on 2015-09-08.
 */
public class Movies implements Parcelable {



    private String mID;
    private String mOriginal_title;
    private String mRelease_date;
    private String mVote_average;
    private String mOverview;
    private String mMovieId;
    private String[] mMovieIdArray;
    private JSONObject[] mJsonListMovies;

    public String getmRelease_date() {
        return mRelease_date;
    }

    public void setmRelease_date(String mRelease_date) {
        this.mRelease_date = mRelease_date;
    }

    public String getmMovieId() {
        return mMovieId;
    }

    public void setmMovieId(String mMovieId) {
        this.mMovieId = mMovieId;
    }

    private String mPoster_path;
    private String[] mMovielist;
    private String[] mPosterPathStr;



    public Movies(){}

    public Movies(String id, String poster, String title, String release_d, String vote_v, String overview){
        this.mID = id;
        this.mPoster_path = poster;
        this.mOriginal_title = title;
        this.mRelease_date = release_d;
        this.mVote_average = vote_v;
        this.mOverview = overview;
    }


    public Movies(Parcel in){
        this.mOriginal_title = in.readString();
        this.mRelease_date = in.readString();
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

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmPoster_path() {
        return mPoster_path;
    }

    public void setmPoster_path(String mPoster_path) {
        this.mPoster_path = mPoster_path;
    }

    public String getRelease_date() {
        return mRelease_date;
    }

    public void setRelease_date(String release_date) {
        this.mRelease_date = release_date;
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

        mMovieIdArray = new String[moviesArray.length()];
        mPosterPathStr = new String[moviesArray.length()];
        mJsonListMovies = new JSONObject[moviesArray.length()];

        for(int i = 0; i < moviesArray.length(); i++) {



            // Get the JSON object representing the movie
            JSONObject movie = moviesArray.getJSONObject(i);

            mID = movie.getString(Constants.MOV_ID);

            //This get movie poster path
            mPoster_path = movie.getString(Constants.MOV_POSTER_PATH);

            mOriginal_title = movie.getString(Constants.MOV_ORIGINAL_TITLE);

            mRelease_date = movie.getString(Constants.MOV_RELEASE_DATE);

            mVote_average = movie.getString(Constants.MOV_VOTE_AVERAGE);

            mOverview = movie.getString(Constants.MOV_OVERVIEW);

            mPosterPathStr[i] = mPoster_path;

            mJsonListMovies[i] = movie;

            mMovieId = movie.getString(Constants.MOV_ID);
            mMovieIdArray[i] = mMovieId;

        }


        for (int i = 0 ; i < mPosterPathStr.length; i++) {
            //Log.v(TAG, "Movies entry: " + s);
            mPosterPathStr[i] = "http://image.tmdb.org/t/p/w185/" + mPosterPathStr[i];
            mMovieIdArray[i] = "http://api.themoviedb.org/3/movie/"+mMovieIdArray[i]+"/videos?api_key=d3b372f7ac246b674bc0d57687d077f0";

        }
        String[] test = mMovieIdArray;

        return mPosterPathStr;

    }

    public String[] getMovies(){

        return null;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {;
        dest.writeString(this.mOriginal_title);
        dest.writeString(this.mRelease_date);
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
