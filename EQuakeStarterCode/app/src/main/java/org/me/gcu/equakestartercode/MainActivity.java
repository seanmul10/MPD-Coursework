package org.me.gcu.equakestartercode;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class MainActivity extends EarthquakeActivity implements OnClickListener, AdapterView.OnItemSelectedListener, RecyclerClickListener
{
    private RecyclerAdapter recyclerAdapter;
    private RecyclerView recyclerView;

    private String urlSource="http://quakes.bgs.ac.uk/feeds/MhSeismology.xml";
    //private String urlSource="http://quakes.bgs.ac.uk/feeds/WorldSeismology.xml";

    private Spinner sortSpinner;
    private Button refreshButton;

    private TextView lastBuildDateText;

    private SortMode currentSortMode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Set up the raw links to the graphical components

        sortSpinner = (Spinner)findViewById(R.id.sortSpinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.sort_modes, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setOnItemSelectedListener(this);

        refreshButton = (Button)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(this);

        recyclerView = (RecyclerView)findViewById(R.id.eqRecyclerView);
        recyclerAdapter = new RecyclerAdapter(getApplicationContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(recyclerAdapter);

        lastBuildDateText = (TextView)findViewById(R.id.buildDate);

        startProgress();
    }

    public void onClick(View view)
    {
        if (view == refreshButton) {
            startProgress(currentSortMode);
        }
    }

    // Standard method used to get and display earthquake data, sorts the data by date descending by default
    public void startProgress()
    {
        startProgress(SortMode.DATE_DESCENDING);
    }

    // Gets and displays the earthquake data and sorts it using the specified sort mode
    public void startProgress(SortMode sortMode)
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource, sortMode)).start();
    }

    // Called when a new sort mode has been clicked in the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        currentSortMode = SortMode.getSortMode(position);
        startProgress(currentSortMode);
    }

    public void onNothingSelected(AdapterView<?> parent) { }

    @Override
    public void onRecyclerItemClicked(View view, int position) {
        Log.d("RecyclerView", "Clicked item at position: " + position);
        startDetailedViewActivity(position);
    }

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;
        private SortMode sortMode;

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

            //Log.e("MyTag","in run");

            String rawData = ""; // This is the data that will be parsed and put into the earthquake array
            try
            {
                //Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                //Log.e("MyTag","after ready");
                //
                // Now read the data. Make sure that there are no specific hedrs
                // in the data file that you need to ignore.
                // The useful data that you need is in each of the item entries
                //
                while ((inputLine = in.readLine()) != null)
                {
                    rawData += inputLine;
                    //Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception in run");
            }

            // Parse the data and store it in the EarthquakeData class
            parseData(rawData);

            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    EarthquakeData.setEarthquakes(Earthquake.sort(EarthquakeData.getEarthquakes(), sortMode));

                    recyclerAdapter.notifyDataSetChanged();

                    lastBuildDateText.setText("Earthquake data correct as of " + EarthquakeData.getLastBuildDate());
                }
            });
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