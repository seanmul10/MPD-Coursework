package org.me.gcu.equakestartercode;

import android.util.Log;

import java.util.List;

public class Earthquake
{
    private String location;
    private int day;
    private Month month;
    private int year;
    private String time;
    private float latitude;
    private float longitude;
    private int depth;
    private float magnitude;

    public Earthquake()
    {
        location = "";
        day = 1;
        month = Month.JANUARY;
        year = 2001;
        time = "00:00:00";
        longitude = latitude = magnitude = 0f;
        depth = 0;
    }

    public Earthquake(String location, int day, String month, int year, String time, float latitude, float longitude, int depth, float magnitude)
    {
        this.location = location;
        this.day = day;
        this.month = Month.getMonth(month);
        this.year = year;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
        this.depth = depth;
        this.magnitude = magnitude;
    }

    // Getter and setter for the location
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    // Getter and setter for the day
    public int getDay() {
        return day;
    }
    public void setDay(int day) {
        this.day = day;
    }

    // Getter and setter for the month
    public String getMonth() {
        return month.toString();
    }
    public int getMonthValue() { return month.ordinal() + 1; }
    public void setMonth(String month) {
        this.month = Month.getMonth(month);
    }

    // Getter and setter for the year
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    // Getter and setter for the time
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    // Getter and setter for the latitude and longitude
    public float[] getLatLng() {
        return new float[] { latitude, longitude };
    }
    public float getLatitude() { return latitude; }
    public float getLongitude() { return longitude; }
    public void setLatLng(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter and setter for the depth
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth) {
        this.depth = depth;
    }

    // Getter and setter for the magnitude
    public float getMagnitude() {
        return magnitude;
    }
    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    // Additional data retrieval methods
    public String getDate() {
        String ordinal;
        if (day > 3 && day < 21) // All dates between 4th and 20th end in "th"
            ordinal = "th";
        else {
            switch (day % 10) {
                case 1:
                    ordinal = "st";
                    break;
                case 2:
                    ordinal = "nd";
                    break;
                case 3:
                    ordinal = "rd";
                    break;
                default:
                    ordinal = "th";
                    break;
            }
        }
        return day + ordinal + " of " + month + " " + year;
    }

    // Sorts the array of earthquakes using a specified sorting method
    public static List<Earthquake> sort(List<Earthquake> earthquakeList, SortMode sortMode) {
        boolean isSorted = false;
        while (!isSorted) {
            for (int i = 0; i < earthquakeList.size() - 1; i++) {
                if (compareForSorting(earthquakeList.get(i), earthquakeList.get(i + 1), sortMode)) {
                    Earthquake swap = earthquakeList.get(i);
                    earthquakeList.set(i, earthquakeList.get(i + 1));
                    earthquakeList.set(i + 1, swap);
                    Log.d("Sorting", "Swapped elements at positions: " + i + " & " + (i + 1));
                    break;
                }
                if (i == earthquakeList.size() - 2) {
                    isSorted = true;
                }
            }
        }
        return earthquakeList;
    }

    private static boolean compareForSorting(Earthquake eq1, Earthquake eq2, SortMode sortMode) {
        switch (sortMode) {
            case MAGNITUDE_ASCENDING:
                return eq1.getMagnitude() > eq2.getMagnitude();
            case MAGNITUDE_DESCENDING:
                return eq1.getMagnitude() < eq2.getMagnitude();
            case DATE_ASCENDING:
                return eq1.getDateAndTimeString().compareToIgnoreCase(eq2.getDateAndTimeString()) > 0;
            case DATE_DESCENDING:
                return eq1.getDateAndTimeString().compareToIgnoreCase(eq2.getDateAndTimeString()) < 0;
            case ALPHABETICAL_ASCENDING:
                return eq1.getLocation().compareToIgnoreCase(eq2.getLocation()) < 0;
            case ALPHABETICAL_DESCENDING:
                return eq2.getLocation().compareToIgnoreCase(eq1.getLocation()) < 0;
            case DEPTH_ASCENDING:
                return eq1.getDepth() > eq2.getDepth();
            case DEPTH_DESCENDING:
                return eq1.getDepth() < eq2.getDepth();
        }
        return false;
    }

    // Returns date and time in the format YYYY:MM:DD:HH:MM:SS, this is used to sort the earthquakes by date and time
    private String getDateAndTimeString() {
        return getYear() + ":" + String.format("%02d", getMonthValue()) + ":" + String.format("%02d", getDay()) + ":" + getTime();
    }
}
