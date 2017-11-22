package com.elsonji.newshub;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Set;

import static com.elsonji.newshub.AddNewsFragment.REMAINING_NEWS_SOURCE;

public class NewsSourceFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<String> mDynamicNewsSource;
    private Context mContext;
    private Fragment mFragment;

    public NewsSourceFragmentPagerAdapter(Context context, FragmentManager fm, ArrayList<String> data) {
        super(fm);
        mContext = context;
        mDynamicNewsSource = data;
    }

    @Override
    public Fragment getItem(int position) {

        SharedPreferences remainingDynamicSource =
                mContext.getSharedPreferences(REMAINING_NEWS_SOURCE, Context.MODE_PRIVATE);
        Set<String> remainingNewsSourceSet = remainingDynamicSource.getStringSet(REMAINING_NEWS_SOURCE, null);
        ArrayList<String> remainingNewsSource;
        if (remainingNewsSourceSet != null) {
            remainingNewsSource = new ArrayList<>(remainingNewsSourceSet);

            switch (position) {
                case 0:
                    if (remainingNewsSource.size() == mDynamicNewsSource.size()) {
                        mFragment = AddNewsFragment.newInstance(mDynamicNewsSource);
                        notifyDataSetChanged();
                        return mFragment;
                    } else {
                        mFragment = AddNewsFragment.newInstance(remainingNewsSource);
                        notifyDataSetChanged();
                        return mFragment;
                    }
                case 1:
                    return DeleteNewsFragment.newInstance(mDynamicNewsSource);
                default:
                    return null;
            }
        } else {
            switch (position) {
                case 0:
                    return AddNewsFragment.newInstance(mDynamicNewsSource);
                case 1:
                    return DeleteNewsFragment.newInstance(mDynamicNewsSource);
                default:
                    return null;
            }
        }
    }

    @Override
    public int getItemPosition(Object object) {
//        if (mFragment == null) {
//            return super.getItemPosition(object);
//        } else {
            return POSITION_NONE;
       // }
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
