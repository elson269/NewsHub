package com.elsonji.newshub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class NewsListFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<String> mNewsSource;
    private PageFragment mPageFragment;

    public NewsListFragmentPagerAdapter(FragmentManager fm, ArrayList<String> data) {
        super(fm);
        mNewsSource = data;
    }

    @Override
    public Fragment getItem(int position) {
        int AsyncTaskLoaderId = position + 100;
        mPageFragment = PageFragment.newInstance(mNewsSource.get(position), AsyncTaskLoaderId);
        return mPageFragment;
    }

    @Override
    public int getCount() {
        return mNewsSource.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mNewsSource.get(position);
    }

}

