package us.master.entregable2.services;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import us.master.entregable2.entities.FirebaseEntity;
import us.master.entregable2.entities.Trip;
import us.master.entregable2.entities.User;

public class FirebaseDatabaseService {
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
        return mDatabase.getReference("trips/").setValue(trip);
    }

    public Task<Void> updateTrip(Trip trip) {
        return mDatabase.getReference("trips/" + trip.get_id()).setValue(trip);
    }

    public Task<Void> removeTrip(String tripId) {
        return mDatabase.getReference("trips/" + tripId).removeValue();
    }

    public DatabaseReference getTrip(String tripId) {
        return mDatabase.getReference("trips/" + tripId).getRef();
    }

    public DatabaseReference getTrips() {
        return mDatabase.getReference("trips/").getRef();
    }
}