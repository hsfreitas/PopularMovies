package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
public class PopMoviesFragment extends Fragment{

    public final String TAG = PopMoviesFragment.class.getSimpleName();
    public String[] movielistStr;
    private ArrayList<String> mImageList;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private String[] fromapimovi;
    private JSONObject movie;
    private String original_title = "";
    private String release_date = "";
    private JSONObject[] mLocalListmovies;
    private String video;
    private String vote_average = "";
    private String vote_count = "";
    private String overview = "";
    private Movies moviesbox;
    private String jsonAddress;
    private ArrayList<Movies> moviesDbsList = new ArrayList<>();
    private Movies mMovies = new Movies();
    private FetchMovieTask test;
    private Bundle mState = new Bundle();
    //private SharedPreferences sharedPreferences;
    //private SharedPreferences.Editor sharedPreferencesEdit;



    public PopMoviesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This is responsible to set the value menu on the fragment
        setHasOptionsMenu(true);

    }

    //This implemented method will inflate the menu of popmovies fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.popmovies_menu, menu);

    }


    //This option will select the button refresh on the fragment menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int  id = item.getItemId();

        if(R.id.action_most_popular == id){
            sortDefault();

            return true;
        }else if (R.id.action_most_rated == id){

            sortCount();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String[] imagemov = {};

        mImageList = new ArrayList<String>(Arrays.asList(imagemov));

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.popgridview_id);

        gridAdapter = new GridViewAdapter(getActivity().getApplicationContext(), mImageList);
        gridView.setAdapter(gridAdapter);

        if(isNetworkAvailable()){

            if(savedInstanceState != null){
                mImageList = savedInstanceState.getStringArrayList(Constants.MOV_STATE);
                mMovies = savedInstanceState.getParcelable(Constants.MOV_ROTATION);
                gridAdapter.setItems(mImageList);
            }else{

                sortDefault();

            }

        }else{
            Toast.makeText(getActivity(), R.string.network_unavailbe, Toast.LENGTH_LONG).show();
        }




        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();
                mLocalListmovies = mMovies.getmJsonListMovies();

                try {

                    original_title = mLocalListmovies[i].getString(Constants.MOV_ORIGINAL_TITLE);
                    release_date = mLocalListmovies[i].getString(Constants.MOV_RELEASE_DATE);
                    vote_average = mLocalListmovies[i].getString(Constants.MOV_VOTE_AVERAGE);
                    overview = mLocalListmovies[i].getString(Constants.MOV_OVERVIEW);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String imov = gridAdapter.getItem(i).toString();
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class)
                        .putExtra(Constants.EXTRA_TEXT_MOV, imov)
                        .putExtra(Constants.EXTRA_ORIGINAL_TITLE, original_title)
                        .putExtra(Constants.EXTRA_RELEASE_DATE, release_date)
                        .putExtra(Constants.EXTRA_VOTE_COUNT, vote_average)
                        .putExtra(Constants.EXTRA_OVERVIEW, overview);
                startActivity(intent);

            }
        });


        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(Constants.MOV_STATE, mImageList);
        outState.putParcelable(Constants.MOV_ROTATION, mMovies);


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

    private void sortDefault(){
        FetchMovieTask weatherTaskUpdate = new FetchMovieTask();
        String preference= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.pref_sort_vote_count),
                getString(R.string.pref_sort_default));
        weatherTaskUpdate.execute(preference);
    }

    private void sortCount(){
        FetchMovieTask weatherTaskUpdate = new FetchMovieTask();
        String preference= PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.pref_sort_default),
                getString(R.string.pref_sort_vote_count));
        weatherTaskUpdate.execute(preference);
    }

    @Override
    public void onStart() {
        super.onStart();
        //sortDefault();
    }

    /**
     * Fechting Data from MoviesDB
     */

    public class FetchMovieTask extends AsyncTask<String, Void, String[]> {

        public final String TAG = FetchMovieTask.class.getSimpleName();



        @Override
        protected String[] doInBackground(String... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.

            if(params.length == 0){
                return null;
            }


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            //String my api Key
            String apiKey = "d3b372f7ac246b674bc0d57687d077f0";


            try {

                // Construct the URL for the Themoviedb query
                // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=

                Uri.Builder uriBuild = Uri.parse(Constants.THEMOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.SORT_BY_PARAM, params[0])
                        .appendQueryParameter(Constants.API_KEY_PARAM, apiKey);

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

                jsonAddress = moviesJsonStr;

                //Log.v(TAG, "Themomovies JSON String: " + moviesJsonStr);
            } catch (IOException e) {
                Log.e(TAG, "Error ", e);
                // If the code didn't successfully get the movies data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
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
                return movielistStr = mMovies.getPosterAddress(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //This will only happen if there was an error getting or parsing the movies.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPreExecute();
            //Movie fetch Implementation TODO
            //List<String> dataFromApi = new ArrayList<String>(Arrays.asList(result));
            if(result !=null){

                mImageList = new ArrayList<String>(Arrays.asList(result));
                gridAdapter.notifyDataSetChanged();
                // New data the server.
                gridAdapter.setItems(mImageList);

            }
        }

        public String getJsonAddress() {
            return jsonAddress;
        }



    }


}
