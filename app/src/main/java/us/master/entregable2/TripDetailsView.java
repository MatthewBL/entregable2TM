package us.master.entregable2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

import us.master.entregable2.entities.Trip;

public class TripDetailsView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details_view);

        Intent intent = getIntent();
        Trip trip = intent.getParcelableExtra("trip");

        TextView destinationTextView = findViewById(R.id.textView5);
        TextView priceTextView = findViewById(R.id.priceValue);
        TextView departureDateTextView = findViewById(R.id.departureDateValue);
        TextView arrivalDateTextView = findViewById(R.id.arrivalDateValue);
        TextView startPointTextView = findViewById(R.id.startPointValue);
        ImageView isSelectedImageView = findViewById(R.id.selectedValue);
        ImageView imageView = findViewById(R.id.imageView4);
        TextView descriptionTextView = findViewById(R.id.textView4);

        Picasso.get()
                .load(trip.getImage())
                .placeholder(R.drawable.cityicon)
                .into(imageView);

        destinationTextView.setText(trip.getDestination());
        priceTextView.setText(String.format(Locale.getDefault(), "%.2f €", trip.getPrice()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM yyyy", Locale.getDefault());
        departureDateTextView.setText(trip.getDepartureDate().format(formatter));
        arrivalDateTextView.setText(trip.getArrivalDate().format(formatter));

        startPointTextView.setText(trip.getStartPoint());

        if (trip.isSelected()) {
            isSelectedImageView.setImageResource(android.R.drawable.btn_star_big_on);
            isSelectedImageView.setTag("android.R.drawable.star_big_on");
        } else {
            isSelectedImageView.setImageResource(android.R.drawable.btn_star_big_off);
            isSelectedImageView.setTag("android.R.drawable.star_big_off");
        }

        descriptionTextView.setText(trip.getDescription());
    }
}