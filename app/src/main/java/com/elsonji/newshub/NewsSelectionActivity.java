package com.elsonji.newshub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static com.elsonji.newshub.AddNewsFragment.REMAINING_NEWS_SOURCE;

public class NewsSelectionActivity extends AppCompatActivity implements AddNewsFragment.DataUpdateListener {

    public static final String MY_NEWS_SOURCE = "MY_NEWS_SOURCE";
    private TabLayout mSourceTabLayout;
    private ViewPager mSourceViewPager;
    private NewsSourceFragmentPagerAdapter mSourcePagerAdapter;
    private ArrayList<String> mSelectedNewsSourceList;
    private ArrayList<String> mRemainingNewsSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_selection);

        ArrayList<String> newsSourceForAddition = new ArrayList<>(Arrays.asList(getResources().
                getStringArray(R.array.dynamic_news_source)));

        SharedPreferences remainingDynamicSource =
                getSharedPreferences(REMAINING_NEWS_SOURCE, MODE_PRIVATE);
        Set<String> remainingNewsSourceSet = remainingDynamicSource.getStringSet(REMAINING_NEWS_SOURCE, null);

        SharedPreferences selectedNewsSource =
                getSharedPreferences(MY_NEWS_SOURCE, MODE_PRIVATE);
        Set<String> selectedNewsSourceSet = selectedNewsSource.getStringSet(MY_NEWS_SOURCE, null);

        mSelectedNewsSourceList = new ArrayList<>();
        if (selectedNewsSourceSet != null && selectedNewsSourceSet.size() != 0) {
            mSelectedNewsSourceList = new ArrayList<>(selectedNewsSourceSet);
        }

        if (remainingNewsSourceSet != null && remainingNewsSourceSet.size() != 0) {
            //if (remainingNewsSourceSet.size() != 0) {
            // if (mSelectedNewsSourceList != null && mSelectedNewsSourceList.size() == 0) {
            mRemainingNewsSource = new ArrayList<>(remainingNewsSourceSet);
            if (mRemainingNewsSource.size() == newsSourceForAddition.size()) {
                mSourcePagerAdapter = new NewsSourceFragmentPagerAdapter(this, getSupportFragmentManager(),
                        newsSourceForAddition, mSelectedNewsSourceList);
            } else {
                mSourcePagerAdapter = new NewsSourceFragmentPagerAdapter(this, getSupportFragmentManager(),
                        mRemainingNewsSource, mSelectedNewsSourceList);
            }
            // }
            //}
        } else {
            if (mSelectedNewsSourceList != null && mSelectedNewsSourceList.size() == 0) {
                mSourcePagerAdapter = new NewsSourceFragmentPagerAdapter(this, getSupportFragmentManager(),
                        newsSourceForAddition, mSelectedNewsSourceList);
            } else {
                mSourcePagerAdapter = new NewsSourceFragmentPagerAdapter(this, getSupportFragmentManager(),
                        mRemainingNewsSource, mSelectedNewsSourceList);
            }
        }

        ArrayList<String> newsSourceForDeletion = new ArrayList<>();


        mSourceTabLayout = findViewById(R.id.news_selection_tab_layout);
        mSourceViewPager = findViewById(R.id.news_selection_view_pager);
        mSourceViewPager.setAdapter(mSourcePagerAdapter);

        mSourceTabLayout.setupWithViewPager(mSourceViewPager);
    }

    @Override
    public void onDataUpdate(ArrayList<String> newsSourceChosen) {
        String tag = "android:switcher:" + R.id.news_selection_view_pager + ":" + 1;
        DeleteNewsFragment deleteNewsFragment = (DeleteNewsFragment)
                getSupportFragmentManager().findFragmentByTag(tag);
        if (newsSourceChosen != null && newsSourceChosen.size() != 0) {
            deleteNewsFragment.updateData(newsSourceChosen);
        }
    }

}
