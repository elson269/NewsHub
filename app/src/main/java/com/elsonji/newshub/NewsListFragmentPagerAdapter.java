package com.elsonji.newshub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class NewsListFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;
    private String[] mNewsSourceStrings;
    //private Context mContext;

    public NewsListFragmentPagerAdapter(FragmentManager fm, String[] strings) {
        super(fm);
        //mContext = context;
        mNumOfTabs = strings.length;
        mNewsSourceStrings = strings;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mNewsSourceStrings[position];
    }
}

