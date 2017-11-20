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
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new DeleteNewsFragment();
                break;
            case 1:
                fragment = AddNewsFragment.newInstance(mDynamicNewsSource);
                break;
            default:
                fragment = null;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "My News";
            case 1:
                return "News Source";
            default:
                return null;
        }
    }
}
