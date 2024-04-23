package us.master.entregable2.services;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleMapsServiceInterface {
    @GET("maps/api/geocode/json")
    Call<JsonObject> getLatLngFromCityName(@Query("address") String cityName, @Query("key") String google_maps_key);
}