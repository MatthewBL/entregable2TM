package us.master.entregable1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TripList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TripAdapter adapter;
    private List<Trip> tripList;
    private TextView filtrarTextView;
    private ImageView filterIcon;
    private Switch columnasSwitch;
    String startDate = "-";
    String endDate = "-";
    int minPrice = 0;
    int maxPrice = 1000;
    Boolean columnLayout = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        boolean displaySelected = getIntent().getBooleanExtra("displaySelected", false);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if (displaySelected) {
            tripList = Trip.getSelectedTripList();
        } else {
            tripList = Trip.getTripList();
        }

        columnasSwitch = findViewById(R.id.columnasSwitch);

        adapter = new TripAdapter(tripList, displaySelected, columnasSwitch.isChecked());
        recyclerView.setAdapter(adapter);

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

                            adapter = new TripAdapter(tripList, displaySelected, columnLayout);
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
                adapter = new TripAdapter(tripList, displaySelected, isChecked);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.getDefault());

        List<Trip> filteredTripList = tripList.stream()
                .filter(trip -> {
                    if (!"-".equals(startDate)) {
                        try {
                            LocalDate start = LocalDate.parse(startDate, formatter);
                            return !trip.getDepartureDate().isBefore(start);
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
                            return !trip.getArrivalDate().isAfter(end);
                        } catch (DateTimeParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return true;
                })
                .filter(trip -> trip.getPrice() >= minPrice)
                .filter(trip -> trip.getPrice() <= maxPrice)
                .collect(Collectors.toList());

        adapter.updateList(filteredTripList);
    }
}