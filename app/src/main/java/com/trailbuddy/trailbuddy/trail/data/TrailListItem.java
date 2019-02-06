package com.trailbuddy.trailbuddy.trail.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TrailListItem implements Serializable {

    @SerializedName("id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("summary")
    public String summary;

    @SerializedName("difficulty")
    public String difficulty;

    @SerializedName("stars")
    public String stars;

    @SerializedName("location")
    public String location;

    @SerializedName("length")
    public String length;

    @SerializedName("conditionStatus")
    public String conditionStatus;

    @SerializedName("conditionDetails")
    public String conditionDetails;

    @SerializedName("imgMedium")
    public String imgMedium;

    @SerializedName("low")
    public String altitudeLow;

    @SerializedName("high")
    public String altitudeHigh;

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("longitude")
    public String longitude;
}
