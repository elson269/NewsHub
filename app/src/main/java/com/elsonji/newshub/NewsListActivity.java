package com.elsonji.newshub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static com.elsonji.newshub.MyNewsFragment.REMAINING_MY_NEWS;

public class NewsListActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageButton mImageButton;
    private NewsListFragmentPagerAdapter mNewsListFragmentPagerAdapter;
    private ArrayList<String> mRemainingNewsSourceList, mCurrentNewsSourceList, mStaticNewsList;
    private SharedPreferences mRemainingNewsPref;
    private Set<String> mRemainingNewsSet;
    public static final String POSITION = "POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        String[] staticNewsSources = {"cnn", "cnbc", "reuters"};
        mStaticNewsList = new ArrayList<>(Arrays.asList(staticNewsSources));
        mCurrentNewsSourceList = new ArrayList<>(Arrays.asList(staticNewsSources));

        mImageButton = findViewById(R.id.news_add_button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newsSelectionIntent = new Intent(getApplicationContext(), NewsSelectionActivity.class);
                startActivity(newsSelectionIntent);
            }
        });

        if (savedInstanceState == null) {

            mRemainingNewsPref = getSharedPreferences(REMAINING_MY_NEWS, MODE_PRIVATE);
            mRemainingNewsSet = mRemainingNewsPref.getStringSet(REMAINING_MY_NEWS, null);
            if (mRemainingNewsSet != null) {
                mRemainingNewsSourceList = new ArrayList<>(mRemainingNewsSet);
                mCurrentNewsSourceList.addAll(mRemainingNewsSourceList);
            }

            mNewsListFragmentPagerAdapter = new NewsListFragmentPagerAdapter(getSupportFragmentManager(),
                    mCurrentNewsSourceList);
            mNewsListFragmentPagerAdapter.notifyDataSetChanged();

            mViewPager = findViewById(R.id.news_list_view_pager);
            mViewPager.setOffscreenPageLimit(1);

            mViewPager.setAdapter(mNewsListFragmentPagerAdapter);
            mTabLayout = findViewById(R.id.sliding_tabs);
            mTabLayout.setupWithViewPager(mViewPager);

            //Avoid creating tabs chosen by user twice after screen rotation.
            mCurrentNewsSourceList.clear();
            mCurrentNewsSourceList.addAll(mStaticNewsList);
        } else {
            mViewPager = findViewById(R.id.news_list_view_pager);
            mViewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mRemainingNewsPref = getSharedPreferences(REMAINING_MY_NEWS, MODE_PRIVATE);
        mRemainingNewsSet = mRemainingNewsPref.getStringSet(REMAINING_MY_NEWS, null);
        if (mRemainingNewsSet != null) {
            mRemainingNewsSourceList = new ArrayList<>(mRemainingNewsSet);
            mCurrentNewsSourceList.addAll(mRemainingNewsSourceList);
        }

        mNewsListFragmentPagerAdapter = new NewsListFragmentPagerAdapter(getSupportFragmentManager(),
                mCurrentNewsSourceList);
        mNewsListFragmentPagerAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mNewsListFragmentPagerAdapter);
        mTabLayout = findViewById(R.id.sliding_tabs);
        mTabLayout.setupWithViewPager(mViewPager);
        int tabPosition;
        Intent intent = getIntent();
        if (intent.getExtras() != null && intent.hasExtra(POSITION)) {
            tabPosition = (int) intent.getExtras().get(POSITION);
            mViewPager.setCurrentItem(tabPosition);
        }
    }

    //Overriding onPause to avoid duplicated tabs chosen by user after returning back from
    //NewsSelectionActivity.
    @Override
    protected void onPause() {
        super.onPause();
        mCurrentNewsSourceList.clear();
        mCurrentNewsSourceList.addAll(mStaticNewsList);
    }

    @Override
    protected void onStop() {
        super.onStop();
        getIntent().putExtra(POSITION, mTabLayout.getSelectedTabPosition());
    }

    //Overriding onSaveInstanceState to stay on the selected tab after screen rotation.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, mTabLayout.getSelectedTabPosition());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickedId = item.getItemId();

        if (itemClickedId == R.id.action_saved_news) {
            Intent savedNewsIntent = new Intent(this, SavedNewsActivity.class);
            startActivity(savedNewsIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
