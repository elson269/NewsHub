package com.elsonji.newshub.Network;

import android.util.Log;

import com.elsonji.newshub.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static ArrayList<News> extractNewsFromJson(String jsonRequestResults) {
        ArrayList<News> news = new ArrayList<>();
        try {
            JSONObject rootJsonResponseObject = new JSONObject(jsonRequestResults);
            JSONArray ArticlesArray = rootJsonResponseObject.getJSONArray("articles");
            for (int i = 0; i < ArticlesArray.length(); i++) {
                JSONObject ArticleObject = ArticlesArray.getJSONObject(i);
                String author = ArticleObject.getString("author");
                String title = ArticleObject.getString("title");
                String description = ArticleObject.getString("description");
                String url = ArticleObject.getString("url");
                String urlToImage = ArticleObject.getString("urlToImage");
                String publishTime = ArticleObject.getString("publishedAt");
                news.add(new News(author, title, description, url, urlToImage, publishTime));
            }
            return news;
        }catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the recipe JSON results", e);
        }
        return null;
    }

    public static URL createUrl(String urlString) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
