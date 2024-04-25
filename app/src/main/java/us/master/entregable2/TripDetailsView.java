package us.master.entregable2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.master.entregable2.entities.Trip;
import us.master.entregable2.entities.TripFunctionalities;
import us.master.entregable2.entities.User;
import us.master.entregable2.services.FirebaseDatabaseService;
import us.master.entregable2.services.PropertiesManager;
import us.master.entregable2.services.RedditAuthInterface;
import us.master.entregable2.services.RedditCommentThreadInterface;
import us.master.entregable2.services.UserCallback;

public class TripDetailsView extends FragmentActivity implements OnMapReadyCallback {
    private String redditAccessToken;
    List<String> comments = new ArrayList<>();
    private GoogleMap mMap;
    private Trip trip;
    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_view);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        trip = intent.getParcelableExtra("trip");

        retrofit = new Retrofit.Builder()
                .baseUrl("https://www.reddit.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

//        redditLogin();
//        redditGetThread(trip.getSubreddit(), trip.getArticleId(), 3, 1);

        TextView destinationTextView = findViewById(R.id.textView5);
        TextView priceTextView = findViewById(R.id.priceValue);
        TextView departureDateTextView = findViewById(R.id.departureDateValue);
        TextView arrivalDateTextView = findViewById(R.id.arrivalDateValue);
        TextView startPointTextView = findViewById(R.id.startPointValue);
        ImageView isSelectedImageView = findViewById(R.id.selectedValue);
        ImageView imageView = findViewById(R.id.imageView4);
        TextView descriptionTextView = findViewById(R.id.textView4);
        TextView comment1TextView = findViewById(R.id.comment1);
        TextView comment2TextView = findViewById(R.id.comment2);
        TextView comment3TextView = findViewById(R.id.comment3);

        Picasso.get()
                .load(trip.getImage())
                .placeholder(R.drawable.cityicon)
                .into(imageView);

        destinationTextView.setText(trip.getDestination());
        priceTextView.setText(String.format(Locale.getDefault(), "%.2f €", trip.getPrice()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM yyyy", Locale.getDefault());
        departureDateTextView.setText(trip.getDepartureDate());
        arrivalDateTextView.setText(trip.getArrivalDate());

        startPointTextView.setText(trip.getStartPoint());

        FirebaseDatabaseService.getCurrentUser(new UserCallback() {
            @Override
            public void onCallback(User user) {
                if (user != null) {
                    // Set the star image based on whether the trip is selected or not
                    if (TripFunctionalities.isSelected(trip, user)) {
                        isSelectedImageView.setImageResource(android.R.drawable.btn_star_big_on);
                        isSelectedImageView.setTag("android.R.drawable.star_big_on");
                    } else {
                        isSelectedImageView.setImageResource(android.R.drawable.btn_star_big_off);
                        isSelectedImageView.setTag("android.R.drawable.star_big_off");
                    }
                } else {
                    // Handle the case where there is no logged in user
                }
            }
        });


        descriptionTextView.setText(trip.getDescription());

        if (comments.size() > 0) {
            comment1TextView.setVisibility(TextView.VISIBLE);
            comment1TextView.setText("Opinion: " + comments.get(0));
        }
        if (comments.size() > 1) {
            comment2TextView.setVisibility(TextView.VISIBLE);
            comment2TextView.setText("Opinion: " + comments.get(1));
        }
        if (comments.size() > 2) {
            comment3TextView.setVisibility(TextView.VISIBLE);
            comment3TextView.setText("Opinion: " + comments.get(2));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        String apiKey = getResources().getString(R.string.google_maps_key);

        String destination = trip.getDestination() + ", " + trip.getDestinationCountry();
        TripFunctionalities.obtainLocationLatLng(destination, apiKey, new TripFunctionalities.LatLngCallback() {
            @Override
            public void onSuccess(LatLng location) {
                mMap.addMarker(new MarkerOptions().title("Trip Destination").position(location));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(location));
            }

            @Override
            public void onFailure(Throwable t) {
                // Handle failure scenario
            }
        });
    }

    private void redditLogin() {
        RedditAuthInterface redditAuthAPI = retrofit.create(RedditAuthInterface.class);

        Properties properties = PropertiesManager.loadProperties(this);
        String clientId = properties.getProperty("clientId");
        String clientSecret = properties.getProperty("clientSecret");
        String username = properties.getProperty("username");
        String password = properties.getProperty("password");

        String authHeader = "Basic " + Base64.encodeToString((clientId + ":" + clientSecret).getBytes(), Base64.NO_WRAP);
        Call<ResponseBody> call = redditAuthAPI.getAccessToken(authHeader, "password", username, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
                        redditAccessToken = jsonObject.get("access_token").getAsString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO: Handle the error
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // TODO: Handle the failure
            }
        });
    }

    private void redditGetThread(String subreddit, String articleId, int limit, int depth) {
        String authHeader = "Bearer " + redditAccessToken;
        RedditCommentThreadInterface redditCommentThreadAPI = retrofit.create(RedditCommentThreadInterface.class);
        Call<ResponseBody> call = redditCommentThreadAPI.getComments(authHeader, subreddit, articleId, limit, depth);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String json = response.body().string();
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
                        JsonArray children = jsonObject.getAsJsonObject("data").getAsJsonArray("children");
                        comments = new ArrayList<>();
                        for (JsonElement child : children) {
                            JsonObject data = child.getAsJsonObject().getAsJsonObject("data");
                            if (data.has("replies")) {
                                JsonObject replies = data.getAsJsonObject("replies");
                                if (replies.has("data")) {
                                    JsonArray replyChildren = replies.getAsJsonObject("data").getAsJsonArray("children");
                                    for (JsonElement replyChild : replyChildren) {
                                        JsonObject replyData = replyChild.getAsJsonObject().getAsJsonObject("data");
                                        if (replyData.has("body")) {
                                            String comment = replyData.get("body").getAsString();
                                            if (comment.length() > 200) {
                                                comment = comment.substring(0, 200).trim() + "...";
                                            }
                                            comments.add(comment);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // TODO: Handle the error
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // TODO: Handle the failure
            }
        });
    }
}