package us.master.entregable2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FilterActivity extends AppCompatActivity {
    private static final int TRIP_LIST_REQUEST_CODE = 1;

    private TextView startDateTextView;
    private ImageView startDateImageView;

    private TextView endDateTextView;
    private ImageView endDateImageView;
    private SeekBar minPriceSeekBar;
    private TextView minPriceTextView;
    private SeekBar maxPriceSeekBar;
    private TextView maxPriceTextView;
    private Button saveAndReturnButton;
    private CheckBox airportCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        startDateTextView = findViewById(R.id.startDateTextView);
        startDateImageView = findViewById(R.id.startDateImageView);
        endDateTextView = findViewById(R.id.endDateTextView);
        endDateImageView = findViewById(R.id.endDateImageView);
        minPriceSeekBar = findViewById(R.id.minPriceSeekBar);
        minPriceTextView = findViewById(R.id.minPriceTextView);
        maxPriceSeekBar = findViewById(R.id.maxPriceSeekBar);
        maxPriceTextView = findViewById(R.id.maxPriceTextView);
        saveAndReturnButton = findViewById(R.id.button3);
        airportCheckBox = findViewById(R.id.airportCheckBox);

        Intent intent = getIntent();
        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");
        int minPrice = intent.getIntExtra("minPrice", 0);
        int maxPrice = intent.getIntExtra("maxPrice", 0);
        boolean airportCheckBoxValue = intent.getBooleanExtra("airportCheckBox", false);

        // Set the initial state of the filter controls
        startDateTextView.setText(startDate);
        endDateTextView.setText(endDate);
        minPriceSeekBar.setProgress(minPrice);
        maxPriceSeekBar.setProgress(maxPrice);
        minPriceTextView.setText("Precio mínimo: " + minPrice + "€");
        maxPriceTextView.setText("Precio máximo: " + maxPrice + "€");
        airportCheckBox.setChecked(airportCheckBoxValue);

        startDateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(startDateTextView);
            }
        });

        endDateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(endDateTextView);
            }
        });

        minPriceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minPriceTextView.setText("Precio mínimo: " + progress + "€");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });

        maxPriceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                maxPriceTextView.setText("Precio máximo: " + progress + "€");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Do nothing
            }
        });

        // When the save button is clicked
        saveAndReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("startDate", startDateTextView.getText().toString());
                resultIntent.putExtra("endDate", endDateTextView.getText().toString());
                resultIntent.putExtra("minPrice", minPriceSeekBar.getProgress());
                resultIntent.putExtra("maxPrice", maxPriceSeekBar.getProgress());
                resultIntent.putExtra("airportCheckBox", airportCheckBox.isChecked());
                setResult(RESULT_OK, resultIntent);
                LinearLayout rootLayout = findViewById(R.id.filterActivityRootLayout);
                rootLayout.setVisibility(View.GONE);
                finish();
            }
        });
    }

    private void showDatePickerDialog(final TextView dateTextView) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(FilterActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                        String strDate = format.format(calendar.getTime());
                        dateTextView.setText(strDate);
                    }
                }, year, month, day);

        datePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dateTextView.setText("-");
            }
        });

        datePickerDialog.show();
    }
}