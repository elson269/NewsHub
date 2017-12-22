package com.elsonji.newshub;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

public class NewsWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            String newsString = WidgetConfigureActivity.getNewsSourceFromConfig();
            updateAppWidget(context, appWidgetManager, appWidgetId, newsString);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String string) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.news_widget_provider);

        Intent intent = new Intent(context, ListViewWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.putExtra("newsString", string);
        //setData is used to distinguish different intents as relying on various extras is not sufficient to make a different intent.
        intent.setData(Uri.fromParts("content", String.valueOf(appWidgetId), null));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list_view);
        remoteViews.setRemoteAdapter(R.id.widget_list_view, intent);

        Intent newsIntent = new Intent(context, WebViewActivity.class);
        newsIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setData is used to distinguish different intents.
        newsIntent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, newsIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setPendingIntentTemplate(R.id.widget_list_view, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

}

