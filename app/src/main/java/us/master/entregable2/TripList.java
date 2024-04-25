package us.master.entregable2;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import us.master.entregable2.entities.Trip;
import us.master.entregable2.entities.TripFunctionalities;
import us.master.entregable2.entities.User;
import us.master.entregable2.services.FirebaseDatabaseService;
import us.master.entregable2.services.PropertiesManager;
import us.master.entregable2.services.UserCallback;

import com.google.android.gms.maps.model.LatLng;

public class TripList extends AppCompatActivity {
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 0x123;
    Location userLocation;
    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private List<Trip> tripList = new ArrayList<>();
    private TextView filtrarTextView;
    private ImageView filterIcon;
    private Switch columnasSwitch;
    String startDate = "-";
    String endDate = "-";
    int minPrice = 0;
    int maxPrice = 1000;
    boolean airportCheckBox = false;
    Boolean columnLayout = false;

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        TextView snackbarText = findViewById(R.id.snackbar_text);

        progressBar = findViewById(R.id.progressBar);

        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Snackbar.make(snackbarText, R.string.location_rationale, Snackbar.LENGTH_LONG)
                        .setAction(R.string.location_rationale_ok, v -> ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSIONS_REQUEST_CODE))
                        .show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSIONS_REQUEST_CODE);
            }
        }
        else {
            startLocationService();
        }

        String display = getIntent().getStringExtra("display");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        updateTripList(display);

        columnasSwitch = findViewById(R.id.columnasSwitch);

        filtrarTextView = findViewById(R.id.filtrarTextView);
        filterIcon = findViewById(R.id.filterIcon);

        // Define the ActivityResultLauncher
        ActivityResultLauncher<Intent> filterActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            startDate = data.getStringExtra("startDate");
                            endDate = data.getStringExtra("endDate");
                            minPrice = data.getIntExtra("minPrice", 0);
                            maxPrice = data.getIntExtra("maxPrice", 0);
                            airportCheckBox = data.getBooleanExtra("airportCheckBox", false);

                            adapter = new TripAdapter(tripList, display, columnLayout);
                            applyFilters();
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });

        View.OnClickListener filterClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TripList.this, FilterActivity.class);
                intent.putParcelableArrayListExtra("tripList", (ArrayList<? extends Parcelable>) tripList);
                // Add the current filter settings to the intent
                intent.putExtra("startDate", startDate);
                intent.putExtra("endDate", endDate);
                intent.putExtra("minPrice", minPrice);
                intent.putExtra("maxPrice", maxPrice);
                intent.putExtra("airportCheckBox", airportCheckBox);
                filterActivityResultLauncher.launch(intent);
            }
        };

        filtrarTextView.setOnClickListener(filterClickListener);
        filterIcon.setOnClickListener(filterClickListener);

        columnasSwitch = findViewById(R.id.columnasSwitch);
        columnasSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    recyclerView.setLayoutManager(new GridLayoutManager(TripList.this, 2));
                } else {
                    recyclerView.setLayoutManager(new GridLayoutManager(TripList.this, 1));
                }
                columnLayout = isChecked;
                adapter = new TripAdapter(tripList, display, isChecked);
                applyFilters();
                recyclerView.setAdapter(adapter);
            }
        });

        // Set the initial state of the RecyclerView
        if (columnasSwitch.isChecked()) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        }
        columnLayout = columnasSwitch.isChecked();
    }

    private void applyFilters() {
        // Show the ProgressBar and hide the RecyclerView
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault());

        // Define the needed variables for the airport distance filter before the stream
        Properties properties = PropertiesManager.loadProperties(this);
        String apiKey = properties.getProperty("googleMapsApiKey");

        // Filter the trips based on the non-asynchronous conditions
        List<Trip> filteredTripList = tripList.stream()
                .filter(trip -> {
                    if (!"-".equals(startDate)) {
                        try {
                            LocalDate start = LocalDate.parse(startDate, formatter);
                            return !TripFunctionalities.obtainDepartureDateType(trip).isBefore(start);
                        } catch (DateTimeParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                })
                .filter(trip -> {
                    if (!"-".equals(endDate)) {
                        try {
                            LocalDate end = LocalDate.parse(endDate, formatter);
                            return !TripFunctionalities.obtainArrivalDateType(trip).isAfter(end);
                        } catch (DateTimeParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                })
                .filter(trip -> trip.getPrice() >= minPrice)
                .filter(trip -> trip.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        // Build a dictionary of CompletableFuture of LatLng, one for each trip in filteredTripList
        Map<Trip, CompletableFuture<LatLng>> futures = new HashMap<>();
        for (Trip trip : filteredTripList) {
            futures.put(trip, CompletableFuture.supplyAsync(() -> TripFunctionalities.obtainStartPointLatLng(trip, apiKey)));
        }

        // Wait for the CompletableFuture to finish. Then filter out those that aren't within 30 kms from the user
        CompletableFuture.allOf(futures.values().toArray(new CompletableFuture[0])).join();
        List<Trip> finalTripList = new ArrayList<>();
        for (Map.Entry<Trip, CompletableFuture<LatLng>> entry : futures.entrySet()) {
            LatLng startPointLatLng = entry.getValue().join();
            if (airportCheckBox && getDistanceInKm(userLocation, startPointLatLng) <= 30) {
                finalTripList.add(entry.getKey());
            }
        }

        // Update the adapter and remove the ProgressBar
        runOnUiThread(() -> {
            adapter.updateList(finalTripList);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        });
    }

    private float getDistanceInKm(Location userLocation, LatLng startPoint) {
        float[] results = new float[1];
        Location.distanceBetween(userLocation.getLatitude(), userLocation.getLongitude(), startPoint.latitude, startPoint.longitude, results);
        return results[0] / 1000; // convert meters to kilometers
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            }
            else {
                Snackbar.make(findViewById(R.id.snackbar_text), R.string.location_denied, Snackbar.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(com.google.android.gms.location.LocationResult locationResult) {
            if (locationResult == null || locationResult.getLastLocation() == null || !locationResult.getLastLocation().hasAccuracy()) {
                return;
            }
            Log.i("Location", "Location received: " + locationResult.getLastLocation().getLatitude() + ", " + locationResult.getLastLocation().getLongitude());
            userLocation = locationResult.getLastLocation();
            Log.i("Location", "User location: " + userLocation.getLatitude() + ", " + userLocation.getLongitude());
        }
    };

    private void startLocationService() {
        Log.i("Location", "Starting location service");
        FusedLocationProviderClient locationServices = LocationServices.getFusedLocationProviderClient(this);
        com.google.android.gms.location.LocationRequest locationRequest = com.google.android.gms.location.LocationRequest.create();
        locationRequest.setInterval(3000);
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        //locationRequest.setSmallestDisplacement(10);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Log.i("Location", "Requesting location updates");
        locationServices.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public void stopService() {
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
    }

    public void updateTripList(String display) {
        FirebaseDatabaseService.getCurrentUser(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if (user != null) {
                    if (display != null && display.equals("selected")) {
                        tripList = TripFunctionalities.obtainSelectedTripList(user);
                    } else if (display != null && display.equals("bought")) {
                        tripList = TripFunctionalities.obtainBoughtTripList(user);
                    } else {
                        tripList = TripFunctionalities.obtainNonBoughtTripList(user);
                    }

                    adapter = new TripAdapter(tripList, display, columnasSwitch.isChecked());
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("TripList", "No user found");
                }
            }
        });
    }
}