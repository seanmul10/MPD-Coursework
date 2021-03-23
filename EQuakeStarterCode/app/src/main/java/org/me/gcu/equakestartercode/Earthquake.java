package org.me.gcu.equakestartercode;

import android.util.Log;

public class Earthquake
{
    private String location;
    private int day;
    private Month month;
    private int year;
    private String time;
    private float latitude;
    private float longitude;
    private float depth;
    private float magnitude;

    public Earthquake()
    {
        location = "";
        day = 1;
        month = Month.JANUARY;
        year = 2001;
        time = "00:00:00";
        longitude = latitude = depth = magnitude = 0f;
    }

    public Earthquake(String location, int day, String month, int year, String time, float latitude, float longitude, float depth, float magnitude)
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
    public float getDepth() {
        return depth;
    }
    public void setDepth(float depth) {
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
        return day + ordinal + " of " + month + " " + year;
    }

    public static Earthquake[] sort(Earthquake[] earthquakeArray) {
        boolean isSorted = false;
        while (!isSorted) {
            for (int i = 0; i < earthquakeArray.length - 1; i++) {
                if (earthquakeArray[i].getMagnitude() > earthquakeArray[i + 1].getMagnitude()) {
                    Earthquake swap = earthquakeArray[i];
                    earthquakeArray[i] = earthquakeArray[i + 1];
                    earthquakeArray[i + 1] = swap;
                    Log.d("Sorting", "Swapped elements at positions: " + i + " & " + (i + 1));
                    break;
                }
                if (i == earthquakeArray.length - 2) {
                    isSorted = true;
                }
            }
        }
        return earthquakeArray;
    }
}
