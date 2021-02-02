package com.tue.yuni.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Review model as presented in the database
 */
public class Review implements Parcelable {
    /**
     * Unique identification number
     */
    public int id;
    /**
     * Textual review
     */
    public String text;
    /**
     * Numerical rating
     */
    public float rating;

    /**
     * @param ID     ID
     * @param text   Textual review
     * @param rating Numerical rating
     */
    public Review(int ID, String text, float rating) {
        this.id = ID;
        this.text = text;
        this.rating = rating;
    }

    /*
     * Parcelable implementation
     */

    /**
     * @see Parcelable
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @see Parcelable
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
        dest.writeFloat(rating);
    }

    /**
     * Parcelable constructor
     *
     * @param in Parcel
     */
    protected Review(Parcel in) {
        this.id = in.readInt();
        this.text = in.readString();
        this.rating = in.readFloat();
    }

    /**
     * @see android.os.Parcelable.Creator
     */
    public static final Creator<Review> CREATOR = new Creator<Review>() {

        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        /**
         * @see android.os.Parcelable.Creator
         */
        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
