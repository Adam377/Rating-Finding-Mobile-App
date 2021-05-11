package mmu.ac.uk;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HygieneWebServiceClient
{
    public List<Restaurant> getRatingsByPostcode(String postcode)
    {
        try
        {
            URL url = new URL("http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=search_postcode&postcode=" + postcode);
            getRatingsFromURL(url);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<Restaurant> getRatingsByLocation(double lat, double lng)
    {
        try
        {
            URL url = new URL("http://sandbox.kriswelsh.com/hygieneapi/hygiene.php?op=search_location&lat=" + lat + "&long=" + lng);
            getRatingsFromURL(url);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private List<Restaurant> getRatingsFromURL(URL u)
    {
        new MainActivity.WebServiceClient().execute(u);
        return null;
    }
}