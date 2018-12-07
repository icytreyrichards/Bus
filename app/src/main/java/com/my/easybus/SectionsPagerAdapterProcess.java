package com.my.easybus;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Richard.Ezama on 03/03/2016.
 */
public class SectionsPagerAdapterProcess extends FragmentPagerAdapter {
    private String[] menus={"Pending Tickets","Reserve"};
    private String UserID;
    private Bundle bundle=new Bundle();
    public SectionsPagerAdapterProcess(FragmentManager fm, String UserID) {
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
                fragment=new ViewTickets();
                bundle.putString("status","1");
                bundle.putString("search","");
                fragment.setArguments(bundle);
                break;
            case 1:
                //we shall pick details from the file
                bundle.putString("source","");
                bundle.putString("destination","");
                bundle.putString("time","All");
                getSchedules fra=new getSchedules();
                fra.setArguments(bundle);
                fragment=fra;
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
