package com.example.android.popularmovies;

import android.content.ActivityNotFoundException;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
public class MovieDetailsActivityFragment extends Fragment {

    private String id;
    private ImageView img;
    private ArrayList<String> arrayList;
    private String url;
    private String title;
    private String date_release;
    private String vote_avg;
    private String overview;
    private ArrayAdapter<String> mTrailerlist;
    private String[] keyArray;
    private String[] mTraillersArray;
    private JSONObject[] mJsonListTrailers;
    private ListView listView;




    public MovieDetailsActivityFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        String[] trailerList = {};


        if(intent != null && intent.hasExtra(Constants.MOV_ID)) {
            id = intent.getStringExtra(Constants.MOV_ID);
            url = intent.getStringExtra(Constants.EXTRA_TEXT_MOV);
            title = intent.getStringExtra(Constants.EXTRA_ORIGINAL_TITLE);
            date_release = intent.getStringExtra(Constants.EXTRA_RELEASE_DATE);
            vote_avg = intent.getStringExtra(Constants.EXTRA_VOTE_COUNT);
            overview = intent.getStringExtra(Constants.EXTRA_OVERVIEW);
            ((TextView)rootView.findViewById(R.id.textdetails)).setText(title);
            ((TextView)rootView.findViewById(R.id.date_id)).setText(date_release);
            ((TextView)rootView.findViewById(R.id.textdetails)).setText(title);
            ((TextView)rootView.findViewById(R.id.avg_id)).setText(vote_avg);
            ((TextView)rootView.findViewById(R.id.overview_id)).setText(overview);
            img = (ImageView)rootView.findViewById(R.id.imgitem);
            Picasso.with(getActivity().getBaseContext())
                    .load(url)
                    .resize(700, 350)
                    .centerInside()
                    .into(img);

            arrayList = new ArrayList<String>(Arrays.asList(trailerList));
            mTrailerlist = new ArrayAdapter<String>(getActivity(), R.layout.list_item_trailers,
                    R.id.list_item_trailer_textview, arrayList);
            listView = (ListView) rootView.findViewById(R.id.listview_id);
            listView.setAdapter(mTrailerlist);

            if (isNetworkAvailable()) {

                if(savedInstanceState != null){
                    arrayList = savedInstanceState.getStringArrayList(Constants.DETAILS_TEXT_LIST);
                    mTrailerlist.addAll(arrayList);
                    keyArray = savedInstanceState.getStringArray(Constants.DETAILS_KEY);
                    id = savedInstanceState.getString(Constants.DETAILS_MOV_ID);
                }else{
                    FetchTrailersTask test = new FetchTrailersTask();
                    test.execute("videos");

                }
            }else{

                Toast.makeText(getActivity(), R.string.network_unavailbe, Toast.LENGTH_LONG).show();

            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String trailer = mTrailerlist.getItem(position);
                    String keyInd = keyArray[position];

                    //Adapted solution from stackoverflow andorid-youtube-app-play-video-intent
                    //I did mine, but the solution below is the best to either play by browser or youtube app.
                    try{

                        Intent intent = new Intent (Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("vdn.youtube:" + keyInd));
                        startActivity(intent);

                    }catch (ActivityNotFoundException ex){
                        Intent intent = new Intent (Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + keyInd));
                        startActivity(intent);

                    }


                }
            });


        }



        TextView textview = (TextView)rootView.findViewById(R.id.review_label_id);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), ReviewActivity.class);
                intent1.putExtra(Constants.MOV_ID,id);
                startActivity(intent1);
            }
        });




        return rootView;
    }

    @Override
    public void onSaveInstanceState (Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(Constants.DETAILS_TEXT_LIST, arrayList);
        outState.putStringArray(Constants.DETAILS_KEY, keyArray);
        outState.putString(Constants.DETAILS_MOV_ID, id);

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




    public String[] getTrailers(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.


        //First node one API. this retrives result LIST
        JSONObject resultsJson = new JSONObject(movieJsonStr);
        JSONArray trailersArray = resultsJson.getJSONArray(Constants.MOV_RESULTS);

        // MOV returns list of trailers from themoviedb API

        String[] arrayTrailer = new String[trailersArray.length()];
        mTraillersArray = new String[trailersArray.length()];
        mJsonListTrailers = new JSONObject[trailersArray.length()];
        String key;

        for(int i = 0; i < trailersArray.length(); i++) {


            // Get the JSON object representing the movie
            JSONObject trail = trailersArray.getJSONObject(i);

            key = trail.getString(Constants.TRAILER_KEY);



            arrayTrailer[i] = key;

        }



        return arrayTrailer;

    }

//    public View getView(View convertView, ViewGroup parent) {
//
//        ImageView img;
//        if (convertView == null) {
//
//            img = new ImageView(getActivity().getBaseContext());
//
//            convertView = img;
//            img.setPadding(0, 0, 0, 0);
//        } else {
//            img = (ImageView) convertView;
//        }
//
//
//
//        Picasso.with(getActivity().getBaseContext())
//
//                .load(url)
//                .resize(900, 800)
//                .centerInside()
//                .into(img);
//
//        return convertView;
//    }

    public class FetchTrailersTask extends AsyncTask<String, Void, String[]> {

        public final String TAG = FetchTrailersTask.class.getSimpleName();
        public ParseMovies mydata;

        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            String[] trailers;

            if (params.length == 0) {
                return null;
            }


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;


            try {

                // Construct the URL for the Themoviedb query

                Uri.Builder uriBuild = Uri.parse(Constants.TRAILER_VIDEO_URL).buildUpon()
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

                return trailers = getTrailers(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //This will only happen if there was an error getting or parsing the movies.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPreExecute();
            //Trailers fetch Implementation

//            if(result != null){
//
//                mTrailerlist.clear();
//
//                for(String movieTrailerStr: result){
//                    mTrailerlist.add(movieTrailerStr);
//                }
//            }
            int count = 1;
            String trailerLink = "Trailer";
            mTrailerlist.clear();
            keyArray = new String[result.length];
            int ind = 0;
            for(String movieTrailerStr: result ){

                mTrailerlist.add(trailerLink + " " + count);
                ++count;

                keyArray[ind] = movieTrailerStr;
                ++ind;

            }

        }
    }




}
