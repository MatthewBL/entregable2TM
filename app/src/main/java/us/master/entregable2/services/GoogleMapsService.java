package us.master.entregable2.services;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import java.util.concurrent.CompletableFuture;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleMapsService {
    private static final String BASE_URL = "https://maps.googleapis.com/";

    public static CompletableFuture<LatLng> getLatLngFromCityName(String cityName, String google_maps_key) {
        CompletableFuture<LatLng> future = new CompletableFuture<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleMapsServiceInterface service = retrofit.create(GoogleMapsServiceInterface.class);

        service.getLatLngFromCityName(cityName, google_maps_key).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject body = response.body();
                    JsonObject location = body.getAsJsonArray("results").get(0).getAsJsonObject().getAsJsonObject("geometry").getAsJsonObject("location");
                    double lat = location.get("lat").getAsDouble();
                    double lng = location.get("lng").getAsDouble();
                    System.out.println("Lat: " + lat + ", Lng: " + lng);
                    future.complete(new LatLng(lat, lng));
                } else {
                    System.out.println("Response not successful: " + response.message());
                    future.completeExceptionally(new Exception("Response not successful: " + response.message()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                System.out.println("Request failed: " + t.getMessage());
                future.completeExceptionally(t);
            }
        });

        return future;
    }
}