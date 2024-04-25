package us.master.entregable2.entities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class User {
    private String _id;
    private String email;
    private String created;
    private ArrayList<String> selectedTrips = new ArrayList<>();
    private ArrayList<String> boughtTrips = new ArrayList<>();

    public User() {
        this._id = "";
        this.email = "";
        this.created = "";
        this.selectedTrips = new ArrayList<>();
        this.boughtTrips = new ArrayList<>();
    }

    public User(String _id, String email, String created) {
        this._id = _id;
        this.email = email;
        this.created = created;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public ArrayList<String> getSelectedTrips() {
        return selectedTrips;
    }

    public void setSelectedTrips(ArrayList<String> selectedTrips) {
        this.selectedTrips = selectedTrips;
    }

    public ArrayList<String> getBoughtTrips() {
        return boughtTrips;
    }

    public void setBoughtTrips(ArrayList<String> boughtTrips) {
        this.boughtTrips = boughtTrips;
    }

    public void selectTrip(String trip) {
        this.selectedTrips.add(trip);
    }

    public void deselectTrip(String trip) {
        this.selectedTrips.remove(trip);
    }

    public void buyTrip(String trip) {
        this.boughtTrips.add(trip);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (_id != null ? !_id.equals(user._id) : user._id != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return created != null ? created.equals(user.created) : user.created == null;
    }

    @Override
    public int hashCode() {
        int result = _id != null ? _id.hashCode() : 0;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (created != null ? created.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", email='" + email + '\'' +
                ", created=" + created +
                '}';
    }
}