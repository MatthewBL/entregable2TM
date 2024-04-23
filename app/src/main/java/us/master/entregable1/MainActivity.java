package us.master.entregable1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout parentLayout = findViewById(R.id.parentLayout);

        CardView cardView1 = AddCardView(parentLayout, R.drawable.trips, "Viajes disponibles");
        CardView cardView2 = AddCardView(parentLayout, R.drawable.selectedtrips, "Viajes seleccionados");

        cardView1.setId(R.id.cardView1);
        cardView2.setId(R.id.cardView2);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TripList.class);
                intent.putExtra("displaySelected", false); // Display all trips
                startActivity(intent);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TripList.class);
                intent.putExtra("displaySelected", true); // Display only selected trips
                startActivity(intent);
            }
        });
    }


    private CardView AddCardView(LinearLayout parentLayout, int imageResource, String text) {
        // Inflate the CardView layout
        LayoutInflater inflater = LayoutInflater.from(this);
        CardView cardView = (CardView) inflater.inflate(R.layout.card_view_layout, parentLayout, false);

        // Get the ImageView and TextView from the CardView
        ImageView imageView = cardView.findViewById(R.id.imageView2);
        TextView textView = cardView.findViewById(R.id.textView6);

        // Set the image and text
        imageView.setImageResource(imageResource);
        textView.setText(text);

        // Finally, add the CardView to the parent layout
        parentLayout.addView(cardView);

        return cardView;
    }
}