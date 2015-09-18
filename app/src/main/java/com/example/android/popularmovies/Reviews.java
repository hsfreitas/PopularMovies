package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hamilton.freitas on 2015-09-18.
 */
public class Reviews implements Parcelable {
    private String mIdreviews;
    private String mContent;
    private String mAuthor;
    private String mUrl;

    public Reviews(){}

    public Reviews(Parcel in){
        this.mIdreviews = in.readString();
        this.mContent = in.readString();
        this.mAuthor = in.readString();
        this.mUrl = in.readString();

    }

    public String getmIdreviews() {
        return mIdreviews;
    }

    public void setmIdreviews(String mIdreviews) {
        this.mIdreviews = mIdreviews;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mIdreviews);
        dest.writeString(this.mContent);
        dest.writeString(this.mAuthor);
        dest.writeString(this.mUrl);

    }

    public static final Parcelable.Creator<Reviews> CREATOR
            = new Parcelable.Creator<Reviews>() {
        public Reviews createFromParcel(Parcel in) {

            return new Reviews(in);
        }

        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };
}
