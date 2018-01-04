package com.elsonji.newshub;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.elsonji.newshub.Network.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class NewsLoader extends AsyncTaskLoader<ArrayList<News>> {
    String mUrl;
    private ArrayList<News> mNews;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        URL newsUrl = NetworkUtils.createUrl(mUrl);
        String jsonResponse = " ";
        try {
            jsonResponse = NetworkUtils.getResponseFromHttpUrl(newsUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mUrl == null) {
            return null;
        }
        mNews = NetworkUtils.extractNewsFromJson(jsonResponse);
        return mNews;
    }
}
