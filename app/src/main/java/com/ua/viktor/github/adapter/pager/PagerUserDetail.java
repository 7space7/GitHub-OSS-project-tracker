package com.ua.viktor.github.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ua.viktor.github.fragment.EventFragment;
import com.ua.viktor.github.fragment.RepositoriesFragment;
import com.ua.viktor.github.fragment.UserDetailFragment;
import com.ua.viktor.github.utils.Constants;

/**
 * Created by viktor on 16.02.16.
 */
public class PagerUserDetail extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private String mUserLogin;

    public PagerUserDetail(FragmentManager fm, int NumOfTabs, String mUserLogin) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.mUserLogin = mUserLogin;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment tab = UserDetailFragment.newInstance(mUserLogin);
                return tab;
            case 1:
                Fragment tab3 = RepositoriesFragment.newInstance(Constants.KEY_YOUR, mUserLogin);
                return tab3;
            case 2:
                Fragment tab1 = EventFragment.newInstance(mUserLogin);
                return tab1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
