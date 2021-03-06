package com.ua.viktor.github.fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.ua.viktor.github.R;
import com.ua.viktor.github.activity.UserDetailActivity;
import com.ua.viktor.github.adapter.PeopleOrgAdapter;
import com.ua.viktor.github.model.Organizations;
import com.ua.viktor.github.rest.OrganizationService;
import com.ua.viktor.github.rest.ServiceGenerator;
import com.ua.viktor.github.utils.Constants;
import com.ua.viktor.github.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PeopleOrgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PeopleOrgFragment extends Fragment {
    private static final String ORG_KEY = "org";
    private static final String AUTH_KEY = "AUTH_KEY";
    private PeopleOrgAdapter mPeopleOrgAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<Organizations> mList;
    private Call<ArrayList<Organizations>> call;
    private static final String KEY_QUERY = "key_org";
    private String mKeyQuery;
    private ErrorView mErrorView;
    private ProgressWheel progressWheel;
    private String mAuthName;
    private ProgressBar empty;

    public PeopleOrgFragment() {
        // Required empty public constructor
    }


    public static PeopleOrgFragment newInstance(String keyQuery) {
        PeopleOrgFragment fragment = new PeopleOrgFragment();
        Bundle args = new Bundle();
        args.putString(KEY_QUERY, keyQuery);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(ORG_KEY, mList);
        outState.putString(mAuthName, AUTH_KEY);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mKeyQuery = getArguments().getString(KEY_QUERY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people_org, container, false);
        empty = (ProgressBar) view.findViewById(android.R.id.empty);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.org_list);
        mErrorView = (ErrorView) view.findViewById(R.id.error_view_org);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress_wheel_org);

        if (savedInstanceState == null) {
            mAuthName = Utils.getUserAuthName(getActivity());
            getPeopleOrgRequest(view);


        } else {

            mList = savedInstanceState.getParcelableArrayList(ORG_KEY);
            mAuthName = savedInstanceState.getString(AUTH_KEY);
            int size = (mList == null) ? 0 : mList.size();
            if (mList != null && size != 0) {
                initializeAdapter(view);
            } else {
                setErrorView(view);
            }
        }

        return view;

    }


    public void getPeopleOrgRequest(final View rootView) {


        OrganizationService client = ServiceGenerator.createService(OrganizationService.class);
        switch (mKeyQuery) {
            case Constants.KEY_FOLLOWING:
                call = client.getFollowing(mAuthName, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
                break;
            case Constants.KEY_FOLLOWERS:
                call = client.getFollowers(mAuthName, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
                break;
            case Constants.KEY_ORGANIZATIONS:
                call = client.getOrgs(mAuthName, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
                break;
        }


        call.enqueue(new Callback<ArrayList<Organizations>>() {
            @Override
            public void onResponse(Response<ArrayList<Organizations>> response) {
                Log.v("count_org", "call");
                mList = response.body();
                int size = (mList == null) ? 0 : mList.size();
                if (mList != null && size != 0) {
                    initializeAdapter(rootView);
                } else {
                    setErrorView(rootView);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                setErrorView(rootView);
            }
        });
    }

    private void initializeAdapter(View rootView) {
        mPeopleOrgAdapter = new PeopleOrgAdapter(mList);
        mRecyclerView.setVisibility(View.VISIBLE);

        mPeopleOrgAdapter.SetOnClickListener(new PeopleOrgAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), UserDetailActivity.class);
                intent.putExtra(Constants.KEY_LOGIN, mList.get(position).getLogin());
                intent.putExtra(Constants.KEY_URL, mList.get(position).getAvatar_url());
                startActivity(intent);
            }
        });


        if (rootView.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        } else {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        }
        mRecyclerView.setAdapter(mPeopleOrgAdapter);
        empty.setVisibility(View.GONE);
    }


    public void setErrorView(final View view) {
        empty.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                progressWheel.setVisibility(View.VISIBLE);
                progressWheel.setSpinSpeed((float) 1);
                mErrorView.setVisibility(View.GONE);
                progressWheel.setRimColor(Color.GRAY);
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                progressWheel.setVisibility(View.GONE);
                                getPeopleOrgRequest(view);
                            }
                        }, 1000);

            }
        });
    }
}
