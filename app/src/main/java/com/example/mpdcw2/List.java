package com.example.mpdcw2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

public class List extends Fragment implements View.OnClickListener {

    private Button btnNext;
    private Button reFreshbtn;
    private Button btnBack;
    private Button glasgowButton;
    private Button newYorkButton;
    private Button londonButton;
    private Button omanButton;
    private Button mauritiusButton;
    private Button bangButton;
    private TextView glasgowWeather;
    private TextView newYorkWeather;
    private TextView unitedKingdomWeather;
    private TextView omanWeather;
    private TextView mauritiusWeather;
    private TextView bangWeather;
    private ViewFlipper campusFlipper;
    private String result;
    public LatestWeather latestWeather = null;
    private LinkedList<LatestWeather> latest;
    boolean useTitle = false;
    private Handler mHandler;

    public List() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list, container, false);

        reFreshbtn = (Button) view.findViewById(R.id.reFreshbtn);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        glasgowButton = (Button) view.findViewById(R.id.glasgowButton);
        newYorkButton = (Button) view.findViewById(R.id.newYorkButton);
        londonButton = (Button) view.findViewById(R.id.londonButton);
        omanButton = (Button) view.findViewById(R.id.omanButton);
        mauritiusButton = (Button) view.findViewById(R.id.mauritiusButton);
        bangButton = (Button) view.findViewById(R.id.bangButton);
        btnNext.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        reFreshbtn.setOnClickListener(this);
        glasgowButton.setOnClickListener(this);
        newYorkButton.setOnClickListener(this);
        londonButton.setOnClickListener(this);
        omanButton.setOnClickListener(this);
        mauritiusButton.setOnClickListener(this);
        bangButton.setOnClickListener(this);
        campusFlipper = (ViewFlipper) view.findViewById(R.id.campusFlipper);
        glasgowWeather = (TextView) view.findViewById(R.id.glasgowWeather);
        newYorkWeather = (TextView) view.findViewById(R.id.newYorkWeather);
        unitedKingdomWeather = (TextView) view.findViewById(R.id.unitedKingdomWeather);
        omanWeather = (TextView) view.findViewById(R.id.omanWeather);
        mauritiusWeather = (TextView) view.findViewById(R.id.mauritiusWeather);
        bangWeather = (TextView) view.findViewById(R.id.bangWeather);
        latest = new LinkedList<LatestWeather>();
        return view;
    }

    public void onClick(View aview){
        if (aview == btnNext){
            campusFlipper.showNext();
            Log.d("MyTag", "next campus");
        }
        else if  (aview == btnBack){
            campusFlipper.showPrevious();
            Log.d("MyTag", "Previous Campus");
        }
        else if (aview == glasgowButton){
            String newYorkID = "2648579";
            new Thread(new Task(newYorkID, glasgowWeather)).start();
        }
        else if (aview == newYorkButton){
            String newYorkID = "5128581";
            new Thread(new Task(newYorkID, newYorkWeather)).start();
        }
        else if (aview == londonButton){
            String londonID = "2643743";
            new Thread(new Task(londonID, unitedKingdomWeather)).start();
        }
        else if (aview == omanButton){
            String omanID = "287286";
            new Thread(new Task(omanID, omanWeather)).start();
        }
        else if (aview == mauritiusButton){
            String mauritiusID = "934154";
            new Thread(new Task(mauritiusID, mauritiusWeather)).start();
        }
        else if (aview == bangButton){
            String bangID = "1185241";
            new Thread(new Task(bangID, bangWeather)).start();
        }
        else if (aview == reFreshbtn){
            getActivity().recreate();
        }
    }


    private void parseData(String result)
    {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(result));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("Item")) {
                        latestWeather = new LatestWeather();
                        useTitle=true;
                        Log.d("MyTag", "Weather item found");
                    }
                    else if (xpp.getName().equalsIgnoreCase("title") && useTitle)
                    {
                        String temp = xpp.nextText();

                        String[]titleArray = temp.split(",");
                        String Summary = titleArray[0];
                        String day = Summary.split(":")[0].trim();
                        //String weather = Summary.split(":")[1].trim();


                        //set description in new class
                        latestWeather.setDay(day);

                        Log.d("MyTag", "day is: "+ day);
                    }
                    else if (xpp.getName().equalsIgnoreCase("description") && useTitle)
                    {
                        String temp = xpp.nextText();

                        String[]currentArray = temp.split(",");

                        int count = 0;
                        for (String j : currentArray){
                            if (currentArray[count].contains("Temperature")) {
                                String currentTemperature = currentArray[count].trim();
                                latestWeather.setCurrentTemperature(currentTemperature);
                                Log.d("MyTag", "Current Temperature is: " + currentTemperature);
                                count++;
                            }
                            if (currentArray[count].contains("Wind Direction")) {
                                String windDirection = currentArray[count].trim();
                                latestWeather.setWindDirection(windDirection);
                                Log.d("MyTag", "Wind Direction is: " + windDirection);
                                count++;
                            }
                            if (currentArray[count].contains("Wind Speed")) {
                                String windSpeed = currentArray[count].trim();
                                latestWeather.setWindSpeed(windSpeed);
                                Log.d("MyTag", "Wind Speed is: " + windSpeed);
                                count++;
                            }
                            if (currentArray[count].contains("Humidity")) {
                                String humidity = currentArray[count].trim();
                                latestWeather.setHumidity(humidity);
                                Log.d("MyTag", "Humidity is: " + humidity);
                                count++;
                            }
                            if (currentArray[count].contains("Pressure")) {
                                String pressure = currentArray[count].trim();
                                latestWeather.setPressure(pressure);
                                Log.d("MyTag", "Pressure is: " + pressure);
                                count++;
                            }
                            if (currentArray[count].contains("Visibility")) {
                                String visibility = currentArray[count].trim();
                                latestWeather.setVisibility(visibility);
                                Log.d("MyTag", "Visibility is: " + visibility);
                                count++;
                            }
                        }
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("Item")) {
                        Log.d("MyTag", "Current weather parsing finished");
                        latest.add(latestWeather);
                    }
                }
                eventType = xpp.next();
            }
        }
        catch(XmlPullParserException ae1)
        {
            Log.e("MyTag","Parsing error" + ae1.toString());
        }
        catch(IOException ae1)
        {
            Log.e("MyTag", "IO error during parsing");
        }

        Log.d("MyTag", "end of document");
    }

    private class Task implements Runnable
    {
        String url = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/";
        TextView display;
        public Task(String rss, TextView text)
        {
            url = url + rss;
            display = text;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.e("MyTag","in run");

            try
            {
                Log.e("MyTag","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;
                    Log.e("MyTag",inputLine);

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }

            //Get rid of the first tag <?xml version="1.0" encoding="utf-8"?>
            int i = result.indexOf(">");
            result = result.substring(i+1);

            parseData(result);
            //
            // Now that you have the xml data you can parse it
            //



            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");
                    //rawDataDisplay.setText(currentWeather.toString());
                    String currentWeather = "";

                    for (LatestWeather d : latest)
                    {
                        currentWeather += d.toString();
                        display.setText(currentWeather);
                    }
                }
            });
        }

    }
}