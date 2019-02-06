package com.trailbuddy.trailbuddy.trail.data;

import com.trailbuddy.trailbuddy.reviews.data.Review;

import java.io.Serializable;
import java.util.ArrayList;

public class TrailDetails implements Serializable {

    public ArrayList<Review> reviews;
    public ArrayList<String> photos;
}
