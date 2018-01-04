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
    public static final String URL_TO_IMAGE = "urlToImage";
    public static final String URL = "url";
    public static final String DESCRIPTION = "description";
    public static final String TITLE = "title";
    public static final String AUTHOR = "author";
    public static final String ARTICLES = "articles";
    public static final String A = "\\A";
    public static final String PROBLEM_PARSING_THE_NEWS_JSON_RESULTS = "Problem parsing the news JSON results";
    public static final String PUBLISHED_AT = "publishedAt";

    public static ArrayList<News> extractNewsFromJson(String jsonRequestResults) {
        ArrayList<News> news = new ArrayList<>();
        try {
            JSONObject rootJsonResponseObject = new JSONObject(jsonRequestResults);
            JSONArray ArticlesArray = rootJsonResponseObject.getJSONArray(ARTICLES);
            for (int i = 0; i < ArticlesArray.length(); i++) {
                JSONObject ArticleObject = ArticlesArray.getJSONObject(i);
                String author = ArticleObject.getString(AUTHOR);
                String title = ArticleObject.getString(TITLE);
                String description = ArticleObject.getString(DESCRIPTION);
                String url = ArticleObject.getString(URL);
                String urlToImage = ArticleObject.getString(URL_TO_IMAGE);
                String publishTime = ArticleObject.getString(PUBLISHED_AT);
                news.add(new News(author, title, description, url, urlToImage, publishTime));
            }
            return news;
        }catch (JSONException e) {
            Log.e(LOG_TAG, PROBLEM_PARSING_THE_NEWS_JSON_RESULTS, e);
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
            scanner.useDelimiter(A);

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
