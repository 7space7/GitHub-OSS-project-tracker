package com.ua.viktor.github.adapter.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ua.viktor.github.fragment.RepositoriesFragment;
import com.ua.viktor.github.utils.Constants;

/**
 * Created by viktor on 27.01.16.
 */
public class PagerRepoAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private String mUserLogin;
    public PagerRepoAdapter(FragmentManager fm, int NumOfTabs,String mUserLogin) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.mUserLogin=mUserLogin;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment tab1 = RepositoriesFragment.newInstance(Constants.KEY_YOUR,mUserLogin);
                return tab1;
            case 1:
                Fragment tab2 = RepositoriesFragment.newInstance(Constants.KEY_STARRED,mUserLogin);
                return tab2;
            case 2:
                Fragment tab3 = RepositoriesFragment.newInstance(Constants.KEY_WATCHED,mUserLogin);
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
