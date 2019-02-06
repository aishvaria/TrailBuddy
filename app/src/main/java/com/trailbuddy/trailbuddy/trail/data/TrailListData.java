package com.trailbuddy.trailbuddy.trail.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TrailListData {

    @SerializedName("trails")
    public ArrayList<TrailListItem> trailListItems;

    @SerializedName("success")
    public String success;

    public static final List<TrailListItem> ITEMS = new ArrayList<TrailListItem>();

    public static void addAll(List<TrailListItem> trailListItems) {
        ITEMS.addAll(trailListItems);
    }

    public static void clearItems() {
        ITEMS.clear();
    }
}
