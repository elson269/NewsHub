package com.elsonji.newshub;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.elsonji.newshub.Network.NetworkUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.elsonji.newshub.PageFragment.API_KEY_PARAM;
import static com.elsonji.newshub.PageFragment.NEWS_BASE_URL;
import static com.elsonji.newshub.PageFragment.SORT_BY_PARAM;
import static com.elsonji.newshub.PageFragment.SOURCE_PARAM;

public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListRemoteViewFactory(getApplicationContext(), intent);
    }
}

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<News> mNews;
    public static final String EXTRA_NEWS_URL = "com.elsonji.newshub.NEWS_URL";
    private String mSelectedNewsString;

    public ListRemoteViewFactory(Context context, Intent intent) {
        mContext = context;
        mSelectedNewsString = String.valueOf(intent.getData().getEncodedSchemeSpecificPart());
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {

            URL recipesUrl = createNewsUrl(mSelectedNewsString, "top");
            String jsonResponse = " ";
            try {
                jsonResponse = NetworkUtils.getResponseFromHttpUrl(recipesUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mNews = NetworkUtils.extractNewsFromJson(jsonResponse);
        }
    }

    @Override
    public void onDestroy() {
        if (mNews != null) {
            mNews.clear();
        }
    }

    @Override
    public int getCount() {
        if (mNews == null) {
            return 0;
        } else {
            return mNews.size();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_news_item);
        if (mNews != null && mNews.size() > 0) {
            remoteViews.setTextViewText(R.id.widget_title_text_view, mNews.get(position).getTitle());
            remoteViews.setTextViewText(R.id.widget_description_text_view, mNews.get(position).getDescription());
        }
        Bundle extras = new Bundle();
        extras.putString(EXTRA_NEWS_URL, mNews.get(position).getUrl());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widget_linear_layout, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private URL createNewsUrl(String sourceParam, String sortByParam) {
        URL newsUrl = null;
        Uri builtUri = Uri.parse(NEWS_BASE_URL)
                .buildUpon()
                .appendQueryParameter(SOURCE_PARAM, sourceParam)
                .appendQueryParameter(SORT_BY_PARAM, sortByParam)
                .appendQueryParameter(API_KEY_PARAM, mContext.getString(R.string.api_key))
                .build();

        try {
            newsUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return newsUrl;
    }
}
