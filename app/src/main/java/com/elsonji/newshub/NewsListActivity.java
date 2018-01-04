package com.elsonji.newshub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.elsonji.newshub.LogInActivity.GOOGLE_SIGN_IN_SKIPPED;
import static com.elsonji.newshub.LogInActivity.GOOGLE_SIGN_IN_USED;
import static com.elsonji.newshub.MyNewsFragment.REMAINING_MY_NEWS;

public class NewsListActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private ImageButton mImageButton;
    private NewsListFragmentPagerAdapter mNewsListFragmentPagerAdapter;
    private ArrayList<String> mRemainingNewsSourceList, mCurrentNewsSourceList, mStaticNewsList;
    private SharedPreferences mRemainingNewsPref;
    private Set<String> mRemainingNewsSet;
    private GoogleSignInClient mGoogleSignInClient;
    private boolean mGoogleSignInStatus;
    private AdView mAdView;
    public static final String POSITION = "POSITION";
    public static final String CURRENT_NEWS_SOURCE = "CURRENT_NEWS_SOURCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        MobileAds.initialize(this, getString(R.string.app_id));
        mAdView = findViewById(R.id.ad_view);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        String[] staticNewsSources = getResources().getStringArray(R.array.static_news_sources);
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

        Intent intentFromSignInActivity = getIntent();
        mGoogleSignInStatus = intentFromSignInActivity.getBooleanExtra(GOOGLE_SIGN_IN_USED, true);
        if (mGoogleSignInStatus) {
            invalidateOptionsMenu();
        }
        if (!mGoogleSignInStatus) {
            invalidateOptionsMenu();
        }

        if (savedInstanceState == null) {

            mRemainingNewsPref = getSharedPreferences(REMAINING_MY_NEWS, MODE_PRIVATE);
            mRemainingNewsSet = mRemainingNewsPref.getStringSet(REMAINING_MY_NEWS, null);
            if (mRemainingNewsSet != null) {
                mRemainingNewsSourceList = new ArrayList<>(mRemainingNewsSet);
                mCurrentNewsSourceList.addAll(mRemainingNewsSourceList);
            }

            mNewsListFragmentPagerAdapter = new NewsListFragmentPagerAdapter(getSupportFragmentManager(),
                    mCurrentNewsSourceList);
            //currentNewsPref is used to allow WidgetConfigureActivity to get current selected news data.
            SharedPreferences currentNewsPref = getSharedPreferences(CURRENT_NEWS_SOURCE, MODE_PRIVATE);
            SharedPreferences.Editor currentNewsPrefEditor = currentNewsPref.edit();
            Set<String> currentNewsSet = new HashSet<>();
            currentNewsSet.addAll(mCurrentNewsSourceList);
            currentNewsPrefEditor.putStringSet(CURRENT_NEWS_SOURCE, currentNewsSet);
            currentNewsPrefEditor.apply();
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
        if (!mGoogleSignInStatus) {
            menu.findItem(R.id.action_sign_out).setVisible(false);
        }
        if (mGoogleSignInStatus) {
            menu.findItem(R.id.action_sign_in).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickedId = item.getItemId();

        if (itemClickedId == R.id.action_saved_news) {
            Intent savedNewsIntent = new Intent(this, SavedNewsActivity.class);
            startActivity(savedNewsIntent);
        }

        if (itemClickedId == R.id.action_about) {
            Intent aboutIntent = new Intent(this, AboutActivity.class);
            startActivity(aboutIntent);
        }

        if (itemClickedId == R.id.action_sign_out) {
            mGoogleSignInClient = GoogleSignInClientSingleton.getInstance(null).getGoogleSignInClient();
            signOut();
        }

        if (itemClickedId == R.id.action_sign_in) {
            //Reset mGoogleSignInSkipped in LogInActivity to false to clean out previously registered mGoogleSignInSkipped status.
            SharedPreferences mSignInStatusPref = getSharedPreferences(GOOGLE_SIGN_IN_SKIPPED, MODE_PRIVATE);
            SharedPreferences.Editor editor = mSignInStatusPref.edit();
            editor.putBoolean(GOOGLE_SIGN_IN_SKIPPED, false);
            editor.apply();
            //Go back to LogInActivity by calling finish().
            //Cannot use intent to go back as this activity is started by LogInActivity.
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
    }

}
