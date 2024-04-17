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

public class Home extends Fragment implements View.OnClickListener {

    private TextView rawDataDisplay;
    private Button startButton;
    private String result;
    private String url1="";
    private String urlSource="https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/5128581";
    public WeatherForecast weatherForecast = null;
    private LinkedList<WeatherForecast> forecast;
    boolean useTitle = false;
    private Handler mHandler;

    public Home() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        rawDataDisplay = (TextView) view.findViewById(R.id.rawDataDisplay);
        startButton = (Button) view.findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        forecast = new LinkedList<WeatherForecast>();
        return view;
    }

    public void onClick(View aview)
    {
        startProgress();
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

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
                        weatherForecast = new WeatherForecast();
                        useTitle=true;
                        Log.d("MyTag", "Weather item found");
                    }
                    else if (xpp.getName().equalsIgnoreCase("title") && useTitle)
                    {
                        String temp = xpp.nextText();

                        String[]titleArray = temp.split(",");
                        String Summary = titleArray[0];
                        String day = Summary.split(":")[0].trim();
                        String weather = Summary.split(":")[1].trim();


                        //set description in new class
                        weatherForecast.setDay(day);
                        weatherForecast.setWeather(weather);

                        Log.d("MyTag", "day is: "+ day + "\nweather is: " + weather);
                        Log.d("MyTag", "Weather for " + temp);
                    }
                    else if (xpp.getName().equalsIgnoreCase("description") && useTitle)
                    {
                        String temp = xpp.nextText();

                        String[]descriptionArray = temp.split(",");
                        String maxTemp = descriptionArray[0].trim();
                        String minTemp = descriptionArray[1].trim();
                        String windDirection = descriptionArray[2].trim();
                        String windSpeed = descriptionArray[3].trim();
                        String visibility = descriptionArray[4].trim();
                        String pressure = descriptionArray[5].trim();
                        String humidity = descriptionArray[6].trim();
                        String uvRisk = descriptionArray[7].trim();
                        String pollution = descriptionArray[8].trim();
                        String sunriseTime = descriptionArray[9].trim();
                        String sunsetTime = descriptionArray[10].trim();

                        //set description in new class
                        //weather.setdescription(temp)
                        weatherForecast.setMaxTemp(maxTemp);
                        weatherForecast.setMinTemp(minTemp);
                        weatherForecast.setWindDirection(windDirection);
                        weatherForecast.setWindSpeed(windSpeed);
                        weatherForecast.setVisibility(visibility);
                        weatherForecast.setPressure(pressure);
                        weatherForecast.setHumidity(humidity);
                        weatherForecast.setUvRisk(uvRisk);
                        weatherForecast.setPollution(pollution);
                        weatherForecast.setSunriseTime(sunriseTime);
                        weatherForecast.setSunsetTime(sunsetTime);
                        Log.d("MyTag", "maxTemp is " + maxTemp +
                                "\nminTemp is " + minTemp +
                                "\nwindDirection is " + windDirection +
                                "\nwindSpeed is " + windSpeed +
                                "\nvisibility is " + visibility +
                                "\npressure is " + pressure +
                                "\nhumidity is " + humidity +
                                "\nuvRisk is " + uvRisk +
                                "\npollution is " + pollution +
                                "\nsunriseTime is " + sunriseTime +
                                "\nsunsetTime is " + sunsetTime);
                        Log.d("MyTag", "Temperature info:  " + temp);
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (xpp.getName().equalsIgnoreCase("Item")) {
                        Log.d("MyTag", "Current weather parsing finished");
                        forecast.add(weatherForecast);
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
            Log.e("MyTag - cleaned",result);

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
                    String threeDayWeather = "";
                    for (WeatherForecast d : forecast)
                    {
                        threeDayWeather += d.toString();
                        rawDataDisplay.setText(threeDayWeather);
                    }
                }
            });
        }

    }
}