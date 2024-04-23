package us.master.entregable2.services;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapsService {
    private static final String BASE_URL = "https://maps.googleapis.com/";

    public static LatLng getLatLngFromCityName(String cityName, String google_maps_key) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleMapsServiceInterface service = retrofit.create(GoogleMapsServiceInterface.class);

        try {
            JsonObject response = service.getLatLngFromCityName(cityName, google_maps_key).execute().body();
            JsonObject location = response.getAsJsonArray("results").get(0)
                    .getAsJsonObject().getAsJsonObject("geometry").getAsJsonObject("location");
            double lat = location.get("lat").getAsDouble();
            double lng = location.get("lng").getAsDouble();
            return new LatLng(lat, lng);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}