package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReviewActivityFragment extends Fragment {
    private String id;
    private String[] mReviewsArray;
    private ArrayList<String> arrayListReview;
    private ArrayAdapter<String> mReviewAdapter;
    private JSONObject[] mJsonListReviews;
    private Movies mMoviedReviewId = new Movies();
    private ParseReviews mReviewsData = new ParseReviews();

    public ReviewActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_review, container, false);

        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        String[] trailerList = {"Review 1", "Review 2", "Review 3"};

        if(intent != null && intent.hasExtra(Constants.MOV_ID)) {
            id = intent.getStringExtra(Constants.MOV_ID);
            arrayListReview = new ArrayList<String>(Arrays.asList(trailerList));
            mReviewAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_review, R.id.text_review_id, arrayListReview);
            ListView listviewReviews = (ListView) rootView.findViewById(R.id.listview_review_id);
            listviewReviews.setAdapter(mReviewAdapter);

            if (isNetworkAvailable()) {

                if (savedInstanceState != null) {

                } else {
                    FetchReviewsTask test = new FetchReviewsTask();
                    test.execute("reviews");

                }
            } else {

                Toast.makeText(getActivity(), R.string.network_unavailbe, Toast.LENGTH_LONG).show();

            }

        }

        return rootView;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }





    public String[] getReviews(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.


        //First node one API. this retrives result LIST
        JSONObject resultsJson = new JSONObject(movieJsonStr);
        JSONArray reviewArray = resultsJson.getJSONArray(Constants.MOV_RESULTS);

        // MOV returns list of reviews from themoviedb API

        String[] arrayReview = new String[reviewArray.length()];
        mReviewsArray = new String[reviewArray.length()];
        mJsonListReviews = new JSONObject[reviewArray.length()];
        String content;


        for(int i = 0; i < reviewArray.length(); i++) {


            // Get the JSON object representing the movie
            JSONObject review = reviewArray.getJSONObject(i);

            content = review.getString(Constants.REVIEW_CONTENT);



            arrayReview[i] = content;

        }



        return arrayReview;

    }



    public class FetchReviewsTask extends AsyncTask<String, Void, Reviews[]> {

        public final String TAG = FetchReviewsTask.class.getSimpleName();
        public ParseMovies mydata;

        @Override
        protected Reviews[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            Reviews[] reviews;

            if (params.length == 0) {
                return null;
            }


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;


            try {

                // Construct the URL for the Themoviedb query


                Uri.Builder uriBuild = Uri.parse(Constants.REVIEW_VIDEO_URL).buildUpon()
                        .appendPath(id)
                        .appendPath(params[0])
                        .appendQueryParameter(Constants.API_KEY_PARAM, Constants.API_KEY);

                URL url = new URL(uriBuild.toString());


                // Create the request to Movie Api, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

                //Log.v(TAG, "Themomovies JSON String: " + moviesJsonStr);
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                reviews = new Reviews[20];
                return reviews = mReviewsData.getReviewsObject(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //This will only happen if there was an error getting or parsing the movies.
            return null;
        }

        @Override
        protected void onPostExecute(Reviews[] result) {
            super.onPreExecute();
            //reviews fetch Implementation
            String[] resultStr = new String[result.length];
            for(int i = 0; i < result.length; i++){
                resultStr[i]= result[i].getmContent();
            }

            if(result != null){

                mReviewAdapter.clear();
                mReviewAdapter.addAll(resultStr);
            }
        }

    }
}


