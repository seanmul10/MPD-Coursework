// Code written by Sean Muldoon
// S1714073
// Mobile Platform Development coursework submission

package org.me.gcu.muldoonseanS1714073;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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

    private final String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml"; // Url for the data feed

    private final int updateDelay = 300000; // The length of delay in milliseconds between each attempt to retrieve the earthquake data

    private static boolean isDataParsed = false; // Ensures the data isn't tried to be parsed again needlessly

    // Thread handler and runnable used to call the startTask() method periodically
    private Handler threadHandler = new Handler();
    private Runnable runTaskPeriodically = new Runnable() {
        @Override
        public void run() {
            try {
                startTask();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                threadHandler.postDelayed(this, updateDelay);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Create the navigation menu at the bottom of the screen
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.menuFragmentPlaceholder, new MenuFragment());
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // This will make sure when an activity is started, it will not always try to retrieve the data needlessly
        if (isDataParsed) {
            onAsyncTaskComplete();
        }
        else {
            threadHandler.post(runTaskPeriodically);
        }
    }

    // Starts the detailed view activity for an individual earthquake
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

    // All activities will need this method to update any UI elements before the data has been retrieved
    public abstract void onAsyncTaskStarted();

    // All activities will need this method to update any UI elements once the data has been retrieved
    public abstract void onAsyncTaskComplete();

    // Starts the async task
    public void startTask() {
        AsyncGetDataTask aSyncTask = new AsyncGetDataTask();
        aSyncTask.execute(urlSource);
    }

    // Async class used to get the earthquake data from the feed on a background thread
    private class AsyncGetDataTask extends android.os.AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onAsyncTaskStarted();
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
                Log.e("InternetConnection", "Error retrieving data from url source: " + params[0]);
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

        // Takes raw data as an input and updates the EarthquakeData class
        private void parseData(String data)
        {
            // List used to temporarily store the earthquakes
            List<Earthquake> earthquakeList = new ArrayList<Earthquake>();
            String lastBuildDate = "No build date found";

            // Set up PullParser factory and pass it the raw data
            XmlPullParserFactory factory = null;
            Log.d("Parsing", "Began parsing data feed.");
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
                            if (xpp.getName().equals("item")) {
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
                            }
                            break;
                    }
                    e = xpp.next();
                }
            }
            catch (XmlPullParserException | IOException e) {
                Log.e("Parsing", "Error parsing data feed. EarthquakeData will be updated with default values.");
                e.printStackTrace();
            }
            Log.d("Parsing", "Data feed parsed successfully. Updating EarthquakeData.");

            EarthquakeData.setEarthquakes(earthquakeList);
            EarthquakeData.setLastBuildDate(lastBuildDate);
        }
    }
}
