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
import static com.elsonji.newshub.DeleteNewsFragment.DELETED_MY_NEWS;

public class NewsSelectionActivity extends AppCompatActivity implements
        AddNewsFragment.DataUpdateListener, DeleteNewsFragment.NewsUpdateListener {

    public static final String MY_NEWS_SOURCE = "MY_NEWS_SOURCE";
    private TabLayout mSourceTabLayout;
    private ViewPager mSourceViewPager;
    private NewsSourceFragmentPagerAdapter mSourcePagerAdapter;
    private ArrayList<String> mSelectedNewsSourceList;
    private ArrayList<String> mRemainingNewsSource;
    private ArrayList<String> mDeletedMyNews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_selection);

        ArrayList<String> newsSourceForAddition = new ArrayList<>(Arrays.asList(getResources().
                getStringArray(R.array.dynamic_news_source)));

        SharedPreferences remainingNewsSource =
                getSharedPreferences(REMAINING_NEWS_SOURCE, MODE_PRIVATE);
        Set<String> remainingNewsSourceSet = remainingNewsSource.getStringSet(REMAINING_NEWS_SOURCE, null);

        SharedPreferences selectedNewsSource =
                getSharedPreferences(MY_NEWS_SOURCE, MODE_PRIVATE);
        Set<String> selectedNewsSourceSet = selectedNewsSource.getStringSet(MY_NEWS_SOURCE, null);

        SharedPreferences deletedMyNewsPref =
                getSharedPreferences(DELETED_MY_NEWS, MODE_PRIVATE);
        Set<String> deletedMyNewsSet = deletedMyNewsPref.getStringSet(DELETED_MY_NEWS, null);


        mSelectedNewsSourceList = new ArrayList<>();
        mDeletedMyNews = new ArrayList<>();
        mRemainingNewsSource = new ArrayList<>();
        if (selectedNewsSourceSet != null && selectedNewsSourceSet.size() != 0) {
            mSelectedNewsSourceList = new ArrayList<>(selectedNewsSourceSet);
        }
        if (deletedMyNewsSet != null && deletedMyNewsSet.size() != 0) {
            mDeletedMyNews = new ArrayList<>(deletedMyNewsSet);
        }

        if (remainingNewsSourceSet != null && remainingNewsSourceSet.size() != 0) {
            mRemainingNewsSource = new ArrayList<>(remainingNewsSourceSet);
        }

        mRemainingNewsSource.addAll(mDeletedMyNews);
        mSelectedNewsSourceList.removeAll(mDeletedMyNews);

        if (mRemainingNewsSource != null && mRemainingNewsSource.size() != 0) {

            if (mSelectedNewsSourceList != null && mSelectedNewsSourceList.size() == 0) {
                mSourcePagerAdapter = new NewsSourceFragmentPagerAdapter(getSupportFragmentManager(),
                        mRemainingNewsSource, mSelectedNewsSourceList, mDeletedMyNews);
            } else {
                mSourcePagerAdapter = new NewsSourceFragmentPagerAdapter(getSupportFragmentManager(),
                        mRemainingNewsSource, mSelectedNewsSourceList, mDeletedMyNews);
            }
        } else {

            if (mSelectedNewsSourceList != null && mSelectedNewsSourceList.size() == 0) {
                mSourcePagerAdapter = new NewsSourceFragmentPagerAdapter(getSupportFragmentManager(),
                        newsSourceForAddition, mSelectedNewsSourceList, mDeletedMyNews);
            } else if (mSelectedNewsSourceList != null && mSelectedNewsSourceList.size() != 0) {
                mSourcePagerAdapter = new NewsSourceFragmentPagerAdapter(getSupportFragmentManager(),
                        mDeletedMyNews, mSelectedNewsSourceList, mDeletedMyNews);
            }
        }

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

    @Override
    public void onNewsUpdate(ArrayList<String> myNewsDeleted) {
        String tag = "android:switcher:" + R.id.news_selection_view_pager + ":" + 0;
        AddNewsFragment addNewsFragment = (AddNewsFragment)
                getSupportFragmentManager().findFragmentByTag(tag);
        if (myNewsDeleted != null && myNewsDeleted.size() != 0) {
            addNewsFragment.updateNews(myNewsDeleted);
        }
    }
}
