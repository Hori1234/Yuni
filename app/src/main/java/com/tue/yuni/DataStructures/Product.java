package com.tue.yuni.DataStructures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Product implements Parcelable {
    public int ID;
    public String Name;
    public float Rating;
    public float Price;
    public int Availability;
    public int Picture;
    public List<Review> Reviews;

    public Product(int ID, String name, float rating, int availability, float price) {
        this.ID = ID;
        Name = name;
        Rating = rating;
        Availability = availability;
        Price = price;
    }

    public Product(int ID, String name, float rating, int availability, float price,int picture, List<Review> reviews) {
        this.ID = ID;
        Name = name;
        Rating = rating;
        Availability = availability;
        Price = price;
        Picture = picture;
        Reviews = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Name);
        dest.writeFloat(Rating);
        dest.writeFloat(Price);
        dest.writeInt(Availability);
        dest.writeInt(Picture);
        dest.writeTypedList(Reviews);
    }

    protected Product(Parcel in) {
        this.ID = in.readInt();
        this.Name = in.readString();
        this.Rating = in.readFloat();
        this.Price = in.readFloat();
        this.Availability = in.readInt();
        this.Picture = in.readInt();
        in.readTypedList(Reviews, Review.CREATOR);
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
