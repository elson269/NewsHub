package com.elsonji.newshub;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class abc extends FragmentStatePagerAdapter {

    private final Bundle mBundle;
    private final String[] mTitles;
    private final Fragment[] mFragments;

    public abc(FragmentManager fm, Bundle bundle, Class<?>[] classes, String[] titles) {
        super(fm);
        mBundle = bundle;
        mTitles = titles;

        // http://stackoverflow.com/questions/9886266/is-there-a-way-to-instantiate-a-class-by-name-in-java
        // use generic constructor for fragments and pass through bundle
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (Class<?> fragmentClass : classes) {
            try {
                Constructor constructor = fragmentClass.getConstructor();
                Fragment fragment = (Fragment) constructor.newInstance();
                fragment.setArguments(mBundle);
                fragments.add(fragment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mFragments = fragments.toArray(new Fragment[fragments.size()]);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return mTitles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // workaround to fragment unattach on rotation
        // http://stackoverflow.com/a/29288093/740474
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        mFragments[position] = fragment;
        return fragment;
    }
}