package us.master.entregable2.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import us.master.entregable2.services.FirebaseDatabaseService;
import us.master.entregable2.services.GoogleMapsService;

public class Trip implements Parcelable {
    private String _id;
    private static List<Trip> tripList = new ArrayList<>();
    private String destination;
    private String startPoint;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private double price;
    private String description;
    private String image;
    private String subreddit;
    private String articleId;

    protected Trip(Parcel in) {
        _id = in.readString();
        destination = in.readString();
        startPoint = in.readString();
        price = in.readDouble();
        description = in.readString();
        arrivalDate = (LocalDate) in.readSerializable();
        departureDate = (LocalDate) in.readSerializable();
        image = in.readString();
        subreddit = in.readString();
        articleId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(destination);
        dest.writeString(startPoint);
        dest.writeDouble(price);
        dest.writeString(description);
        dest.writeSerializable(arrivalDate);
        dest.writeSerializable(departureDate);
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

    public Trip(String _id, String destination, String startPoint, LocalDate arrivalDate, LocalDate departureDate, double price, boolean isSelected, String description, String image, String subreddit, String articleId) {
        this._id = _id;
        this.destination = destination;
        this.startPoint = startPoint;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.price = price;
        this.description = description;
        this.image = image;
        this.subreddit = subreddit;
        this.articleId = articleId;
    }

    public static void generateTripData() {
        String[] destinations = {"París", "Londres", "Nueva York", "Tokio", "Sídney", "Roma", "Berlín", "Madrid", "Pekín", "Río de Janeiro"};
        String[] startPoints = {"Madrid", "Barcelona", "Valencia", "Sevilla", "Bilbao", "Málaga", "Oviedo", "Santander", "Zaragoza", "Murcia"};
        String[] descriptions = {"Viaje a la ciudad del amor", "Viaje a la ciudad de la lluvia", "Viaje a la ciudad de los rascacielos", "Viaje a la ciudad del sushi", "Viaje a la ciudad de los koalas", "Viaje a la ciudad de los gladiadores", "Viaje a la ciudad de la cerveza", "Viaje a la ciudad del bocadillo de calamares", "Viaje a la ciudad de la Gran Muralla", "Viaje a la ciudad de la samba"};
        String[] images = {"https://viajes.nationalgeographic.com.es/medio/2023/01/31/2023_7fffe24b_230131085752_800x800.jpg",
                "https://cms.finnair.com/resource/blob/630892/68a843d4659786d6b381603c8e394e42/london-main-data.jpg",
                "https://images.hola.com/imagenes/viajes/20200416165850/manhattan-nueva-york-maravillas-desde-mi-pantalla/0-812-351/nueva-york-manhattan-maravillas-desde-mi-pantalla-m.jpg",
                "https://aws-tiqets-cdn.imgix.net/images/content/69e3b96cd5414970b3da6b14ec9b5ee6.jpeg",
                "https://img.freepik.com/fotos-premium/dia-mundial-turismo-ciudad-puerto-sydney-australia_940384-210.jpg",
                "https://www.thetrainline.com/cms/media/1473/italy-rome-sunset.jpg",
                "https://aws-tiqets-cdn.imgix.net/images/content/da0b659013eb49cf816407c5ada7bd3c.jpg",
                "https://www.spain.info/export/sites/segtur/.content/imagenes/reportajes/madrid/plaza-callao-gran-via-madrid-c-giuseppe-buccola-u1128812.jpg",
                "https://i.natgeofe.com/n/2024d353-131c-4c29-a04f-5589c541e980/beijing_travel_square.jpg",
                "https://i.natgeofe.com/n/560b293d-80b2-4449-ad6c-036a249d46f8/rio-de-janeiro-travel_square.jpg"};
        String[] subreddits = {"EmilyInParis", "solotravel", "AskAnAmerican", "Tokyo", "sydney", "travel", "travel", "travel", "beijing", "travel"};
        String[] articleIds = {"y5ujsg", "9cdpdk", "uw5dwc", "44r46g", "16ibt0i", "uug49c", "pwdj43", "11466s6", "15he1ao", "17avy5u"};

        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            int destinationIndex = random.nextInt(destinations.length);
            String destination = destinations[destinationIndex];
            String startPoint = startPoints[random.nextInt(startPoints.length)];
            LocalDate departureDate = LocalDate.now().plusDays(random.nextInt(60));
            LocalDate arrivalDate = departureDate.plusDays(10 + random.nextInt(10));
            double price = 100 + (500 - 100) * random.nextDouble();
            boolean isSelected = false;
            String description = descriptions[destinationIndex];
            String image = images[destinationIndex];
            String subreddit = subreddits[destinationIndex];
            String articleId = articleIds[destinationIndex];

            Trip trip = new Trip(String.valueOf(i), destination, startPoint, arrivalDate, departureDate, price, isSelected, description, image, subreddit, articleId);
            FirebaseDatabaseService.getServiceInstance().saveTrip(trip);
        }
    }

    public static void readTripData() {
        tripList.clear();
        DatabaseReference tripsRef = FirebaseDatabaseService.getServiceInstance().getTrips();
        tripsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot tripSnapshot: dataSnapshot.getChildren()) {
                    Trip trip = tripSnapshot.getValue(Trip.class);
                    tripList.add(trip);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }

