package com.elsonji.newshub;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class NewsSourceFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mDynamicNewsSource;

    public NewsSourceFragmentPagerAdapter(FragmentManager fm, ArrayList<String> data) {
        super(fm);
        mDynamicNewsSource = data;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return AddNewsFragment.newInstance(mDynamicNewsSource);
            case 1:
                return DeleteNewsFragment.newInstance(mDynamicNewsSource);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "News Source";
            case 1:
                return "My News";
            default:
                return null;
        }
    }
}
