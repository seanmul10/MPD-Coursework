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

    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;

    TextView dateEditText1;
    TextView dateEditText2;

    Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_date_search);

        earthquakeRange = new ArrayList<>();

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
        }
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