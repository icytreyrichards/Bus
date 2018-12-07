package com.my.easybus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Richard.Ezama on 03/03/2016.
 */
public class IndexHome extends Fragment {
    private IndexPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.viewpager, container, false);
        mSectionsPagerAdapter = new IndexPagerAdapter(getChildFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)rootView. findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        return rootView;
    }
}
