package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import java.util.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    private LinearLayout eqView;
    private TextView eqLocation;
    private TextView eqMagnitude;
    private TextView eqDateTime;
    private TextView eqLatLong;
    private TextView eqDepth;
    private Button startButton;
    private String url1="";
    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    public static Earthquake[] earthquakes;

    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e("MyTag","in onCreate");
        // Set up the raw links to the graphical components
        eqView = (LinearLayout)findViewById(R.id.eqView);
        eqLocation = (TextView)findViewById(R.id.eqLocation);
        eqMagnitude = (TextView)findViewById(R.id.eqMagnitude);
        eqDateTime = (TextView)findViewById(R.id.eqDateTime);
        eqLatLong = (TextView)findViewById(R.id.eqLatLong);
        eqDepth = (TextView)findViewById(R.id.eqDepth);

        startButton = (Button)findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        Log.e("MyTag","after startButton");
        // More Code goes here
        mapButton = (Button)findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);

        startProgress();
    }

    public void onClick(View view)
    {
        if (view == startButton) {
            startProgress();
        }
        if (view == mapButton) {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
/*
            int length = earthquakes.length;
            String[] locations = new String[length];
            String[] dateTimes = new String[length];
            float[] latitudes = new float[length];
            float[] longitudes = new float[length];
            float[] magnitudes = new float[length];
            float[] depths = new float[length];
            for (int i = 0; i < length; i++) {
                locations[i] = earthquakes[i].getLocation();
                dateTimes[i] = earthquakes[i].getDate();
                latitudes[i] = earthquakes[i].getLatitude();
                longitudes[i] = earthquakes[i].getLongitude();
                magnitudes[i] = earthquakes[i].getMagnitude();
                depths[i] = earthquakes[i].getDepth();
            }
            intent.putExtra("earthquakeLocations", locations);
            intent.putExtra("earthquakeDateAndTimes", dateTimes);
            intent.putExtra("earthquakeLatitudes", latitudes);
            intent.putExtra("earthquakeLongitudes", longitudes);
            intent.putExtra("earthquakeMagnitudes", magnitudes);
            intent.putExtra("earthquakeDepths", depths);
*/
            startActivity(intent);
        }
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;

        public Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";

            Log.e("MyTag","in run");

            String rawData = ""; // This is the data that will be parsed and put into the earthquake array
            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                Log.e("MyTag","after ready");
                //
                // Now read the data. Make sure that there are no specific hedrs
                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries
                //
                while ((inputLine = in.readLine()) != null)
                {
                    rawData += inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            //
            // Now that you have the xml data you can parse it
            //
            earthquakes = ParseData(rawData);

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    // Delete randomness
                    Random rand = new Random();
                    int r = rand.nextInt(earthquakes.length - 1);

                    Earthquake earthquake = earthquakes[r];
                    eqView.setBackgroundColor(Color.parseColor(MagnitudeColourCoding.getColour(earthquake.getMagnitude())));
                    eqLocation.setText("Location: " + earthquake.getLocation());
                    eqMagnitude.setText("Magnitude: " + earthquake.getMagnitude());
                    eqDateTime.setText("Occurred on: " + earthquake.getDate() + " at " + earthquake.getTime());
                    eqLatLong.setText("LatLng: " + earthquake.getLatitude() + ", " + earthquake.getLongitude());
                    eqDepth.setText("Depth: " + earthquake.getDepth() + "km");
                }
            });
        }

        // Takes raw data as an input and returns an array of earthquakes
        private Earthquake[] ParseData(String data)  {
            // List used to temporarily store the earthquakes
            List<Earthquake> earthquakeList = new ArrayList<Earthquake>();

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

                                    currentEarthquake.setLocation(desc[3].trim());
                                    currentEarthquake.setLatLng(Float.parseFloat(latLong[0]), Float.parseFloat(latLong[1]));
                                    currentEarthquake.setDepth(Float.parseFloat(desc[7].trim().substring(0, desc[7].trim().length() - 2)));
                                    currentEarthquake.setMagnitude(Float.parseFloat(desc[9].trim()));
                                    Log.d("Parsing", "Parsed earthquake data");
                                }
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
            // Convert the list to an array and return it
            return earthquakeList.toArray(new Earthquake[earthquakeList.size()]);
        }
    }
}