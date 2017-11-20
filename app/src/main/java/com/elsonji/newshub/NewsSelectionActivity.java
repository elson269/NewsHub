package com.elsonji.newshub;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class NewsSelectionActivity extends AppCompatActivity {

    private TabLayout mSourceTabLayout;
    private ViewPager mSourceViewPager;
    private NewsSourceFragmentPagerAdapter mSourcePagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_selection);

        ArrayList<String> dynamicNewsSource = new ArrayList<>(Arrays.asList(getResources().
                getStringArray(R.array.dynamic_news_source)));


        mSourcePagerAdapter = new NewsSourceFragmentPagerAdapter(getSupportFragmentManager(), dynamicNewsSource);

        mSourceTabLayout = findViewById(R.id.news_selection_tab_layout);
        mSourceViewPager = findViewById(R.id.news_selection_view_pager);
        mSourceViewPager.setAdapter(mSourcePagerAdapter);

        mSourceTabLayout.setupWithViewPager(mSourceViewPager);
    }
}
