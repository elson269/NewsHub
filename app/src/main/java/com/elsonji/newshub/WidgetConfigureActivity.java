package com.elsonji.newshub;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

import static com.elsonji.newshub.NewsListActivity.CURRENT_NEWS_SOURCE;

public class WidgetConfigureActivity extends AppCompatActivity implements
        WidgetNewsAdapter.OnWidgetSourceClickListener {
    private int mAppWidgetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences selectedNewsSourcePref;
        Set<String> selectedNewsSourceSet;
        ArrayList<String> selectedNewsSource;

        super.onCreate(savedInstanceState);
        setResult(RESULT_CANCELED);
        setContentView(R.layout.activity_widget_configure);

        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        selectedNewsSourcePref = getSharedPreferences(CURRENT_NEWS_SOURCE, MODE_PRIVATE);

        selectedNewsSourceSet = selectedNewsSourcePref.getStringSet(CURRENT_NEWS_SOURCE, null);
        if (selectedNewsSourceSet != null) {

            selectedNewsSource = new ArrayList<>(selectedNewsSourceSet);
            Collections.sort(selectedNewsSource);
            WidgetNewsAdapter mWidgetNewsAdapter = new WidgetNewsAdapter(this, selectedNewsSource, this, mAppWidgetId);
            RecyclerView mWidgetNewsRecyclerView = findViewById(R.id.widget_config_recycler_view);
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
            mWidgetNewsRecyclerView.setAdapter(mWidgetNewsAdapter);
            mWidgetNewsRecyclerView.setLayoutManager(mLinearLayoutManager);
        }
    }

    @Override
    public void onWidgetSourceClick(View view, int position) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        NewsWidgetProvider.updateAppWidget(this, appWidgetManager, mAppWidgetId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

}
