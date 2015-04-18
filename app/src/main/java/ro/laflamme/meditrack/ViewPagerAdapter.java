package ro.laflamme.meditrack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by loopiezlol on 18.04.2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[];
    int NumbOfTabs;

    public ViewPagerAdapter(FragmentManager fm,CharSequence mTitles[], int mNumbOfTabs) {
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                PharmsFragment pF = new PharmsFragment();
                return  pF;
            case 1:
                MapFragment mF = new MapFragment();
                return mF;
            case 2:
                MedsFragment mdF = new MedsFragment();
                return mdF;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return Titles[position];
    }

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
