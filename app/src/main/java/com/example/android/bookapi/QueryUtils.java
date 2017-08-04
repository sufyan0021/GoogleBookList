package com.example.android.bookapi;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.bookapi.MainActivity.LOG_TAG;

/**
 * Created by sufya on 03-08-2017.
 */

public class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private static String tle,author,rprice,prlink;
    private static Uri imglink;
    private QueryUtils() {
    }

    public static List<Book_Data> fetchBooksData(String requestUrl) {

        Log.v("fetchBooksData","url received");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List<Book_Data>earthquake = extractBooks(jsonResponse);

        // Return the {@link Event}
        return earthquake;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {

        Log.v("createUrl","url created");

        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        Log.v("makeHttpRequest","Http Request made");

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        Log.v("readFromStream","input stream object data being read");

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Book_Data} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Book_Data> extractBooks(String booklistJSON) {

        Log.v("extractBooks","JsonResponse extracted");

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(booklistJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Book_Data> books = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Books objects with the corresponding data.
            JSONObject baseJsonResponse=new JSONObject(booklistJSON);
            JSONArray ab=baseJsonResponse.getJSONArray("items");

            for(int i=0;i<ab.length();i++)
            {
                JSONObject c=ab.getJSONObject(i);
                JSONObject d=c.getJSONObject("volumeInfo");
                JSONObject im=d.getJSONObject("imageLinks");
                Uri imglink=Uri.parse(im.getString("thumbnail"));
                //int imglink=R.mipmap.ic_launcher;
                JSONArray p=d.getJSONArray("authors");
                author=p.getString(0);
                 tle=d.getString("title");
                JSONObject s=c.getJSONObject("saleInfo");
                if(s.has("listPrice")){
                    JSONObject cp=s.getJSONObject("listPrice");
                 rprice=cp.getString("amount");}
                else
                    rprice="NA";
                if(s.has("buyLink"))
                 prlink=s.getString("buyLink");
                else
                    prlink="NA";
                Log.v("stage1","finished parsing"+imglink);
                Book_Data book=new Book_Data(imglink,tle,author,rprice,prlink);
                Log.v("stage1","finished parsing"+imglink);
                books.add(book);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the books JSON results", e);
        }

        // Return the list of earthquakes
        return books;
    }
}
