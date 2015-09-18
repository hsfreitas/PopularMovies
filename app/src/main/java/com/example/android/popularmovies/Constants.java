package com.example.android.popularmovies;

/**
 * Created by hamilton.freitas on 2015-09-03.
 */
public class Constants {
    public static final String MOV_RESULTS = "results";
    public static final String MOV_ID = "id";
    public static final String MOV_ORIGINAL_LANGUAGE = "original_language";
    public static final String MOV_ORIGINAL_TITLE = "original_title";
    public static final String MOV_RELEASE_DATE = "release_date";
    public static final String MOV_POSTER_PATH = "poster_path";
    public static final String MOV_TITLE = "title";
    public static final String MOV_VIDEO = "video";
    public static final String MOV_VOTE_AVERAGE = "vote_average";
    public static final String MOV_VOTE_COUNT = "vote_count";
    public static final String MOV_OVERVIEW = "overview";

    //Fetch poster movies
    public static final String THEMOVIEDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
    public static final String SORT_BY_PARAM ="sort_by";
    public static final String API_KEY_PARAM = "api_key";

    //Fetch movies trailers
    public static final String TRAILER_VIDEO_URL = "http://api.themoviedb.org/3/movie/";
    public static final String TRAILER_VIDEO_ID = "trailer_vide_id";
    //public static final String TRAILER_VIDEO_PARAM = "?";
    public static final String TRAILER_KEY = "key";


    //Fetch movies Reviews
    public static final String REVIEW_VIDEO_URL = "http://api.themoviedb.org/3/movie/";
    public static final String REVIEW_AUTHOR = "author";
    public static final String REVIEW_CONTENT = "content";
    public static final String REVIEW_URL = "url";
    public static final String REVIEW_ID = "id";

    //API KEY
    public static final String API_KEY = "";

    //Constants to intent put Extra
    public static final String EXTRA_TEXT_MOV = "extra_text_mov";
    public static final String EXTRA_ORIGINAL_TITLE = "extra_original_title";
    public static final String EXTRA_VOTE_COUNT = "extra_vote_count";
    public static final String EXTRA_RELEASE_DATE = "extra_release_date";
    public static final String EXTRA_OVERVIEW = "extra_overview";

    //Recreating Activity constants
    public static final String MOV_STATE = "mov_state";
    public static final String MOV_ROTATION = "mov_rotation";

    //Shared preferences
    public static final String SHARE_STATE_POPULARITY = "share_popularity";
    public static final int SHARE_MODE_PRIVATE = 0;

    //Movies details
    public static final String DETAILS_TEXT_LIST = "details_list";
    public static final String DETAILS_KEY = "key";
    public static final String DETAILS_MOV_ID = "id";




}
