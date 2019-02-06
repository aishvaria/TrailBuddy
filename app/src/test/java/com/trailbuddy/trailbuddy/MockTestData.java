package com.trailbuddy.trailbuddy;

import android.content.Intent;
import android.net.Uri;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.trailbuddy.trailbuddy.login.User;
import com.trailbuddy.trailbuddy.notes.data.Note;
import com.trailbuddy.trailbuddy.reviews.data.Review;
import com.trailbuddy.trailbuddy.trail.data.TrailDetails;
import com.trailbuddy.trailbuddy.trail.data.TrailListItem;
import com.trailbuddy.trailbuddy.trail.ui.TrailListActivity;

import java.util.ArrayList;
import java.util.List;

public class MockTestData {

    public static User getMockUser() {
        User user = new User();
        user.displayName = getMockUsername();
        user.UID = "xxxx";
        return user;
    }

    public static TrailListItem getMocktrailListItem() {
        TrailListItem trailListItem = new TrailListItem();
        trailListItem.id = "7000130";
        trailListItem.name = getMockTrailname();
        trailListItem.summary = "A must-do hike for Boulder locals and visitors alike!";
        trailListItem.stars = "4.5";
        trailListItem.location = "Boulder, Colorado";
        trailListItem.length = "5.7";
        trailListItem.imgMedium = "https:\\/\\/cdn-files.apstatic.com\\/hike\\/7005382_medium_1435421346.jpg";
        trailListItem.altitudeHigh = "8370";
        trailListItem.altitudeLow = "6113";
        trailListItem.latitude = "39.9787";
        trailListItem.longitude = "-105.2755";
        return trailListItem;
    }

    public static String getMockTrailname() {
        return "Bear Peak";
    }

    public static String getMockUsername() {
        return "Gayatri Pise";
    }

    public static Intent getMockFeedbackIntent() {
        String mailto = "mailto:trailbuddy780@gmail.com" +
                "?cc=" + "gayatri.pise@gmail.com;" + "laturkaraishvaria@gmail.com" +
                "&subject=" + Uri.encode("Feedback for TrailBuddy from " + getMockUsername()) +
                "&body=" + Uri.encode("Thank you for using TrailBuddy.\n\nPlease write your feedback below.");

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));
        return emailIntent;
    }

    public static Intent getMockPlaceSearchIntent(TrailListActivity trailListActivity) {
        try {
            return new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .build(trailListActivity);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TrailDetails getMockTrailDetails() {
        TrailDetails trailDetails = new TrailDetails();

        ArrayList<Review> reviews = new ArrayList<>();
        ArrayList<String> photos = new ArrayList<>();
        photos.add("test_photo_url");

        reviews.add(getMockReview());

        trailDetails.photos = photos;
        trailDetails.reviews = reviews;
        return trailDetails;
    }

    public static List<Note> getMockNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(getMockNote());
        return notes;
    }

    public static List<TrailListItem> getMockTrailsList() {
        List<TrailListItem> trailListItems = new ArrayList<>();
        trailListItems.add(getMocktrailListItem());
        return trailListItems;
    }

    public static Note getMockNote() {
        Note note = new Note(getMockNoteText());
        return note;
    }

    public static String getMockNoteText() {
        return "Test note";
    }

    public static Review getMockReview() {
        Review review = new Review();
        review.name = getMockUsername();
        review.text = getMockReviewText();
        return review;
    }

    public static String getMockReviewText() {
        return "Test review";
    }

}

