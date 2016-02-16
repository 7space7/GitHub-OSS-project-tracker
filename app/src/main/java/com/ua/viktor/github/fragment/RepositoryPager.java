package com.ua.viktor.github.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ua.viktor.github.R;
import com.ua.viktor.github.adapter.pager.PagerRepoAdapter;
import com.ua.viktor.github.utils.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepositoryPager extends Fragment {


    public RepositoryPager() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.repository_pager, container, false);


        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("YOURS"));
        tabLayout.addTab(tabLayout.newTab().setText("STARRED"));
        tabLayout.addTab(tabLayout.newTab().setText("WATCHED"));
        // tabLayout.addTab(tabLayout.newTab().setText("CONTRIBUTED"));
        //  tabLayout.addTab(tabLayout.newTab().setText("FROM ORGANIZATIONS"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        String authName = Utils.getUserAuthName(getActivity());

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        final PagerRepoAdapter adapter = new PagerRepoAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount(),authName);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

}
