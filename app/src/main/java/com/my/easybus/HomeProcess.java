package com.my.easybus;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import localdata.Data;

public class HomeProcess extends Fragment {
    private SectionsPagerAdapterProcess mSectionsPagerAdapter;
    private ViewPager mViewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tabbed_layout, container, false);
        mSectionsPagerAdapter = new SectionsPagerAdapterProcess(getChildFragmentManager(), Data.UserID(getActivity()));
        mViewPager = (ViewPager)rootView. findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout)rootView. findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        return rootView;
    }
}
