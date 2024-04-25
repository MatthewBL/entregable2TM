package us.master.entregable2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.format.DateTimeFormatter;

public class Trip implements Parcelable {
    private String _id;
    private String destination;
    private String destinationCountry;
    private String startPoint;
    private String arrivalDate;
    private String departureDate;
    private double price;
    private String description;
    private String image;
    private String subreddit;
    private String articleId;

    public Trip() {
        this._id = "";
        this.destination = "";
        this.destinationCountry = "";
        this.startPoint = "";
        this.arrivalDate = "";
        this.departureDate = "";
        this.price = 0.0;
        this.description = "";
        this.image = "";
        this.subreddit = "";
        this.articleId = "";
    }

    protected Trip(Parcel in) {
        _id = in.readString();
        destination = in.readString();
        destinationCountry = in.readString();
        startPoint = in.readString();
        price = in.readDouble();
        description = in.readString();
        arrivalDate = in.readString();
        departureDate = in.readString();
        image = in.readString();
        subreddit = in.readString();
        articleId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(destination);
        dest.writeString(destinationCountry);
        dest.writeString(startPoint);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeString(arrivalDate);
        dest.writeString(departureDate);
        dest.writeString(image);
        dest.writeString(subreddit);
        dest.writeString(articleId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    public Trip(String _id, String destination, String destinationCountry, String startPoint, String arrivalDate, String departureDate, double price, boolean isSelected, String description, String image, String subreddit, String articleId) {
        this._id = _id;
        this.destination = destination;
        this.destinationCountry = destinationCountry;
        this.startPoint = startPoint;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.price = price;
        this.description = description;
        this.image = image;
        this.subreddit = subreddit;
        this.articleId = articleId;
    }

    public String get_id(){ return _id; }

    public void set_id(String id){ this._id = id; }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(String destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "destination='" + destination + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", arrivalDate=" + arrivalDate +
                ", departureDate=" + departureDate +
                ", price=" + price +
                ", description='" + description + '\'' +
                '}';
    }
}
