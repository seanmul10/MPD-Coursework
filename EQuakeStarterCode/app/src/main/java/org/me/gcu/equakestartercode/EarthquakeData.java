package org.me.gcu.equakestartercode;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeData {
    private static List<Earthquake> _earthquakes = new ArrayList<Earthquake>();
    private static String _lastBuildDate = "";

    public static void setEarthquakes(List<Earthquake> earthquakes) {
        _earthquakes = earthquakes;
    }

    public static void setLastBuildDate(String lastBuildDate) {
        _lastBuildDate = lastBuildDate;
    }

    public static List<Earthquake> getEarthquakes() {
        return _earthquakes;
    }

    public static String getLastBuildDate() {
        return _lastBuildDate;
    }

    // Sorts the array of earthquakes using a specified sorting method
    public static void sort(SortMode sortMode) {
        boolean isSorted = false;
        while (!isSorted) {
            for (int i = 0; i < _earthquakes.size() - 1; i++) {
                if (compareForSorting(_earthquakes.get(i), _earthquakes.get(i + 1), sortMode)) {
                    Earthquake swap = _earthquakes.get(i);
                    _earthquakes.set(i, _earthquakes.get(i + 1));
                    _earthquakes.set(i + 1, swap);
                    Log.d("Sorting", "Swapped elements at positions: " + i + " & " + (i + 1));
                    break;
                }
                if (i == _earthquakes.size() - 2) {
                    isSorted = true;
                }
            }
        }
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
}
