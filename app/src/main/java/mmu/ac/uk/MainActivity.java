package mmu.ac.uk;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    //members
    TextView input, output;
    Button rtgBtn;
    String[] requiredPermissions = {Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    double lat = 0;
    double lng = 0;
    RadioButton postcodeRB;
    RadioButton locationRB;

    //constructor


    //methods
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        input = (TextView) findViewById(R.id.inputText);
        output = (TextView) findViewById(R.id.outputTextView);
        rtgBtn = (Button) findViewById(R.id.getRatingButton);
        postcodeRB = (RadioButton) findViewById(R.id.postcodeRadioButton);
        locationRB = (RadioButton) findViewById(R.id.locationRadioButton);

        locationRB.setChecked(true);

        //Try to access permissions for app to work
        boolean ok = true;
        for(int i=0; i<requiredPermissions.length; i++)
        {
            int result = ActivityCompat.checkSelfPermission(this, requiredPermissions[i]);
            if(result != PackageManager.PERMISSION_GRANTED)
            {
                ok = false;
            }
        }

        //If permission not granted
        if(!ok)
        {
            ActivityCompat.requestPermissions(this, requiredPermissions, 1);
            System.exit(0);
        }
        else
        {
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener()
            {
                @Override
                public void onLocationChanged(Location location)
                {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    //output.setText("MY LOCATION IS: " + lat + ", " + lng);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras)
                {

                }

                @Override
                public void onProviderEnabled(String provider)
                {

                }

                @Override
                public void onProviderDisabled(String provider)
                {

                }
            });
        }
    }

    public void search_onClick(View v)
    {
        String postcode = String.valueOf(input.getText());
        HygieneWebServiceClient hwsc = new HygieneWebServiceClient();

        if(postcodeRB.isChecked())
        {
            hwsc.getRatingsByPostcode(postcode);
        }
        else
        {
            hwsc.getRatingsByLocation(lat, lng);
        }
    }

    public void displayResults(List<Restaurant> r)
    {
        String outStr = "";
        for(Restaurant rest : r)
        {
            outStr += rest.getName() + "\n";
        }
        Log.e("TAG", outStr);

        output.setText(outStr);
    }

    public static class WebServiceClient extends AsyncTask<URL, Void, List<Restaurant>>
    {
        //members


        //constructor


        //methods


        @Override
        protected List<Restaurant> doInBackground(URL... urls)
        {
            List<Restaurant> restaurants = new ArrayList<Restaurant>();
            HttpURLConnection tc = null;

            try
            {
                tc = (HttpURLConnection) urls[0].openConnection();
                InputStreamReader isr = new InputStreamReader(tc.getInputStream());
                BufferedReader in = new BufferedReader(isr);

                String line;
                while((line=in.readLine()) != null)
                {
                    JSONArray ja = new JSONArray(line);
                    for(int i=0; i<ja.length(); i++)
                    {
                        JSONObject jo = (JSONObject) ja.get(i);
                        //outStr += jo.getString("BusinessName") + "; Rating: " + jo.getString("RatingValue") + "\n";
                        Restaurant r = new Restaurant();
                        r.setName(jo.getString("BusinessName"));
                        r.setHygieneRating(jo.getInt("RatingValue"));
                        restaurants.add(r);
                    }
                }
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            return restaurants;
        }

        protected void onPostExecute(List<Restaurant> restaurants)
        {
            new MainActivity().displayResults(restaurants);

        }
    }
}
