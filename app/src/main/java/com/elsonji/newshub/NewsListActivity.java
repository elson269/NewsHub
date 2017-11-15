package com.elsonji.newshub;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class NewsListActivity extends AppCompatActivity {

//    private static final String NEWS_BASE_URL = "https://newsapi.org/v1/articles?";
//    private static final String SOURCE_PARAM = "source";
//    private static final String SORT_BY_PARAM = "sortBy";
//    private static final String API_KEY_PRAM =  "apiKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        String[] newsSources = getResources().getStringArray(R.array.news_sources);
        ArrayList<String> newsSourceArrayList = new ArrayList<>(Arrays.asList(newsSources));

        ViewPager viewPager = (ViewPager) findViewById(R.id.news_list_view_pager);
        viewPager.setAdapter(new NewsListFragmentPagerAdapter(getSupportFragmentManager(), newsSourceArrayList));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
//        for (int i = 0; i < newsSources.length; i++) {
//            tabLayout.addTab(tabLayout.newTab().setText(newSourceArrayList.get(i)));
//        }
        tabLayout.setupWithViewPager(viewPager);
    }

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
