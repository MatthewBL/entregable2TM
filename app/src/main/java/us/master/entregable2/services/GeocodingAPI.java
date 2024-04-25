package us.master.entregable2.services;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingAPI {
    @GET("maps/api/geocode/json")
    Call<JsonObject> getLatLng(
            @Query("address") String address,
            @Query("key") String apiKey
    );
}
