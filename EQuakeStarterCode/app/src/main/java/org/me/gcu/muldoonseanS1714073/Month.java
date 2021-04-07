// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.util.Log;

// Enum for using the full name of each month
public enum Month
{
    NULL, JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER;

    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    // Returns the full name of a month using its first 3 letters
    public static Month getMonth(String month) {
        switch (month) {
            case "Jan":
                return Month.JANUARY;
            case "Feb":
                return Month.FEBRUARY;
            case "Mar":
                return Month.MARCH;
            case "Apr":
                return Month.APRIL;
            case "May":
                return Month.MAY;
            case "Jun":
                return Month.JUNE;
            case "Jul":
                return Month.JULY;
            case "Aug":
                return Month.AUGUST;
            case "Sep":
                return Month.SEPTEMBER;
            case "Oct":
                return Month.OCTOBER;
            case "Nov":
                return Month.NOVEMBER;
            case "Dec":
                return Month.DECEMBER;
            default:
                Log.e("Error", "Invalid month, must be in the format Jan, Feb, Mar etc.");
                return Month.NULL;
        }
    }
}