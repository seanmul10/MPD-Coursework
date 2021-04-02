package org.me.gcu.equakestartercode;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SearchByDateActivity extends EarthquakeActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener
{
    List<Earthquake> earthquakeRange;

    // Date search elements
    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;

    TextView dateEditText1;
    TextView dateEditText2;

    Button searchButton;

    // Earthquake text views
    TextView largestEqLocation;
    TextView largestEqDate;
    TextView largestEqMagnitude;

    TextView northEqLocation;
    TextView northEqDate;
    TextView northEqLatLon;

    TextView eastEqLocation;
    TextView eastEqDate;
    TextView eastEqLatLon;

    TextView southEqLocation;
    TextView southEqDate;
    TextView southEqLatLon;

    TextView westEqLocation;
    TextView westEqDate;
    TextView westEqLatLon;

    TextView shallowestEqLocation;
    TextView shallowestEqDate;
    TextView shallowestEqDepth;

    TextView deepestEqLocation;
    TextView deepestEqDate;
    TextView deepestEqDepth;

    TextView lastBuildDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_search);

        earthquakeRange = new ArrayList<>();

        // Date search elements
        Calendar calendar = Calendar.getInstance();
        datePickerDialog1 = new DatePickerDialog(this, SearchByDateActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog2 = new DatePickerDialog(this, SearchByDateActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        dateEditText1 = (TextView) findViewById(R.id.editText1);
        dateEditText2 = (TextView) findViewById(R.id.editText2);
        searchButton = (Button) findViewById(R.id.searchButton);

        dateEditText1.setOnClickListener(this);
        dateEditText2.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        SetEditText(dateEditText1, calendar);
        SetEditText(dateEditText2, calendar);

        // Earthquake display elements
        largestEqLocation = (TextView)findViewById(R.id.largestEqLocation);
        largestEqDate = (TextView)findViewById(R.id.largestEqDate);
        //largestEqMagnitude = (TextView)findViewById(R.id.largestEqMagnitude);

        northEqLocation = (TextView)findViewById(R.id.northEqLocation);;
        northEqDate = (TextView)findViewById(R.id.northEqDate);
        //northEqLatLon = (TextView)findViewById(R.id.northEqLatLon);

        eastEqLocation = (TextView)findViewById(R.id.eastEqLocation);
        eastEqDate = (TextView)findViewById(R.id.eastEqDate);
        //eastEqLatLon = (TextView)findViewById(R.id.eastEqLatLon);

        southEqLocation = (TextView)findViewById(R.id.southEqLocation);
        southEqDate = (TextView)findViewById(R.id.southEqDate);
        //southEqLatLon = (TextView)findViewById(R.id.southEqLatLon);

        westEqLocation = (TextView)findViewById(R.id.westEqLocation);
        westEqDate = (TextView)findViewById(R.id.westEqDate);
        //westEqLatLon = (TextView)findViewById(R.id.westEqLatLon);

        shallowestEqLocation = (TextView)findViewById(R.id.shallowestEqLocation);
        shallowestEqDate = (TextView)findViewById(R.id.shallowestEqDate);
        //shallowestEqDepth = (TextView)findViewById(R.id.shallowestEqDepth);

        deepestEqLocation = (TextView)findViewById(R.id.deepestEqLocation);
        deepestEqDate = (TextView)findViewById(R.id.deepestEqDate);
        //deepestEqDepth = (TextView)findViewById(R.id.deepestEqDepth);

        lastBuildDate = (TextView)findViewById(R.id.buildDate);

        lastBuildDate.setText("Earthquake data correct as of " + EarthquakeData.getLastBuildDate());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (isDatesInvalid()) {
            displayAlertDialog("Invalid date entered", "End date must be on or after the start date.", "Dismiss");
            return;
        }
        if (datePicker == datePickerDialog1.getDatePicker()) {
            SetEditText(dateEditText1, year, month, day);
        }
        else if (datePicker == datePickerDialog2.getDatePicker()) {
            SetEditText(dateEditText2, year, month, day);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == dateEditText1) {
            datePickerDialog1.show();
        }
        else if (view == dateEditText2) {
            datePickerDialog2.show();
        }
        else if (view == searchButton) {
            earthquakeRange = EarthquakeData.getEarthquakesInRange(getComparableDate(datePickerDialog1.getDatePicker()), getComparableDate(datePickerDialog2.getDatePicker()));
            Log.d("DateSearch", "Found " + earthquakeRange.size() + " earthquakes in the given range.");
            UpdateEarthquakeData();
        }
    }

    private void UpdateEarthquakeData() {
        if (earthquakeRange.size() == 0) {
            // No earthquakes found in this range
            return;
        }
        Earthquake largestEarthquake = EarthquakeData.getBiggestEarthquake(earthquakeRange);
        largestEqLocation.setText(largestEarthquake.getLocation());
        largestEqDate.setText(largestEarthquake.getDate());
        //largestEqMagnitude.setText(Float.toString(largestEarthquake.getMagnitude()));

        Earthquake northernmostEarthquake = EarthquakeData.getMostNorthernEarthquake(earthquakeRange);
        northEqLocation.setText(northernmostEarthquake.getLocation());
        northEqDate.setText(northernmostEarthquake.getDate());
        //northEqLatLon.setText(northernmostEarthquake.getLatitude() + ", " + northernmostEarthquake.getLongitude());

        Earthquake easternmostEarthquake = EarthquakeData.getMostEasterlyEarthquake(earthquakeRange);
        eastEqLocation.setText(easternmostEarthquake.getLocation());
        eastEqDate.setText(easternmostEarthquake.getDate());
        //eastEqLatLon.setText(easternmostEarthquake.getLatitude() + ", " + easternmostEarthquake.getLongitude());

        Earthquake southernmostEarthquake = EarthquakeData.getMostSoutherlyEarthquake(earthquakeRange);
        southEqLocation.setText(southernmostEarthquake.getLocation());
        southEqDate.setText(southernmostEarthquake.getDate());
        //southEqLatLon.setText(southernmostEarthquake.getLatitude() + ", " + southernmostEarthquake.getLongitude());

        Earthquake westernmostEarthquake = EarthquakeData.getMostWesterlyEarthquake(earthquakeRange);
        westEqLocation.setText(westernmostEarthquake.getLocation());
        westEqDate.setText(westernmostEarthquake.getDate());
        //westEqLatLon.setText(westernmostEarthquake.getLatitude() + ", " + westernmostEarthquake.getLongitude());

        Earthquake shallowestEarthquake = EarthquakeData.getShallowestEarthquake(earthquakeRange);
        shallowestEqLocation.setText(shallowestEarthquake.getLocation());
        shallowestEqDate.setText(shallowestEarthquake.getDate());
        //shallowestEqDepth.setText(shallowestEarthquake.getDepth() + "km");

        Earthquake deepestEarthquake = EarthquakeData.getDeepestEarthquake(earthquakeRange);
        deepestEqLocation.setText(deepestEarthquake.getLocation());
        deepestEqDate.setText(deepestEarthquake.getDate());
        //deepestEqDepth.setText(deepestEarthquake.getDepth() + "km");
    }

    private void SetEditText(TextView editText, Calendar calendar) {
        SetEditText(editText, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void SetEditText(TextView editText, int year, int month, int day) {
        editText.setText(String.format("%02d", day) + "/" + String.format("%02d", month + 1) + "/" + year);
    }

    private boolean isDatesInvalid() {
        String date1 = getComparableDate(datePickerDialog1.getDatePicker());
        String date2 = getComparableDate(datePickerDialog2.getDatePicker());

        if (date1.compareToIgnoreCase(date2) > 0)
            return true;
        return false;
    }

    private void displayAlertDialog(String title, String message, String negativeButton) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)

                .setNegativeButton(negativeButton, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private String getComparableDate(DatePicker datePicker) {
        return datePicker.getYear() + ":" + String.format("%02d", datePicker.getMonth() + 1) + ":" + String.format("%02d", datePicker.getDayOfMonth());
    }
}