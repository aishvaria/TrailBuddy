package com.trailbuddy.trailbuddy.trail.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.trailbuddy.trailbuddy.R;
import com.trailbuddy.trailbuddy.app.Constants;
import com.trailbuddy.trailbuddy.compass.CompassActivity;
import com.trailbuddy.trailbuddy.login.LoginActivity;
import com.trailbuddy.trailbuddy.login.User;
import com.trailbuddy.trailbuddy.notes.ui.TrailNotesActivity;
import com.trailbuddy.trailbuddy.trail.adapters.TrailListAdapter;
import com.trailbuddy.trailbuddy.trail.adapters.TrailListAdapter.OnListInteractionListener;
import com.trailbuddy.trailbuddy.trail.data.LocationCoordinates;
import com.trailbuddy.trailbuddy.trail.data.TrailListData;
import com.trailbuddy.trailbuddy.trail.data.TrailListItem;
import com.trailbuddy.trailbuddy.trail.network.FetchAndLoadTrailListData;

public class TrailListActivity extends AppCompatActivity implements OnListInteractionListener {

    private User appUser;
    private Toolbar toolbar;
    private FusedLocationProviderClient fusedLocationClient;
    private static String toolbarTitle = "";
    private TrailListAdapter trailListRecyclerViewAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_list);
        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        appUser = (User) getIntent().getSerializableExtra(Constants.APP_USER_TAG);
        toolbarTitle = "Hello, " + appUser.displayName;
        toolbar.setTitle(toolbarTitle);

        progressBar = findViewById(R.id.main_progress_bar);
        showProgressBar(true);

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        trailListRecyclerViewAdapter = new TrailListAdapter(TrailListData.ITEMS, this);
        recyclerView.setAdapter(trailListRecyclerViewAdapter);

        FloatingActionButton fab = findViewById(R.id.place_search_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open place search
                Intent intent;
                try {
                    intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(TrailListActivity.this);
                    startActivityForResult(intent, Constants.SEARCH_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        showTrailListOnScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_account:
                goToLoginActivity(appUser);
                return true;

            case R.id.action_compass:
                goToCompassActivity();
                return true;

            case R.id.action_notes:
                goToTrailNotesActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.SEARCH_REQUEST_CODE && resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            if (place != null) {
                LocationCoordinates locationCoordinates = new LocationCoordinates();
                LatLng latLng = place.getLatLng();
                locationCoordinates.latitude = String.valueOf(latLng.latitude);
                locationCoordinates.longitude = String.valueOf(latLng.longitude);
                loadTrailListData(locationCoordinates);
                toolbarTitle = place.getName().toString();
                toolbar.setTitle(toolbarTitle);
            }
        }
    }

    private void showTrailListOnScreen() {
        if (hasLocationPermission())
            fetchLocationAndAddTrailListFragment();
        else
            requestLocationPermissions();
    }

    private void loadTrailListData(LocationCoordinates locationCoordinates) {
        new FetchAndLoadTrailListData(this, trailListRecyclerViewAdapter, locationCoordinates).execute();
    }

    private void goToTrailDetailActivity(TrailListItem trailListItem) {
        Intent intent = new Intent(this, TrailDetailActivity.class);
        intent.putExtra(Constants.TRAIL_ITEM_BUNDLE_TAG, trailListItem);
        intent.putExtra(Constants.APP_USER_TAG, appUser);
        startActivity(intent);
    }

    private void goToLoginActivity(User appUser) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(Constants.APP_USER_TAG, appUser);
        startActivity(intent);
    }

    private void goToCompassActivity() {
        Intent intent = new Intent(this, CompassActivity.class);
        startActivity(intent);
    }

    private void goToTrailNotesActivity() {
        Intent intent = new Intent(this, TrailNotesActivity.class);
        startActivity(intent);
    }

    @SuppressLint("MissingPermission")
    private void fetchLocationAndAddTrailListFragment() {
        fusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location location = task.getResult();
                            LocationCoordinates locationCoordinates = new LocationCoordinates();
                            locationCoordinates.longitude = String.valueOf(location.getLongitude());
                            locationCoordinates.latitude = String.valueOf(location.getLatitude());
                            loadTrailListData(locationCoordinates);
                        }
                    }
                });
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                Constants.LOCATION_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Constants.LOCATION_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocationAndAddTrailListFragment();
                } else {
                    Toast.makeText(this, "Location permission is required to continue using this TrailBuddy", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }

    @Override
    public void onItemClick(TrailListItem item) {
        goToTrailDetailActivity(item);
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }
}
