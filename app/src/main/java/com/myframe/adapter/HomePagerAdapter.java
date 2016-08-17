package com.myframe.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.myframe.R;

/**
 * FragmentPagerAdapter 例子
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private final String[] TITLES;

    private Fragment[] fragments;

    public HomePagerAdapter(FragmentManager fm, Context context) {

        super(fm);
        TITLES = context.getResources().getStringArray(R.array.sections);
        fragments = new Fragment[TITLES.length];
    }

    @Override
    public Fragment getItem(int position) {

        if (fragments[position] == null) {
//            switch (position) {
//                case 0:
//                    fragments[position] = TwoFragment.newInstance();
//                    break;
//                case 1:
//                    fragments[position] = TwoFragment.newInstance();
//                    break;
//                default:
//                    fragments[position] = TwoFragment.newInstance();
//            }
        }
        return fragments[position];
    }

    @Override
    public int getCount() {

        return TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return TITLES[position];
    }

}
