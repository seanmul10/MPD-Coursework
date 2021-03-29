package org.me.gcu.equakestartercode;

import java.util.List;

public class EarthquakeData {
    private List<Earthquake> earthquakes;
    private String lastBuildDate;

    public EarthquakeData(List<Earthquake> earthquakes, String lastBuildDate) {
        this.earthquakes = earthquakes;
        this.lastBuildDate = lastBuildDate;
    }

    public List<Earthquake> getEarthquakeArray() {
        return earthquakes;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }
}
