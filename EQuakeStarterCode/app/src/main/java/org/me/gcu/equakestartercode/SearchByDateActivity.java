package org.me.gcu.equakestartercode;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

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
    TextView searchResultsText;

    TextView lastBuildDate;

    private final long millisecondsInDay = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_search);

        earthquakeRange = new ArrayList<>();

        // Date search elements
        Calendar calendar = Calendar.getInstance();
        datePickerDialog1 = new DatePickerDialog(this, SearchByDateActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog2 = new DatePickerDialog(this, SearchByDateActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis() - millisecondsInDay * 50);
        datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis() - millisecondsInDay * 50);

        datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());

        dateEditText1 = (TextView) findViewById(R.id.editText1);
        dateEditText2 = (TextView) findViewById(R.id.editText2);
        searchButton = (Button) findViewById(R.id.searchButton);

        dateEditText1.setOnClickListener(this);
        dateEditText2.setOnClickListener(this);
        searchButton.setOnClickListener(this);

        setEditText(dateEditText1, calendar);
        setEditText(dateEditText2, calendar);

        // Earthquake display elements
        searchResultsText = (TextView)findViewById(R.id.searchResults);

        lastBuildDate = (TextView)findViewById(R.id.buildDate);

        startProgress();
    }

    @Override
    public void onThreadComplete() {
        updateEarthquakeData();

        lastBuildDate.setText("Earthquake data correct as of " + EarthquakeData.getLastBuildDate());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        if (isDatesInvalid()) {
            displayAlertDialog("Invalid date entered", "End date must be on or after the start date.", "Dismiss", android.R.drawable.ic_dialog_alert);
            return;
        }
        if (datePicker == datePickerDialog1.getDatePicker()) {
            setEditText(dateEditText1, year, month, day);
        }
        else if (datePicker == datePickerDialog2.getDatePicker()) {
            setEditText(dateEditText2, year, month, day);
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
            if (earthquakeRange.size() == 0)
                displayAlertDialog("No earthquake data found", "No earthquake data was found in the given range. Try entering a different set of dates.", "Dismiss", android.R.drawable.ic_dialog_info);
            updateEarthquakeData();
            searchResultsText.setText("Search returned " + earthquakeRange.size() + " earthquake(s) in the range of dates entered.");
        }
    }

    private void updateEarthquakeData() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (earthquakeRange.size() == 0) {
            fragmentTransaction.replace(R.id.resultsFragment, new EmptyFragment());
        }
        else {
            SearchResultsFragment searchResultsFragment = new SearchResultsFragment(earthquakeRange);

            fragmentTransaction.replace(R.id.resultsFragment, searchResultsFragment);
        }
        fragmentTransaction.commit();
    }

    private void setSearchResultsFragmentVisibility(Fragment fragment) {

    }

    private void setEditText(TextView editText, Calendar calendar) {
        setEditText(editText, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setEditText(TextView editText, int year, int month, int day) {
        editText.setText(String.format("%02d", day) + "/" + String.format("%02d", month + 1) + "/" + year);
    }

    private boolean isDatesInvalid() {
        String date1 = getComparableDate(datePickerDialog1.getDatePicker());
        String date2 = getComparableDate(datePickerDialog2.getDatePicker());

        if (date1.compareToIgnoreCase(date2) > 0)
            return true;
        return false;
    }

    private void displayAlertDialog(String title, String message, String negativeButton, int icon) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)

                .setNegativeButton(negativeButton, null)
                .setIcon(icon)
                .show();
    }

    private String getComparableDate(DatePicker datePicker) {
        return datePicker.getYear() + ":" + String.format("%02d", datePicker.getMonth() + 1) + ":" + String.format("%02d", datePicker.getDayOfMonth());
    }
}