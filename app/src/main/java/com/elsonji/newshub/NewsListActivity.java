package com.elsonji.newshub;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;

public class NewsListActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageButton mImageButton;
    private NewsListFragmentPagerAdapter mNewsListFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        String[] newsSources = {"cnn", "cnbc", "bild", "bloomberg", "buzzfeed"};
        ArrayList<String> newsSourceArrayList = new ArrayList<>(Arrays.asList(newsSources));

        mNewsListFragmentPagerAdapter = new NewsListFragmentPagerAdapter(getSupportFragmentManager(),
                newsSourceArrayList);

        mImageButton = findViewById(R.id.news_add_button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsSelectionIntent = new Intent(getApplicationContext(), NewsSelectionActivity.class);
                startActivity(newsSelectionIntent);
            }
        });

        mViewPager = findViewById(R.id.news_list_view_pager);
        mViewPager.setOffscreenPageLimit(1);

        mViewPager.setAdapter(mNewsListFragmentPagerAdapter);
        mTabLayout = findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
