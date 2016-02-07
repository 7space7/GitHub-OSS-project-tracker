package com.ua.viktor.github.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ua.viktor.github.fragment.PeopleOrgFragment;
import com.ua.viktor.github.utils.Constants;

/**
 * Created by viktor on 07.02.16.
 */
public class PagerOrgAdapter extends FragmentStatePagerAdapter {
   private   int mNumOfTabs;
    public PagerOrgAdapter(FragmentManager fm,int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab1 = PeopleOrgFragment.newInstance(Constants.KEY_FOLLOWING);
                return tab1;
            case 1:
                Fragment tab2 = PeopleOrgFragment.newInstance(Constants.KEY_FOLLOWERS);
                return tab2;
            case 2:
                Fragment tab3 = PeopleOrgFragment.newInstance(Constants.KEY_ORGANIZATIONS);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
