package com.elsonji.newshub;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class NewsListActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        String[] newsSources = {"cnn", "cnbc", "bild", "bloomberg", "buzzfeed"};
        ArrayList<String> newsSourceArrayList = new ArrayList<>(Arrays.asList(newsSources));
//
//        if (savedInstanceState != null && savedInstanceState.containsKey(TAB_POSITION)) {
//            mViewPager = (ViewPager) findViewById(R.id.news_list_view_pager);
//            mViewPager.setCurrentItem(savedInstanceState.getInt(TAB_POSITION));
//        } else {
            mViewPager = (ViewPager) findViewById(R.id.news_list_view_pager);
            mViewPager.setOffscreenPageLimit(3);
            mViewPager.setAdapter(new NewsListFragmentPagerAdapter(getSupportFragmentManager(), newsSourceArrayList));

            mTabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
            mTabLayout.setupWithViewPager(mViewPager);
       // }
    }

//    @Override
//    protected void onSaveInstanceState( Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt(TAB_POSITION, mTabLayout.getSelectedTabPosition());
//    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        mViewPager.setCurrentItem(savedInstanceState.getInt(TAB_POSITION));
//    }

    //    public URL createNewsUrl(String sourceParam, String sortByParam) {
//        URL newsUrl = null;
//        Uri builtUri = Uri.parse(NEWS_BASE_URL)
//                .buildUpon()
//                .appendQueryParameter(SOURCE_PARAM, sourceParam)
//                .appendQueryParameter(SORT_BY_PARAM, sortByParam)
//                .appendQueryParameter(API_KEY_PRAM, getResources().getString(R.string.api_key))
//                .build();
//
//        try {
//            newsUrl = new URL(builtUri.toString());
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        return newsUrl;
//    }
}
