package org.me.gcu.equakestartercode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemSelectedListener
{
    public static Earthquake[] earthquakes;

    private RecyclerView recyclerView;

    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";

    private Spinner sortSpinner;

    private Button refreshButton;
    private Button mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the raw links to the graphical components
        recyclerView = (RecyclerView)findViewById(R.id.eqRecyclerView);

        sortSpinner = (Spinner)findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_modes, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setOnItemSelectedListener(this);

        refreshButton = (Button)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);

        mapButton = (Button)findViewById(R.id.mapButton);
        mapButton.setOnClickListener(this);

        startProgress();
    }

    public void onClick(View view)
    {
        if (view == refreshButton) {
            recyclerView.setAdapter(null);
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
    }

    public void startProgress(SortMode sortMode)
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource, sortMode)).start();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startProgress(SortMode.DATE_DESCENDING);
                break;
            case 1:
                startProgress(SortMode.DATE_ASCENDING);
                break;
            case 2:
                startProgress(SortMode.MAGNITUDE_DESCENDING);
                break;
            case 3:
                startProgress(SortMode.MAGNITUDE_ASCENDING);
                break;
            case 4:
                startProgress(SortMode.ALPHABETICAL_DESCENDING);
                break;
            case 5:
                startProgress(SortMode.ALPHABETICAL_ASCENDING);
                break;
            case 6:
                startProgress(SortMode.DEPTH_DESCENDING);
                break;
            case 7:
                startProgress(SortMode.DEPTH_ASCENDING);
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) { }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;
        private SortMode sortMode;

        public Task(String url)
        {
            this.url = url;
            this.sortMode = SortMode.DATE_DESCENDING;
        }
        public Task(String url, SortMode sortMode)
        {
            this.url = url;
            this.sortMode = sortMode;
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

                    earthquakes = Earthquake.sort(earthquakes, sortMode);

                    /*
                    eqView.setBackgroundColor(Color.parseColor(MagnitudeColourCoding.getColour(earthquake.getMagnitude())));
                    eqLocation.setText("Location: " + earthquake.getLocation());
                    eqMagnitude.setText("Magnitude: " + earthquake.getMagnitude());
                    eqDateTime.setText("Occurred on: " + earthquake.getDate() + " at " + earthquake.getTime());
                    eqLatLong.setText("LatLng: " + earthquake.getLatitude() + ", " + earthquake.getLongitude());
                    eqDepth.setText("Depth: " + earthquake.getDepth() + "km");
                     */
                    RecyclerAdapter adapter = new RecyclerAdapter(earthquakes);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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