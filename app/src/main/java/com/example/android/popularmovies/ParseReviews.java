package com.example.android.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by hamilton.freitas on 2015-09-17.
 */
public class ParseReviews {

    private Reviews mReviews;
    private Reviews[] mReviewsArray;
    private JSONObject[] mJsonListReviews;

    public Reviews[] getReviewsObject (String reviewJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.


        //First node one API. this retrives result LIST
        JSONObject resultsJson = new JSONObject(reviewJsonStr);
        JSONArray reviewsArray = resultsJson.getJSONArray(Constants.MOV_RESULTS);

        // MOV returns list of results from themoviedb API

        mReviewsArray = new Reviews[reviewsArray.length()];

        mJsonListReviews = new JSONObject[reviewsArray.length()];


        for(int i = 0; i < reviewsArray.length(); i++) {

            mReviews = new Reviews();

            // Get the JSON object representing the movie
            JSONObject review = reviewsArray.getJSONObject(i);

            mReviews.setmIdreviews(review.getString(Constants.REVIEW_ID));
            mReviews.setmContent(review.getString(Constants.REVIEW_CONTENT));
            mReviews.setmAuthor(review.getString(Constants.REVIEW_AUTHOR));
            mReviews.setmUrl(review.getString(Constants.REVIEW_URL));




            mReviewsArray[i] = mReviews;

        }

        return mReviewsArray;

    }
}
