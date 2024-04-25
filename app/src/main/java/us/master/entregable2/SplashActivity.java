package us.master.entregable2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import us.master.entregable2.entities.TripFunctionalities;
import us.master.entregable2.services.FirebaseDatabaseService;

public class SplashActivity extends AppCompatActivity {

    private final int DURACION=1000;
    private boolean isGeneratingData = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DatabaseReference tripsRef = FirebaseDatabaseService.getServiceInstance().getTrips();

        tripsRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Log.d("FirebaseDataAccess ", "Success");
                if (!dataSnapshot.exists()) { // Check if the DataSnapshot is empty
                    TripFunctionalities.generateTripData();
                }
                proceedToNextActivity();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void proceedToNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isGeneratingData = false;
                Log.d("FirebaseDataAccess ", "Proceed");
                TripFunctionalities.readTripData();
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, DURACION);
    }
}