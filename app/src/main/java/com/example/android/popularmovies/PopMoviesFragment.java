package com.example.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PopMoviesFragment extends Fragment {

    public final String TAG = PopMoviesFragment.class.getSimpleName();
    public String[] movielistStr;
    private List<String> movieList;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private  String[] fromapimovi;
    private JSONObject movie;
    private String original_title = "";
    private String release_date = "";
    private JSONObject[] listMovies;
    private String video;
    private String vote_average = "";
    private String vote_count = "";
    private String overview = "";



    public PopMoviesFragment() {
    }

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
        int id = item.getItemId();
        if(id == R.id.action_most_popular){
            FetchMovierTask test = new FetchMovierTask();
            test.execute("popularity.desc");

            return true;
        }else if(id == R.id.action_most_rated){
            FetchMovierTask test = new FetchMovierTask();
            test.execute("vote_count.desc");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         final String[] imagemov = {
                "http://image.tmdb.org/t/p/w185//qFC07nj9lWWmnbkS191AgFUth9J.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//s5uMY8ooGRZOL0oe4sIvnlTsYQO.jpg",
                "http://image.tmdb.org/t/p/w185//xxOKDTQbQo7h1h7TyrQIW7u8KcJ.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
                "http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"
        };


        movieList = new ArrayList<String>(Arrays.asList(imagemov));

        FetchMovierTask test = new FetchMovierTask();
        test.execute("popularity.desc");


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.popgridview_id);

        gridAdapter = new GridViewAdapter(getActivity().getApplicationContext(), movieList);



        gridView.setAdapter(gridAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Toast.makeText(getActivity(), "test", Toast.LENGTH_LONG).show();


                try {
                        original_title = listMovies[i].getString(Constants.MOV_ORIGINAL_TITLE);
                        release_date = listMovies[i].getString(Constants.MOV_RELEASE_DATE);
                        vote_average = listMovies[i].getString(Constants.MOV_VOTE_AVERAGE);
                        overview = listMovies[i].getString(Constants.MOV_OVERVIEW);

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



    /**
     * Take the String representing the complete moviedb in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private String[] getPosterAddress(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.


        String poster_path;

        //First node one API. this retrives result LIST
        JSONObject resultsJson = new JSONObject(movieJsonStr);
        JSONArray moviesArray = resultsJson.getJSONArray(Constants.MOV_RESULTS);

        // MOV returns list of results from themoviedb API


        String[] posterPathStr = new String[moviesArray.length()];
        listMovies = new JSONObject[moviesArray.length()];
        for(int i = 0; i < moviesArray.length(); i++) {



            // Get the JSON object representing the movie
            JSONObject movie = moviesArray.getJSONObject(i);


            //This get movie poster path
            poster_path = movie.getString(Constants.MOV_POSTER_PATH);

            posterPathStr[i] = poster_path ;

            listMovies[i] = movie;

        }


        for (int i = 0 ; i < posterPathStr.length; i++) {
            //Log.v(TAG, "Movies entry: " + s);
            posterPathStr[i] = "http://image.tmdb.org/t/p/w185/" + posterPathStr[i];


        }

        return posterPathStr;

    }



    public class FetchMovierTask extends AsyncTask<String, Void, String[]> {

        public final String TAG = FetchMovierTask.class.getSimpleName();



        public String jsonAddress;

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
            String apiKey = "";


            try {
                // Construct the URL for the Themoviedb query
                // http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=



                Uri.Builder uriBuild = Uri.parse(Constants.THEMOVIEDB_BASE_URL).buildUpon()
                        .appendQueryParameter(Constants.SORT_BY_PARAM, params[0])
                        .appendQueryParameter(Constants.API_KEY_PARAM, apiKey);


                URL url = new URL(uriBuild.toString());

                //Log.v(TAG, "Build URI " + uriBuild.toString());

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
                return movielistStr = getPosterAddress(moviesJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
             //This will only happen if there was an error getting or parsing the movies.
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPreExecute();

            //List<String> dataFromApi = new ArrayList<String>(Arrays.asList(result));
            if(result !=null){
                //movieList.clear();
                movieList = new ArrayList<String>(Arrays.asList(result));
                gridAdapter.notifyDataSetChanged();
                // New data the server.
                gridAdapter.setItems(movieList);

            }
        }

        public String getJsonAddress() {
            return jsonAddress;
        }
    }




}
