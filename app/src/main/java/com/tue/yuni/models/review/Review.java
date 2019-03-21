package com.tue.yuni.models.review;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class Review {

    protected final int id;
    protected final float rating;
    protected final String description;
    protected final String createdAt;

    /**
     * @param id          Id
     * @param rating      Rating
     * @param description Description
     * @param createdAt   Created at
     */
    public Review(int id, float rating, String description, String createdAt) {
        this.id = id;
        this.rating = rating;
        this.description = description;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public static class CustomComparator implements Comparator<Review> {
        @Override
        public int compare(Review o1, Review o2) {
            //2019-03-15T20:28:53+01:00
            DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                Date d1 = simpleDateFormat.parse(o1.getCreatedAt());
                Date d2 = simpleDateFormat.parse(o2.getCreatedAt());
                if (d1.after(d2)) return -1;
                else if (d1.before(d2)) return 1;
            }
            catch (Exception e) {

            }

            return 0;
        }
    }
}
