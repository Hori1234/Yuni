package com.tue.yuni.DataStructures;

import android.os.Parcel;
import android.os.Parcelable;

public class Review implements Parcelable {
    public int ID;
    public String Text;
    public float Rating;

    public Review(int ID, String text, float rating) {
        this.ID = ID;
        Text = text;
        Rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Text);
        dest.writeFloat(Rating);
    }

    protected Review(Parcel in) {
        this.ID = in.readInt();
        this.Text = in.readString();
        this.Rating = in.readFloat();
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
