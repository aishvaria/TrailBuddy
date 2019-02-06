package com.trailbuddy.trailbuddy.trail.firestore;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.trail.data.TrailDetails;

public class FirestoreUtils {

    public static void updateTrailDetailsInFirestone(String trailId, TrailDetails trailDetails) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constants.FIRESTONE_COLLECTION_PATH).document(trailId).set(trailDetails, SetOptions.merge());
    }

    public static void getTrailDetailsFromFirestone(String trailId, final FirestoneCallbacks firestoneCallbacks) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(Constants.FIRESTONE_COLLECTION_PATH).document(trailId);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                TrailDetails trailDetails = documentSnapshot.toObject(TrailDetails.class);
                firestoneCallbacks.onFetchComplete(trailDetails);
            }
        });
    }
}
