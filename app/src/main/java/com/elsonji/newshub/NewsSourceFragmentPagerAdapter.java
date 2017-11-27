package com.elsonji.newshub;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class NewsSourceFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mNewsSource, mMyNewsSource;
    private Fragment mFragment;
    private Context mContext;

    public NewsSourceFragmentPagerAdapter(Context context, FragmentManager fm, ArrayList<String> newsSourceData,
                                          ArrayList<String> myNewsData) {
        super(fm);
        mContext = context;
        mNewsSource = newsSourceData;
        mMyNewsSource = myNewsData;
        //notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                mFragment = AddNewsFragment.newInstance(mNewsSource, mMyNewsSource);
                notifyDataSetChanged();
                return mFragment;
            case 1:
                //notifyDataSetChanged();
                mFragment = DeleteNewsFragment.newInstance(mMyNewsSource);

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
                return "News Source";
            case 1:
                return "My News";
            default:
                return null;
        }
    }
}
