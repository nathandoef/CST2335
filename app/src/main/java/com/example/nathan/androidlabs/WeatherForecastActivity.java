package com.example.nathan.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecastActivity extends Activity {

    private static final String ACTIVITY_NAME = "WeatherForecastActivity";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather" +
            "?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";

    private ImageView ivForecast;
    private TextView tvMinTemp;
    private TextView tvMaxTemp;
    private TextView tvCurrTemp;
    private ProgressBar pbWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ivForecast = (ImageView)findViewById(R.id.ivForecast);
        tvMinTemp = (TextView)findViewById(R.id.tvMinTemp);
        tvMaxTemp = (TextView)findViewById(R.id.tvMaxTemp);
        tvCurrTemp = (TextView)findViewById(R.id.tvCurrTemp);

        pbWeather = (ProgressBar)findViewById(R.id.pbWeather);

        ForecastQuery fq = new ForecastQuery();

        try {
            fq.execute(API_URL);
        }
        catch(Exception e){
            Log.e("ERROR", "Exception");
        }
    }

    protected class ForecastQuery extends AsyncTask<String, Integer, String>{

        protected String minTemp;
        protected String maxTemp;
        protected String currTemp;
        protected Bitmap weatherPic;

        @Override
        protected String doInBackground(String ... args) {

            InputStream iStream;
            HttpURLConnection conn = null;

            try {
                URL url = new URL(args[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);     // milliseconds
                conn.setConnectTimeout(15000);  // milliseconds
                conn.setRequestMethod("GET");
                conn.setDoInput(true);          // input data from weather server
                conn.connect();                 // starts the query

                if (conn.getResponseCode() != 200)
                    return "HTTP ERROR";

                iStream = conn.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(iStream, "UTF-8");

                while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {

                    switch (parser.getEventType()) {

                        case XmlPullParser.START_TAG:
                            if (parser.getName().equals("temperature")){

                                minTemp = parser.getAttributeValue(null, "min");
                                publishProgress(25);  // update ProgressBar
                                Thread.sleep(1000);

                                maxTemp = parser.getAttributeValue(null, "max");
                                publishProgress(50);
                                Thread.sleep(1000);

                                currTemp = parser.getAttributeValue(null, "value");
                                publishProgress(75);
                                Thread.sleep(1000);
                            }

                            if (parser.getName().equals("weather")) {
                                String iconName = parser.getAttributeValue(null, "icon");
                                String fileName = iconName + ".png";

                                //File file = getBaseContext().getFileStreamPath(fileName);
                                //file.delete();

                                Log.i("MESSAGE", "Looking for file: " + fileName);

                                if (fileExists(fileName)){
                                    FileInputStream fis = null;
                                    fis = openFileInput(iconName + ".png");
                                    weatherPic = BitmapFactory.decodeStream(fis);
                                    Log.i("MESSAGE", "Loaded image locally");
                                }
                                else {
                                    weatherPic = downloadBitmapFrom(fileName);
                                    Log.i("MESSAGE", "Downloaded image from server");
                                }

                                publishProgress(100);
                            }
                            break;
                    }
                    parser.next();
                }

            } catch (Exception e) {
                return "";
            }
            finally {
                if (conn != null)
                    conn.disconnect();
            }
            return "";
        }

        @Override
        protected void onProgressUpdate(Integer ... value){
            pbWeather.setVisibility(View.VISIBLE);
            pbWeather.setProgress(value[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            tvMinTemp.setText(tvMinTemp.getText() + " " + minTemp + "°C");
            tvMaxTemp.setText(tvMaxTemp.getText() + " " + maxTemp + "°C");
            tvCurrTemp.setText(tvCurrTemp.getText() + " " + currTemp + "°C");
            ivForecast.setImageBitmap(weatherPic);
            pbWeather.setVisibility(View.INVISIBLE);
        }

        protected Bitmap downloadBitmapFrom(String fileName){
            HttpURLConnection conn = null;
            Bitmap image = null;

            try {
                URL url = new URL("http://openweathermap.org/img/w/" + fileName);
                conn = (HttpURLConnection) url.openConnection();
                if (conn.getResponseCode() == 200){
                    image = BitmapFactory.decodeStream(conn.getInputStream());
                    FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                }
                return image;
            }
            catch (Exception e){
                return null;
            }
            finally {
                if (conn != null)
                    conn.disconnect();
            }
        }

        protected boolean fileExists(String fName){
            File file = getBaseContext().getFileStreamPath(fName);
            return file.exists();
        }
    }
}
