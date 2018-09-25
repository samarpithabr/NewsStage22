package com.example.shara.newsstage2;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {

    }

    public static List<News> fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<News> newsData = extractFeatureFromJson(jsonResponse);

        return newsData;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

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
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
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

    private static String readFromStream(InputStream inputStream) throws IOException {
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

    private static List<News> extractFeatureFromJson(String newsJSON) {

        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        List<News> newsArrayList = new ArrayList<>();

        try {

            //TODO: Create a new JSONObject
            JSONObject jsonObj = new JSONObject(newsJSON);
            JSONObject baseResponse = jsonObj.getJSONObject("response");
            // TODO: Get the JSON Array node
            JSONArray result = baseResponse.getJSONArray("results");

            // looping through all Contacts
            for (int i = 0; i < result.length(); i++) {
                //TODO: get the JSONObject
                JSONObject c = result.getJSONObject(i);

                String sectionName = c.getString("sectionName");
                String webPublicationDate = c.getString("webPublicationDate");
                String date = formattedDate(webPublicationDate);
                String time = formatTime(webPublicationDate);

                String webTitle = c.getString("webTitle");
                String webUrl = c.getString("webUrl");
                JSONArray authorarry = c.getJSONArray("tags");
                for (int y = 0; y < authorarry.length(); y++) {
                    JSONObject authorarryJSONObject = authorarry.getJSONObject(y);
                    String authorWebName = authorarryJSONObject.getString("webTitle");


                    News newsfinal = new News(sectionName, date, time, webTitle, webUrl, authorWebName);
                    newsArrayList.add(newsfinal);
                }
            }
        } catch (JSONException e) {

            Log.e("QueryUtils", "Problem parsing the News JSON results", e);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return newsArrayList;
    }

    private static String formatTime(String webPublicationTime) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date articleTime = format.parse(webPublicationTime);
        format.applyPattern("hh:mm");
        webPublicationTime = format.format(articleTime);
        return webPublicationTime;
    }

    private static String formattedDate(String webPublicationDate) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'", Locale.getDefault());
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date articleDate = format.parse(webPublicationDate);
        format.applyPattern("MMM dd, yyyy");
        webPublicationDate = format.format(articleDate);
        return webPublicationDate;
    }


}
