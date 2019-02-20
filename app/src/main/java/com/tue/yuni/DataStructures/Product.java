package com.tue.yuni.DataStructures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Product implements Parcelable {
    public int id;
    public String name;
    public float rating;
    public float price;
    public int availability;
    public int picture;
    public List<Review> reviews;

    public Product(int ID, String name, float rating, int availability, float price) {
        this.id = ID;
        this.name = name;
        this.rating = rating;
        this.availability = availability;
        this.price = price;
    }

    public Product(int ID, String name, float rating, int availability, float price,int picture, List<Review> reviews) {
        this.id = ID;
        this.name = name;
        this.rating = rating;
        this.availability = availability;
        this.price = price;
        this.picture = picture;
        this.reviews = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeFloat(rating);
        dest.writeFloat(price);
        dest.writeInt(availability);
        dest.writeInt(picture);
        dest.writeTypedList(reviews);
    }

    protected Product(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.rating = in.readFloat();
        this.price = in.readFloat();
        this.availability = in.readInt();
        this.picture = in.readInt();
        in.readTypedList(reviews, Review.CREATOR);
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
