package us.master.entregable2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import us.master.entregable2.entities.Trip;
import us.master.entregable2.services.FirebaseDatabaseService;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripViewHolder> {

    private List<Trip> tripList;
    private boolean displaySelected;
    private boolean isTwoColumnLayout;

    public TripAdapter(List<Trip> tripList, boolean displaySelected, boolean isTwoColumnLayout) {
        this.tripList = tripList;
        this.displaySelected = displaySelected;
        this.isTwoColumnLayout = isTwoColumnLayout;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (isTwoColumnLayout) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item_two_column, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
        }
        return new TripViewHolder(view, tripList);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripList.get(position);
        holder.destinationTextView.setText(trip.getDestination());

        Date departureDate = Date.from(trip.getDepartureDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date arrivalDate = Date.from(trip.getArrivalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.departureDateTextViewRight.setText(dateFormat.format(departureDate));
        holder.arrivalDateTextViewRight.setText(dateFormat.format(arrivalDate));
        Picasso.get()
                .load(trip.getImage())
                .placeholder(R.drawable.cityicon)
                .into(holder.imageViewLeft);

        holder.priceTextViewRight.setText(String.format(Locale.getDefault(), "%.2f €", trip.getPrice()));

        // Set the star image based on whether the trip is selected or not
        if (trip.isSelected()) {
            holder.imageViewRight.setImageResource(android.R.drawable.star_big_on);
        } else {
            holder.imageViewRight.setImageResource(android.R.drawable.star_big_off);
        }

        // Add an OnClickListener to the imageViewRight
        holder.imageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the isSelected value of the trip
                trip.setSelected(!trip.isSelected());

                FirebaseDatabaseService.getServiceInstance().updateTrip(trip);

                // Get the current position
                int currentPosition = holder.getAdapterPosition();

                // If the list is only displaying selected trips and the trip is unselected, remove it from the list
                if (!trip.isSelected() && displaySelected && currentPosition != RecyclerView.NO_POSITION) {
                    tripList.remove(currentPosition);
                    notifyDataSetChanged();
                } else {
                    // Update the star image based on the new isSelected value
                    if (trip.isSelected()) {
                        holder.imageViewRight.setImageResource(android.R.drawable.star_big_on);
                    } else {
                        holder.imageViewRight.setImageResource(android.R.drawable.star_big_off);
                    }
                }
            }
        });

        // Add an OnClickListener to the cartIcon
        holder.cartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Has comprado el viaje con destino " + trip.getDestination() + " por " + String.format("%.2f", trip.getPrice()) + " €", Toast.LENGTH_SHORT).show();
            }
        });

        // Hide the cartIcon if the list is not the selected trip list
        if (!displaySelected) {
            holder.cartIcon.setVisibility(View.GONE);
        } else {
            holder.cartIcon.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        private final List<Trip> tripList;
        TextView destinationTextView;
        TextView priceTextViewRight;
        TextView departureDateTextViewRight;
        TextView arrivalDateTextViewRight;
        ImageView imageViewLeft;
        ImageView imageViewRight;
        ImageView cartIcon;

        public TripViewHolder(@NonNull View itemView, List<Trip> tripList) {
            super(itemView);
            this.tripList = tripList;
            destinationTextView = itemView.findViewById(R.id.destinationTextView);
            priceTextViewRight = itemView.findViewById(R.id.priceTextViewRight);
            departureDateTextViewRight = itemView.findViewById(R.id.departureDateTextViewRight);
            arrivalDateTextViewRight = itemView.findViewById(R.id.arrivalDateTextViewRight);
            imageViewLeft = itemView.findViewById(R.id.imageViewLeft);
            imageViewRight = itemView.findViewById(R.id.imageViewRight);
            cartIcon = itemView.findViewById(R.id.cartIcon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Trip clickedTrip = tripList.get(position);
                        if (clickedTrip != null) {
                            Intent intent = new Intent(v.getContext(), TripDetailsView.class);
                            intent.putExtra("trip", clickedTrip);
                            v.getContext().startActivity(intent);
                        } else {
                            // Handle the case where the Trip object is null
                        }
                    }
                }
            });
        }
    }

    public void updateList(List<Trip> newList) {
        this.tripList = newList;
        notifyDataSetChanged();
    }
}