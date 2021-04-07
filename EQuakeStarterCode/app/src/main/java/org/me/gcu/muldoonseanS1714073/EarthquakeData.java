// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeData {
    private static List<Earthquake> earthquakes = new ArrayList<Earthquake>();
    private static String lastBuildDate = "";

    // Used by the parseData method to update the earthquake list
    public static void setEarthquakes(List<Earthquake> earthquakes) {
        EarthquakeData.earthquakes = earthquakes;
    }

    // Used by the parseData method to update the date and time of the last build date
    public static void setLastBuildDate(String lastBuildDate) {
        EarthquakeData.lastBuildDate = lastBuildDate;
    }

    public static List<Earthquake> getEarthquakes() {
        return earthquakes;
    }

    public static String getLastBuildDate() {
        return lastBuildDate;
    }

    // Returns the index of an array in the earthquake list
    public static int getEarthquakeIndex(Earthquake earthquake) {
        for (int i = 0; i < earthquakes.size(); i++) {
            if (earthquake == earthquakes.get(i)) {
                return i;
            }
        }
        return -1;
    }

    // Sorts the array of earthquakes using a specified sorting method
    public static void sort(SortMode sortMode) {
        boolean isSorted = false;
        while (!isSorted) {
            for (int i = 0; i < earthquakes.size() - 1; i++) {
                if (compareForSorting(earthquakes.get(i), earthquakes.get(i + 1), sortMode)) {
                    Earthquake swap = earthquakes.get(i);
                    earthquakes.set(i, earthquakes.get(i + 1));
                    earthquakes.set(i + 1, swap);
                    Log.d("Sorting", "Swapped elements at positions: " + i + " & " + (i + 1));
                    break;
                }
                if (i == earthquakes.size() - 2) {
                    isSorted = true;
                }
            }
        }
    }

    // Compares two earthquakes using a specified sorting method
    private static boolean compareForSorting(Earthquake eq1, Earthquake eq2, SortMode sortMode) {
        switch (sortMode) {
            case MAGNITUDE_ASCENDING:
                return eq1.getMagnitude() > eq2.getMagnitude();
            case MAGNITUDE_DESCENDING:
                return eq1.getMagnitude() < eq2.getMagnitude();
            case DATE_ASCENDING:
                return eq1.getComparableDateTime().compareToIgnoreCase(eq2.getComparableDateTime()) > 0;
            case DATE_DESCENDING:
                return eq1.getComparableDateTime().compareToIgnoreCase(eq2.getComparableDateTime()) < 0;
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

    // Returns a new list of earthquakes within a range of two dates
    public static List<Earthquake> getEarthquakesInRange(String earliestDate, String latestDate)
    {
        List<Earthquake> earthquakesInRange = new ArrayList<Earthquake>();
        for (int i = 0; i < earthquakes.size(); i++) {
            String date = earthquakes.get(i).getComparableDate();
            if (date.compareToIgnoreCase(earliestDate) >= 0 && date.compareToIgnoreCase(latestDate) <= 0)
                earthquakesInRange.add(earthquakes.get(i));
        }
        return earthquakesInRange;
    }

    // Returns the northern-most earthquake
    public static Earthquake getMostNorthernEarthquake(List<Earthquake> earthquakes)
    {
        float maxLat = -90f;
        int resultIndex = -1;
        for (int i = 0; i < earthquakes.size(); i++) {
            float currentLat = earthquakes.get(i).getLatitude();
            if (currentLat >= maxLat) {
                maxLat = currentLat;
                resultIndex = i;
            }
        }
        if (resultIndex == -1)
            return null;
        return earthquakes.get(resultIndex);
    }

    // Returns the eastern-most earthquake
    public static Earthquake getMostEasterlyEarthquake(List<Earthquake> earthquakes)
    {
        float maxLon = -180f;
        int resultIndex = -1;
        for (int i = 0; i < earthquakes.size(); i++) {
            float currentLat = earthquakes.get(i).getLongitude();
            if (currentLat >= maxLon) {
                maxLon = currentLat;
                resultIndex = i;
            }
        }
        if (resultIndex == -1)
            return null;
        return earthquakes.get(resultIndex);
    }

    // Returns the southern-most earthquake
    public static Earthquake getMostSoutherlyEarthquake(List<Earthquake> earthquakes)
    {
        float minLat = 90f;
        int resultIndex = -1;
        for (int i = 0; i < earthquakes.size(); i++) {
            float currentLat = earthquakes.get(i).getLatitude();
            if (currentLat <= minLat) {
                minLat = currentLat;
                resultIndex = i;
            }
        }
        if (resultIndex == -1)
            return null;
        return earthquakes.get(resultIndex);
    }

    // Returns the western-most earthquake
    public static Earthquake getMostWesterlyEarthquake(List<Earthquake> earthquakes)
    {
        float minLon = 180f;
        int resultIndex = -1;
        for (int i = 0; i < earthquakes.size(); i++) {
            float currentLat = earthquakes.get(i).getLongitude();
            if (currentLat <= minLon) {
                minLon = currentLat;
                resultIndex = i;
            }
        }
        if (resultIndex == -1)
            return null;
        return earthquakes.get(resultIndex);
    }

    // Returns the earthquake with the highest magnitude
    public static Earthquake getBiggestEarthquake(List<Earthquake> earthquakes)
    {
        float maxMag = -1f;
        int resultIndex = -1;
        for (int i = 0; i < earthquakes.size(); i++) {
            float currentLat = earthquakes.get(i).getMagnitude();
            if (currentLat > maxMag) {
                maxMag = currentLat;
                resultIndex = i;
            }
        }
        if (resultIndex == -1)
            return null;
        return earthquakes.get(resultIndex);
    }

    // Returns the earthquake with the lowest depth
    public static Earthquake getShallowestEarthquake(List<Earthquake> earthquakes)
    {
        int minDepth = 1000;
        int resultIndex = -1;
        for (int i = 0; i < earthquakes.size(); i++) {
            int currentLat = earthquakes.get(i).getDepth();
            if (currentLat < minDepth) {
                minDepth = currentLat;
                resultIndex = i;
            }
        }
        if (resultIndex == -1)
            return null;
        return earthquakes.get(resultIndex);
    }

    // Returns the earthquake with the lowest depth
    public static Earthquake getDeepestEarthquake(List<Earthquake> earthquakes)
    {
        int maxDepth = -1000;
        int resultIndex = -1;
        for (int i = 0; i < earthquakes.size(); i++) {
            int currentLat = earthquakes.get(i).getDepth();
            if (currentLat > maxDepth) {
                maxDepth = currentLat;
                resultIndex = i;
            }
        }
        if (resultIndex == -1)
            return null;
        return earthquakes.get(resultIndex);
    }
}
