package com.my.easybus;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Richard.Ezama on 03/03/2016.
 */
public class IndexPagerAdapter extends FragmentPagerAdapter {

    public IndexPagerAdapter(FragmentManager fm) {
        super(fm);
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
                fragment=new Register();
                break;
            case 1:
                fragment=new Register();
                break;
            case 2:
                fragment=new Register();
                break;
            default:
                fragment=new Register();
        }
        return fragment;
        //58,18,1
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                //return menus[0].toUpperCase();
            case 1:
               // return menus[1].toUpperCase();
            case 2:

        }
        return null;
    }
}
