package com.ua.viktor.github.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ua.viktor.github.R;
import com.ua.viktor.github.adapter.pager.PagerOrgAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleOrgPager extends Fragment {


    public PeopleOrgPager() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.people_org_pager, container, false);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout_org);
        tabLayout.addTab(tabLayout.newTab().setText("FOLLOWING"));
        tabLayout.addTab(tabLayout.newTab().setText("FOLOWERS"));
        tabLayout.addTab(tabLayout.newTab().setText("ORGANIZATIONS"));


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager_org);
        final PagerOrgAdapter adapter = new PagerOrgAdapter
                (getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
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
