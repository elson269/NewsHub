package com.elsonji.newshub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class NewsListFragmentPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<String> mNewsSource;
    //private Context mContext;

    public NewsListFragmentPagerAdapter(FragmentManager fm, ArrayList<String> data) {
        super(fm);
        //mContext = context;
        mNewsSource = data;
    }

    @Override
    public Fragment getItem(int position) {
        return PageFragment.newInstance(mNewsSource.get(position));
    }

    @Override
    public int getCount() {
        return mNewsSource.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mNewsSource.get(position);
    }

//    public int getItemPosition(Object item) {
//        PageFragment fragment = (PageFragment) item;
//        String title = fragment.getTitle();
//        int position = mNewsSource.indexOf(title);
//
//        if (position >= 0) {
//            return position;
//        } else {
//            return POSITION_NONE;
//        }
//    }

}

