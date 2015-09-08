package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsActivityFragment extends Fragment {

    private ImageView img;
    private String url;
    private String title;
    private String date_release;
    private String vote_avg;
    private String overview;

    public MovieDetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        if(intent != null && intent.hasExtra(Constants.EXTRA_TEXT_MOV)) {
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

        }

        return rootView;
    }

    public View getView(View convertView, ViewGroup parent) {

        ImageView img;
        if (convertView == null) {

            img = new ImageView(getActivity().getBaseContext());

            convertView = img;
            img.setPadding(0, 0, 0, 0);
        } else {
            img = (ImageView) convertView;
        }



        Picasso.with(getActivity().getBaseContext())

                .load(url)
                .resize(900, 800)
                .centerInside()
                .into(img);

        return convertView;
    }


}
