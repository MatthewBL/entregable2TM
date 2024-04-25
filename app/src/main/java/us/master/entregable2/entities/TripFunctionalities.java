package us.master.entregable2.entities;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import us.master.entregable2.services.FirebaseDatabaseService;
import us.master.entregable2.services.GoogleMapsService;

public class TripFunctionalities {
    private static List<Trip> tripList = new ArrayList<>();

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
            LocalDate departureDateType = LocalDate.now().plusDays(random.nextInt(60));
            LocalDate arrivalDateType = departureDateType.plusDays(10 + random.nextInt(10));
            String departureDate = departureDateType.format(DateTimeFormatter.ISO_DATE);
            String arrivalDate = arrivalDateType.format(DateTimeFormatter.ISO_DATE);
            double price = 100 + (500 - 100) * random.nextDouble();
            boolean isSelected = false;
            String description = descriptions[destinationIndex];
            String image = images[destinationIndex];
            String subreddit = subreddits[destinationIndex];
            String articleId = articleIds[destinationIndex];

            Trip trip = new Trip(String.valueOf(i), destination, startPoint, arrivalDate, departureDate, price, isSelected, description, image, subreddit, articleId);
            DatabaseReference tripsRef = FirebaseDatabaseService.getServiceInstance().getTrips();
            tripsRef.push().setValue(trip);
        }
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
            LocalDate departureDateType = LocalDate.now().plusDays(random.nextInt(60));
            LocalDate arrivalDateType = departureDateType.plusDays(10 + random.nextInt(10));
            String departureDate = departureDateType.format(DateTimeFormatter.ISO_DATE);
            String arrivalDate = arrivalDateType.format(DateTimeFormatter.ISO_DATE);
            double price = 100 + (500 - 100) * random.nextDouble();
            boolean isSelected = false;
            String description = descriptions[destinationIndex];
            String image = images[destinationIndex];
            String subreddit = subreddits[destinationIndex];
            String articleId = articleIds[destinationIndex];

            Trip trip = new Trip(String.valueOf(i), destination, startPoint, arrivalDate, departureDate, price, isSelected, description, image, subreddit, articleId);
            DatabaseReference tripsRef = FirebaseDatabaseService.getServiceInstance().getTrips();
            tripsRef.push().setValue(trip);
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

    public static List<Trip> obtainTripList() {
        return tripList;
    }

    public static List<Trip> obtainSelectedTripList(User user) {
        List<Trip> selectedTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (isSelected(trip, user) && !isBought(trip, user)) {
                selectedTripList.add(trip);
            }
        }
        return selectedTripList;
    }

    public static List<Trip> obtainBoughtTripList(User user) {
        List<Trip> boughtTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (isBought(trip, user)) {
                boughtTripList.add(trip);
            }
        }
        return boughtTripList;
    }

    public static List<Trip> obtainNonBoughtTripList(User user) {
        List<Trip> nonBoughtTripList = new ArrayList<>();
        for (Trip trip : tripList) {
            if (!isBought(trip, user)) {
                nonBoughtTripList.add(trip);
            }
        }
        return nonBoughtTripList;
    }

    public static LocalDate obtainArrivalDateType(Trip trip) {
        return LocalDate.parse(trip.getArrivalDate(), DateTimeFormatter.ISO_DATE);
    }

    public static LocalDate obtainDepartureDateType(Trip trip) {
        return LocalDate.parse(trip.getDepartureDate(), DateTimeFormatter.ISO_DATE);
    }

    public static boolean isSelected(Trip trip, User user) {
        return user.getSelectedTrips().contains(trip.get_id());
    }

    public static boolean isBought(Trip trip, User user) {
        return user.getBoughtTrips().contains(trip.get_id());
    }

    public static LatLng obtainDestinationLatLng(Trip trip, String google_maps_key) {
        return null; //GoogleMapsService.getLatLngFromCityName(trip.getDestination(), google_maps_key);
    }

    public static LatLng obtainStartPointLatLng(Trip trip, String google_maps_key) {
        CompletableFuture<LatLng> future = GoogleMapsService.getLatLngFromCityName(trip.getStartPoint(), google_maps_key);
        return future.join();
    }
}
