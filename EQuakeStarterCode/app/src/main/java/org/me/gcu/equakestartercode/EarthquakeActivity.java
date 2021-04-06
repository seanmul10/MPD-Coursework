package org.me.gcu.equakestartercode;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public abstract class EarthquakeActivity extends FragmentActivity {

    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    private static boolean isDataParsed = false; // Ensures the data isn't tried to be parsed again needlessly

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.menuFragmentPlaceholder, new MenuFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (isDataParsed) {
            onAsyncTaskComplete();
        }
        else {
            startProgress();
        }
    }

    public void startDetailedViewActivity(int index) {
        Intent intent = new Intent(this, DetailedEarthquakeActivity.class);
        intent.putExtra("earthquakeIndex", index);
        if (intent != null)
            startActivity(intent);
    }

    // Checks if the device is currently in dark mode
    public static boolean isDarkMode(Context context) {
        return (context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    // Standard method used to get and display earthquake data
    public void startProgress()
    {
        AsyncGetDataTask aSyncTask = new AsyncGetDataTask();
        aSyncTask.execute(urlSource);
    }

    public abstract void onAsyncTaskComplete();

    private class AsyncGetDataTask extends android.os.AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String rawData = ""; // This is the data that will be parsed and put into the earthquake array
            try
            {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null)
                {
                    rawData += line;
                }
                reader.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
                Log.e("Threading", "Error retrieving data from url source: " + params[0]);
            }

            return rawData;
        }

        @Override
        protected void onPostExecute(String rawData) {
            if (rawData == "")
                return;
            parseData(rawData);
            onAsyncTaskComplete();
            isDataParsed = true;
        }

        // Takes raw data as an input and returns an array of earthquakes
        private void parseData(String data)  {
            // List used to temporarily store the earthquakes
            List<Earthquake> earthquakeList = new ArrayList<Earthquake>();
            String lastBuildDate = "No build date found";

            // Set up PullParser factory and pass it the raw data
            XmlPullParserFactory factory = null;
            try {
                factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(data));

                // Parse the data
                int e = xpp.getEventType();
                Earthquake currentEarthquake = new Earthquake();
                while (e != XmlPullParser.END_DOCUMENT) {
                    switch (e) {
                        case XmlPullParser.START_TAG:
                            Log.d("Parsing", "START TAG " + xpp.getName());
                            if (xpp.getName().equals("item")) {
                                Log.d("Parsing", "Starting new earthquake");
                                currentEarthquake = new Earthquake();
                            }
                            else if (xpp.getName().equals("description")) {
                                xpp.next();

                                String[] desc = xpp.getText().split(": |;");
                                if (desc.length != 1) { // Do not parse description if it has length of 1, this is the channel description and should be ignored
                                    String[] timeAndDate = desc[1].trim().split(" ");
                                    String[] latLong = desc[5].trim().split(",");

                                    currentEarthquake.setDay(Integer.parseInt(timeAndDate[1]));
                                    currentEarthquake.setMonth(timeAndDate[2]);
                                    currentEarthquake.setYear(Integer.parseInt(timeAndDate[3]));
                                    currentEarthquake.setTime(timeAndDate[4]);

                                    // The location of an earthquake might not be specified as soon as the data becomes available
                                    // In this case set the location name to unknown
                                    String location = desc[3].trim();
                                    if (location.equals(""))
                                        currentEarthquake.setLocation("UNKNOWN LOCATION");
                                    else
                                        currentEarthquake.setLocation(location.replace(",", ", "));
                                    currentEarthquake.setLatLng(Float.parseFloat(latLong[0]), Float.parseFloat(latLong[1]));
                                    currentEarthquake.setDepth(Integer.parseInt(desc[7].trim().substring(0, desc[7].trim().length() - 2).trim()));
                                    currentEarthquake.setMagnitude(Float.parseFloat(desc[9].trim()));
                                    Log.d("Parsing", "Parsed earthquake data");
                                }
                            }
                            else if (xpp.getName().equals("lastBuildDate")) {
                                xpp.next();
                                lastBuildDate = xpp.getText();
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if (xpp.getName().equals("item")) {
                                earthquakeList.add(currentEarthquake);
                                Log.d("Parsing", "Added new earthquake");
                            }
                            break;
                    }
                    e = xpp.next();
                }
            }
            catch (XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
            Log.d("Parsing", "Finished parsing document");

            EarthquakeData.setEarthquakes(earthquakeList);
            EarthquakeData.setLastBuildDate(lastBuildDate);
        }
    }
}