    public static void generateTestTripList(long seed){
        tripList.clear();
        FirebaseDatabaseService.testing = true;
        FirebaseDatabaseService.getServiceInstance().clearTrips();

        String[] destinations = {"París", "Londres", "Nueva York", "Tokio", "Sídney", "Roma", "Berlín", "Madrid", "Pekín", "Río de Janeiro"};
        String[] startPoints = {"Madrid", "Barcelona", "Valencia", "Sevilla", "Bilbao", "Málaga", "Oviedo", "Santander", "Zaragoza", "Murcia"};
        String[] descriptions = {"Viaje a la ciudad del amor", "Viaje a la ciudad de la lluvia", "Viaje a la ciudad de los rascacielos", "Viaje a la ciudad del sushi", "Viaje a la ciudad de los koalas", "Viaje a la ciudad de los gladiadores", "Viaje a la ciudad de la cerveza", "Viaje a la ciudad del bocadillo de calamares", "Viaje a la ciudad de la Gran Muralla", "Viaje a la ciudad de la samba"};
        String[] images = {"https://viajes.nationalgeographic.com.es/medio/2023/01/31/2023_7fffe24b_230131085752_800x800.jpg",
                "https://cms.finnair.com/resource/blob/630892/68a843d4659786d6b381603c8e394e42/london-main-data.jpg",
                "https://images.hola.com/imagenes/viajes/20200416165850/manhattan-nueva-york-maravillas-desde-mi-pantalla/0-812-351/nueva-york-manhattan-maravillas-desde-mi-pantalla-m.jpg",
                "https://aws-tiqets-cdn.imgix.net/images/content/69e3b96cd5414970b3da6b14ec9b5ee6.jpeg",
                "https://img.freepik.com/fotos-premium/dia-mundial-turismo-ciudad-puerto-sydney-australia_940384-210.jpg",
                "https://www.thetrainline.com/cms/media/1473/italy-rome-sunset.jpg",
                "https://aws-tiqets-cdn.imgix.net/images/content/da0b659013eb49cf816407c5ada7bd3c.jpg",
                "https://www.spain.info/export/sites/segtur/.content/imagenes/reportajes/madrid/plaza-callao-gran-via-madrid-c-giuseppe-buccola-u1128812.jpg",
                "https://i.natgeofe.com/n/2024d353-131c-4c29-a04f-5589c541e980/beijing_travel_square.jpg",
                "https://i.natgeofe.com/n/560b293d-80b2-4449-ad6c-036a249d46f8/rio-de-janeiro-travel_square.jpg"};
        String[] subreddits = {"EmilyInParis", "solotravel", "AskAnAmerican", "Tokyo", "sydney", "travel", "travel", "travel", "beijing", "travel"};
        String[] articleIds = {"y5ujsg", "9cdpdk", "uw5dwc", "44r46g", "16ibt0i", "uug49c", "pwdj43", "11466s6", "15he1ao", "17avy5u"};

        Random random = new Random(seed);
        for (int i = 0; i < 20; i++) {
            int destinationIndex = random.nextInt(destinations.length);
            String destination = destinations[destinationIndex];
            String startPoint = startPoints[random.nextInt(startPoints.length)];
            LocalDate departureDate = LocalDate.now().plusDays(random.nextInt(60));
            LocalDate arrivalDate = departureDate.plusDays(10 + random.nextInt(10));
            double price = 100 + (500 - 100) * random.nextDouble();
            boolean isSelected = false;
            String description = descriptions[destinationIndex];
            String image = images[destinationIndex];
            String subreddit = subreddits[destinationIndex];
            String articleId = articleIds[destinationIndex];

            Trip trip = new Trip(String.valueOf(i), destination, startPoint, arrivalDate, departureDate, price, isSelected, description, image, subreddit, articleId);
            FirebaseDatabaseService.getServiceInstance().saveTrip(trip);
        }
    }

    public String get_id(){ return _id; }

    public void set_id(String id){ this._id = id; }

    public static List<Trip> getTripList() {
        return tripList;
    }

    public static List<Trip> getSelectedTripList(User user) {
        List<Trip> selectedTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (trip.isSelected(user) && !trip.isBought(user)) {
                selectedTripList.add(trip);
            }
        }
        return selectedTripList;
    }

    public static List<Trip> getBoughtTripList(User user) {
        List<Trip> boughtTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (trip.isBought(user)) {
                boughtTripList.add(trip);
            }
        }
        return boughtTripList;
    }

    public static List<Trip> getNonBoughtTripList(User user) {
        List<Trip> nonBoughtTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (!trip.isBought(user)) {
                nonBoughtTripList.add(trip);
            }
        }
        return nonBoughtTripList;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSelected(User user) {
        return user.isSelected(this.get_id());
    }
    public boolean isBought(User user) { return user.isBought(this.get_id()); }

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

    public LatLng getDestinationLatLng(String google_maps_key) {
        return GoogleMapsService.getLatLngFromCityName(destination, google_maps_key);
    }

    public LatLng getStartPointLatLng(String google_maps_key) {
        return GoogleMapsService.getLatLngFromCityName(startPoint, google_maps_key);
    }
}
