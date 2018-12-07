package com.my.easybus;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Richard.Ezama on 03/03/2016.
 */
public class ProfileSectionsPageAdapter extends FragmentPagerAdapter {
    private String[] menus={"Profile","My Account"};

    public ProfileSectionsPageAdapter(FragmentManager fm) {
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        Fragment fragment;
        Bundle bundle = new Bundle();
        switch (position)
        {
            case 0:
                fragment=new MiniProfile();
                break;
            case 1:
                // fragment=Send_Cash.newInstance(position);
                MyAccount acc=new MyAccount();
                Bundle b=new Bundle();
                b.putString("status","0");
                acc.setArguments(b);
                fragment=acc;
                break;

            default:
                fragment=new MiniProfile();
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


        }
        return null;
    }
}
