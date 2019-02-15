package com.tue.yuni;

import java.util.List;

public class Product {
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
}
