package com.example.android.news;

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

/**
 * Created by James on 6/26/18.
 */

public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils(){

    }

    public static List<Article> fetchArticleData(String requestUrl){

        URL url = createUrl(requestUrl);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException error){
            Log.e(LOG_TAG, "Error making HTTP request.", error);
        }

        List<Article> articles = extractFeatureFromJson(jsonResponse);
        return articles;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the Guardian JSON results.", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();

            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Article> extractFeatureFromJson(String articleJson){
        if (TextUtils.isEmpty(articleJson)){
            return null;
        }
        List<Article> articles = new ArrayList<>();

        try {
            JSONObject jsonObj = new JSONObject(articleJson);

            JSONObject responseObj = jsonObj.getJSONObject("response");

            JSONArray articleArray = responseObj.getJSONArray("results");

            for (int i = 0; i < articleArray.length(); i++) {
                JSONObject result = articleArray.getJSONObject(i);
                String title = result.getString("webTitle");
                String section = result.getString("sectionName");
                String date = result.getString("webPublicationDate");
                String url = result.getString("webUrl");
                JSONArray tagArray = result.getJSONArray("tags");
                String author = null;
                if (tagArray != null){
                    for (int j = 0; j < tagArray.length(); j++){
                        JSONObject tag = tagArray.getJSONObject(j);
                        author = tag.getString("webTitle");
                        System.out.println(author);
                    }
                }
                Article current = new Article(title, section, date, url, author);
                articles.add(current);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the Guardian JSON results", e);
        }
        return articles;
    }
}
