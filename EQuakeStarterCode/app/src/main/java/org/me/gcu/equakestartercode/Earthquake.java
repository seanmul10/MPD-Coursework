package org.me.gcu.equakestartercode;

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
    public int getMonthValue() { return month.ordinal(); }
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
    public String getLatLngString() { return Math.abs(latitude) + (latitude < 0 ? "°S" : "°N") + ", " + Math.abs(longitude) + (longitude < 0 ? "°W" : "°E"); }
    public void setLatLng(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getter and setter for the depth
    public int getDepth() {
        return depth;
    }
    public String getDepthString() { return depth + "km"; }
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

    // Returns date and time in the format YYYY:MM:DD:HH:MM:SS, this is used to sort the earthquakes by date and time
    public String getComparableDateTime() {
        return getComparableDate() + ":" + getTime();
    }

    // Returns date and time in the format YYYY:MM:DD, this is used to sort the earthquakes by date
    public String getComparableDate() {
        return getYear() + ":" + String.format("%02d", getMonthValue()) + ":" + String.format("%02d", getDay());
    }
}
