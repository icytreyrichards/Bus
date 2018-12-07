package com.my.easybus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Richard.Ezama on 03/03/2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private String[] menus={"Book Now","Schedules","Prices"};
    private String UserID;
    private Bundle bundle=new Bundle();
    public SectionsPagerAdapter(FragmentManager fm,String UserID) {
        super(fm);
        this.UserID=UserID;
    }
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        switch (position)
        {
            case 0:
               // fragment=Send_Cash.newInstance(position);
                fragment=new startBook();
                break;
            case 1:
                bundle.putString("source","");
                bundle.putString("destination","");
                bundle.putString("time","All");
                getSchedules fra=new getSchedules();
                fra.setArguments(bundle);
                fragment=fra;
                break;
            case 2:
                fragment=new getPrices();
                break;

            default:
                fragment=new getSchedules();
        }
        return fragment;
        //58,18,1
    }

    @Override
    public int getCount() {
        return menus.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return menus[0].toUpperCase();
            case 1:
                return menus[1].toUpperCase();
            case 2:
                return menus[2].toUpperCase();
        }
        return null;
    }
}
