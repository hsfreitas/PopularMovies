package com.example.android.popularmovies;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hamilton.freitas on 2015-08-31.
 * Source http://iswwwup.com/t/1a89b3063f83/android-gridview-images-are-not-shown-using-picasso-lib.html
 */
public class GridViewAdapter extends BaseAdapter {

    private Context context;

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    private ArrayList<String> items;



    public GridViewAdapter(Context context, ArrayList<String> items){
        super();
        this.context = context;
        this.items = items;
    }





    @Override
    public int getCount() {
        return items.size();
    }



    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView img;
        if (convertView == null) {

            img = new ImageView(context);

            convertView = img;
            img.setPadding(0, 0, 0, 0);
        } else {
            img = (ImageView) convertView;
        }



        Picasso.with(context)

                .load(items.get(position))
                .resize(900, 800)
                .centerInside()
                .into(img);

        return convertView;
    }

}
