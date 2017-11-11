package com.elsonji.newshub;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class NewsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        String[] newsSources = getResources().getStringArray(R.array.news_sources);


        ViewPager viewPager = (ViewPager) findViewById(R.id.news_list_view_pager);
        viewPager.setAdapter(new NewsListFragmentPagerAdapter(getSupportFragmentManager(), newsSources.length));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.addTab(tabLayout.newTab().setText(newsSources[1]));
        tabLayout.setupWithViewPager(viewPager);
    }
}
