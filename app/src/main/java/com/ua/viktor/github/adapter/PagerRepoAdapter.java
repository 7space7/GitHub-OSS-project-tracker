package com.ua.viktor.github.adapter;

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

    public PagerRepoAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment tab1 = RepositoriesFragment.newInstance(Constants.KEY_YOUR);
                return tab1;
            case 1:
                Fragment tab2 = RepositoriesFragment.newInstance(Constants.KEY_STARRED);
            return tab2;
            case 2:
                Fragment tab3 = RepositoriesFragment.newInstance(Constants.KEY_WATCHED);
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
