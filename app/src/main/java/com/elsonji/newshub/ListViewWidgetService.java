package com.elsonji.newshub;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.elsonji.newshub.Network.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static com.elsonji.newshub.PageFragment.createNewsUrl;

public class ListViewWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String string = intent.getStringExtra("newsString");
        return new ListRemoteViewFactory(getApplicationContext(), string);
    }
}

class ListRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<News> mNews;
    private String mNewsString;
    public static final String EXTRA_NEWS_URL = "com.elsonji.newshub.NEWS_URL";

    public ListRemoteViewFactory(Context context, String string ) {
        mContext = context;
        mNewsString = string;
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

//            SharedPreferences selectedWidgetSourcePref = mContext.
//                    getSharedPreferences(WIDGET_SELECTED_STRING, Context.MODE_PRIVATE);
//            String selectedSource = selectedWidgetSourcePref.getString(WIDGET_SELECTED_STRING, null);

            URL recipesUrl = createNewsUrl(mNewsString, "top");
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
}
