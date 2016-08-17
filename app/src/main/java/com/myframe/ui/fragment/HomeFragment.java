package com.myframe.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyco.tablayout.SlidingTabLayout;
import com.myframe.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends Fragment {
    @BindView(R.id.tab_pager)
    ViewPager mTabPager;

    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTab;

    private int position =0;

    private HomePagerAdapter mHomeAdapter;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_sprout, container, false);
        ButterKnife.bind(this, view);

        mHomeAdapter = new HomePagerAdapter(getChildFragmentManager(), getContext());
        mTabPager.setOffscreenPageLimit(4);
        mTabPager.setAdapter(mHomeAdapter);
        mSlidingTab.setViewPager(mTabPager);
        if(savedInstanceState != null){
            mTabPager.setCurrentItem(savedInstanceState.getInt("current_pager"));
        }else{
            mTabPager.setCurrentItem(0);
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        Unbinder.EMPTY.unbind();
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_pager",mHomeAdapter.getPosition());
    }

    class HomePagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES;

        private Fragment[] fragments;

        private int posit = 0;//当前显示位置

        public HomePagerAdapter(FragmentManager fm, Context context) {

            super(fm);
            TITLES = context.getResources().getStringArray(R.array.sections);
            fragments = new Fragment[TITLES.length];
        }

        @Override
        public Fragment getItem(int position) {

            if (fragments[position] == null) {
                switch (position) {
                    case 0:
                        posit = 0;
                        fragments[position] = FirstFragment.newInstance();
                        break;
                    case 1:
                        posit = 1;
                        fragments[position] = ThemesDailyFragment.newInstance();
                        break;
                    case 2:
                        posit = 2;
                        fragments[position] = SectionsFragment.newInstance();
                        break;
                    case 3:
                        posit = 3;
                        fragments[position] = HotNewsFragment.newInstance();
                        break;
                    default:
                        fragments[position] = FirstFragment.newInstance();
                }
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

        /**
         * 获取当前页位置
         */
        public int getPosition(){
            return posit;
        }

    }
}
