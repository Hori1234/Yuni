package com.tue.yuni.DataStructures;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    public int id;
    public String text;
    public float rating;

    public Review(int ID, String text, float rating) {
        this.id = ID;
        this.text = text;
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(text);
        dest.writeFloat(rating);
    }

    protected Review(Parcel in) {
        this.id = in.readInt();
        this.text = in.readString();
        this.rating = in.readFloat();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}
