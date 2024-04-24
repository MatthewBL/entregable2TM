package us.master.entregable2.services;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import us.master.entregable2.entities.FirebaseEntity;
import us.master.entregable2.entities.Trip;
import us.master.entregable2.entities.User;

public class FirebaseDatabaseService {
    public static Boolean testing = false;
    private static String userId;
    private static FirebaseDatabaseService service;
    private static FirebaseDatabase mDatabase;

    public static synchronized FirebaseDatabaseService getServiceInstance() {
        if (service == null || mDatabase == null) {
            service = new FirebaseDatabaseService();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }

        if (userId == null || userId.isEmpty()) {
            FirebaseDatabaseService.userId = LocalPreferences.getLocalUserInformationId();
        }
        return service;
    }

    public static FirebaseDatabaseService getServiceInstance(String userId) {
        FirebaseDatabaseService.userId = userId;
        FirebaseDatabaseService.getServiceInstance();
        return service;
    }

    public static void getCurrentUser(final UserCallback callback) {
        if (userId == null) {
            callback.onCallback(null);
        }

        DatabaseReference userRef = mDatabase.getReference("users/" + userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                callback.onCallback(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    public Task<Void> saveUser(User user) {
        return mDatabase.getReference("users/" + user.get_id()).setValue(user);
    }

    public Task<Void> removeUser(String userId) {
        return mDatabase.getReference("users/" + userId).removeValue();
    }

    public void saveFirebaseToken(FirebaseEntity token) {
        mDatabase.getReference("users/" + userId + "/firebase_token").setValue(token);
    }

    public DatabaseReference getUsers() {
        return mDatabase.getReference("users");
    }

    public DatabaseReference getUser() {
        return mDatabase.getReference("users/" + userId);
    }

    public Task<Void> saveTrip(Trip trip) {
        if (testing) {
            return mDatabase.getReference("test_trips/").setValue(trip);
        }
        else {
            return mDatabase.getReference("trips/").setValue(trip);
        }
    }

    public Task<Void> updateTrip(Trip trip) {
        if (testing) {
            return mDatabase.getReference("test_trips/" + trip.get_id()).setValue(trip);
        }
        else {
            return mDatabase.getReference("trips/" + trip.get_id()).setValue(trip);
        }
    }

    public Task<Void> removeTrip(String tripId) {
        if (testing) {
            return mDatabase.getReference("test_trips/" + tripId).removeValue();
        }
        else {
            return mDatabase.getReference("trips/" + tripId).removeValue();
        }
    }

    public DatabaseReference getTrip(String tripId) {
        if (testing) {
            return mDatabase.getReference("test_trips/" + tripId).getRef();
        }
        else {
            return mDatabase.getReference("trips/" + tripId).getRef();
        }
    }

    public DatabaseReference getTrips() {
        if (testing) {
            return mDatabase.getReference("test_trips/").getRef();
        }
        else {
            return mDatabase.getReference("trips/").getRef();
        }
    }

    public Task<Void> clearTrips() {
        if (testing) {
            return mDatabase.getReference("test_trips/").removeValue();
        }
        else {
            return mDatabase.getReference("trips/").removeValue();
        }
    }
}