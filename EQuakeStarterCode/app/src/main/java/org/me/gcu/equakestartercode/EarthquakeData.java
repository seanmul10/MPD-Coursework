package org.me.gcu.equakestartercode;

public class EarthquakeData {
    private Earthquake[] earthquakes;
    private String lastBuildDate;

    public EarthquakeData(Earthquake[] earthquakes, String lastBuildDate) {
        this.earthquakes = earthquakes;
        this.lastBuildDate = lastBuildDate;
    }

    public Earthquake[] getEarthquakeArray() {
        return earthquakes;
    }

    public String getLastBuildDate() {
        return lastBuildDate;
    }
}
