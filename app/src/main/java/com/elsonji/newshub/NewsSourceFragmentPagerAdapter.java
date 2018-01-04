package com.elsonji.newshub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class NewsSourceFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final String NEWS_SOURCE = "News Source";
    public static final String MY_NEWS = "My News";
    private ArrayList<String> mNewsSource, mMyNewsSource, mDeletedNewsData;
    private Fragment mFragment;

    public NewsSourceFragmentPagerAdapter(FragmentManager fm, ArrayList<String> newsSourceData,
                                          ArrayList<String> myNewsData, ArrayList<String> myDeletedNewsData) {
        super(fm);

        mNewsSource = newsSourceData;
        mMyNewsSource = myNewsData;
        mDeletedNewsData = myDeletedNewsData;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                mFragment = NewsSourceFragment.newInstance(mNewsSource, mMyNewsSource);
                notifyDataSetChanged();
                return mFragment;
            case 1:
                mFragment = MyNewsFragment.newInstance(mMyNewsSource, mDeletedNewsData);

                return mFragment;
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return NEWS_SOURCE;
            case 1:
                return MY_NEWS;
            default:
                return null;
        }
    }
}
