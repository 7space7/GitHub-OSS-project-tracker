package com.ua.viktor.github.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ua.viktor.github.fragment.RepositoriesFragment;

/**
 * Created by viktor on 27.01.16.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment tab1 = RepositoriesFragment.newInstance("lol");
                return tab1;
            case 1:
                Fragment tab2 = RepositoriesFragment.newInstance("lolka");
            return tab2;

            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "First Tab";
            case 1:
            default:
                return "Second Tab";
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
