package com.trailbuddy.trailbuddy.trail.firestore;

import com.trailbuddy.trailbuddy.trail.data.TrailDetails;

public interface FirestoneCallbacks {
    void onFetchComplete(TrailDetails trailDetails);
}
