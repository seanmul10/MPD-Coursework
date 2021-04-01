package org.me.gcu.equakestartercode;

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
}
