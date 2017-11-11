package com.elsonji.newshub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class NewsListFragmentPagerAdapter extends FragmentStatePagerAdapter {

    //private final int PAGE_COUNT = 8;
    //private String tabTitles[] = new String[] { "Tab1", "Tab2", "Tab3", "Tab4", "Tab5", "Tab6", "Tab7", "Tab8"};
    private int mNumOfTabs;
    //private Context mContext;

    public NewsListFragmentPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        //mContext = context;
        mNumOfTabs = numOfTabs;

    }

    @Override
    public Fragment getItem(int position) {
        return null;
        //PageFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        //This method might not be needed.
//        return tabTitles[position];
//    }
}

