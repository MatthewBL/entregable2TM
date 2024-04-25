package us.master.entregable2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
import us.master.entregable2.services.RedditAuthInterface;
import us.master.entregable2.services.RedditCommentThreadInterface;
import us.master.entregable2.services.UserCallback;

public class TripDetailsView extends FragmentActivity implements OnMapReadyCallback {
    private String redditAccessToken;
    List<String> redditComments = new ArrayList<>();
    private GoogleMap mMap;
    private Trip trip;
    Retrofit retrofit;
    LinearLayout redditIcon;
    TextView comment1TextView;
    TextView comment2UpperSpaceTextView;
    TextView comment2TextView;
    TextView comment3UpperSpaceTextView;
    TextView comment3TextView;

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

        redditLogin();

        TextView destinationTextView = findViewById(R.id.textView5);
        TextView priceTextView = findViewById(R.id.priceValue);
        TextView departureDateTextView = findViewById(R.id.departureDateValue);
        TextView arrivalDateTextView = findViewById(R.id.arrivalDateValue);
        TextView startPointTextView = findViewById(R.id.startPointValue);
        ImageView isSelectedImageView = findViewById(R.id.selectedValue);
        ImageView imageView = findViewById(R.id.imageView4);
        TextView descriptionTextView = findViewById(R.id.textView4);
        redditIcon = findViewById(R.id.reddit);
        comment1TextView = findViewById(R.id.comment1);
        comment2UpperSpaceTextView = findViewById(R.id.comment2_upper_space);
        comment2TextView = findViewById(R.id.comment2);
        comment3UpperSpaceTextView = findViewById(R.id.comment3_upper_space);
        comment3TextView = findViewById(R.id.comment3);

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

        String clientId = getResources().getString(R.string.redditClientId);
        String clientSecret = getResources().getString(R.string.redditClientSecret);
        String username = getResources().getString(R.string.redditUsername);
        String password = getResources().getString(R.string.redditPassword);

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
                        redditGetThread(trip.getSubreddit(), trip.getArticleId(), 3, 1);
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
                        String html = response.body().string();
                        Document doc = Jsoup.parse(html);
                        Elements comments = doc.select(".comment");
                        for (Element comment : comments) {
                            Log.i("COMMENT", comment.toString());
                            String commentText = comment.text();
                            String[] words = commentText.split(" ");
                            int wordsToSkip = (words.length > 7 && words[7].equals("editado")) ? 12 : 6;
                            if (words.length > wordsToSkip) {
                                StringBuilder sb = new StringBuilder();
                                for (int i = wordsToSkip; i < words.length; i++) {
                                    sb.append(words[i]).append(" ");
                                }
                                commentText = sb.toString().trim(); // trim to remove the last space
                            }
                            words = commentText.split(" ");
                            wordsToSkip = (words.length > 1 && words[words.length - 2].equals("Responder")) ? 3 : 2;
                            if (words.length > wordsToSkip) {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < words.length - wordsToSkip; i++) {
                                    sb.append(words[i]).append(" ");
                                }
                                commentText = sb.toString().trim(); // trim to remove the last space
                            }
                            if (commentText.length() > 500) {
                                commentText = commentText.substring(0, 200).trim() + "...";
                            }
                            redditComments.add(commentText);
                        }
                        updateComments();
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

    private void updateComments() {
        if (redditComments.size() > 0) {
            redditIcon.setVisibility(LinearLayout.VISIBLE);
            comment1TextView.setVisibility(TextView.VISIBLE);
            comment1TextView.setText(redditComments.get(0));
        }
        if (redditComments.size() > 1) {
            comment2UpperSpaceTextView.setVisibility(TextView.VISIBLE);
            comment2TextView.setVisibility(TextView.VISIBLE);
            comment2TextView.setText(redditComments.get(1));
        }
        if (redditComments.size() > 2) {
            comment3UpperSpaceTextView.setVisibility(TextView.VISIBLE);
            comment3TextView.setVisibility(TextView.VISIBLE);
            comment3TextView.setText(redditComments.get(2));
        }
    }
}