package com.elsonji.newshub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

public class NewsListFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> mNewsSource;
    private PageFragment mPageFragment;
    private PageFragment[] pageFragments;

    public NewsListFragmentPagerAdapter(FragmentManager fm, ArrayList<String> data) {
        super(fm);
        mNewsSource = data;
        pageFragments = new PageFragment[mNewsSource.size()];
    }

    @Override
    public Fragment getItem(int position) {
        int AsyncTaskLoaderId = position + 100;
        mPageFragment = PageFragment.newInstance(mNewsSource.get(position), AsyncTaskLoaderId);
        pageFragments[position] = mPageFragment;

        return pageFragments[position];
    }

    @Override
    public int getCount() {
        return mNewsSource.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mNewsSource.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        pageFragments[position] = (PageFragment) super.instantiateItem(container, position);
        return pageFragments[position];
    }
}

