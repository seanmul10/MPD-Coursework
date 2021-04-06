package org.me.gcu.equakestartercode;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchByDateActivity extends EarthquakeActivity implements DatePickerDialog.OnDateSetListener, View.OnClickListener
{
    // Date search elements
    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;

    TextView dateEditText1;
    TextView dateEditText2;
    LinearLayout endDateLayout;

    RadioGroup radioGroup;
    RadioButton radio1;
    RadioButton radio2;

    Button searchButton;

    TextView lastBuildDate;

    private final long millisecondsInDay = 86400000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_search);

        // Date search elements
        Calendar calendar = Calendar.getInstance();
        if (savedInstanceState == null) {
            datePickerDialog1 = new DatePickerDialog(this, SearchByDateActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog2 = new DatePickerDialog(this, SearchByDateActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        else {
            datePickerDialog1 = new DatePickerDialog(this, SearchByDateActivity.this, savedInstanceState.getInt("year1"), savedInstanceState.getInt("month1"), savedInstanceState.getInt("day1"));
            datePickerDialog2 = new DatePickerDialog(this, SearchByDateActivity.this, savedInstanceState.getInt("year2"), savedInstanceState.getInt("month2"), savedInstanceState.getInt("day2"));
        }

        datePickerDialog1.getDatePicker().setMinDate(System.currentTimeMillis() - millisecondsInDay * 50);
        datePickerDialog2.getDatePicker().setMinDate(System.currentTimeMillis() - millisecondsInDay * 50);

        datePickerDialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog2.getDatePicker().setMaxDate(System.currentTimeMillis());

        dateEditText1 = (TextView) findViewById(R.id.editText1);
        dateEditText2 = (TextView) findViewById(R.id.editText2);
        endDateLayout = (LinearLayout) findViewById(R.id.endDateLayout);

        searchButton = (Button) findViewById(R.id.searchButton);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radio1 = (RadioButton) findViewById(R.id.radioDateRange);
        radio2 = (RadioButton) findViewById(R.id.radioSingleDate);

        dateEditText1.setOnClickListener(this);
        dateEditText2.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> endDateLayout.setVisibility(radio1.isChecked() ? View.VISIBLE : View.INVISIBLE));

        DatePicker dp1 = datePickerDialog1.getDatePicker();
        DatePicker dp2 = datePickerDialog2.getDatePicker();
        setEditText(dateEditText1, dp1.getYear(), dp1.getMonth(), dp1.getDayOfMonth());
        setEditText(dateEditText2, dp2.getYear(), dp2.getMonth(), dp2.getDayOfMonth());

        radio1.setChecked(true);

        lastBuildDate = (TextView)findViewById(R.id.buildDate);
    }

    @Override
    public void onAsyncTaskComplete() {
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
            String startDate = getComparableDate(datePickerDialog1.getDatePicker());
            String endDate = radio1.isChecked() ? getComparableDate(datePickerDialog2.getDatePicker()) : startDate;
            int earthquakeRangeSize = EarthquakeData.getEarthquakesInRange(startDate, endDate).size();

            if (earthquakeRangeSize == 0)
                displayAlertDialog("No earthquake data found", "No earthquake data was found in the given range. Try entering a different set of dates.", "Dismiss", android.R.drawable.ic_dialog_info);
            updateEarthquakeData(earthquakeRangeSize, startDate, endDate);
        }
    }

    private void updateEarthquakeData(int rangeSize, String date1, String date2) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (rangeSize == 0) {
            fragmentTransaction.replace(R.id.resultsFragment, new EmptyFragment());
        }
        else {
            SearchResultsFragment searchResultsFragment = new SearchResultsFragment(date1, date2);

            fragmentTransaction.replace(R.id.resultsFragment, searchResultsFragment);
        }
        fragmentTransaction.commit();
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putInt("year1", datePickerDialog1.getDatePicker().getYear());
        savedInstanceState.putInt("month1", datePickerDialog1.getDatePicker().getMonth());
        savedInstanceState.putInt("day1", datePickerDialog1.getDatePicker().getDayOfMonth());
        savedInstanceState.putInt("year2", datePickerDialog2.getDatePicker().getYear());
        savedInstanceState.putInt("month2", datePickerDialog2.getDatePicker().getMonth());
        savedInstanceState.putInt("day2", datePickerDialog2.getDatePicker().getDayOfMonth());
        savedInstanceState.putBoolean("radioBool", radio1.isChecked());

        super.onSaveInstanceState(savedInstanceState);
    }
}