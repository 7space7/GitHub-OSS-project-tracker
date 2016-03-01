package com.ua.viktor.github.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.ua.viktor.github.R;
import com.ua.viktor.github.adapter.RepositoriesAdapter;
import com.ua.viktor.github.model.Repositories;
import com.ua.viktor.github.retrofit.RepositoryService;
import com.ua.viktor.github.retrofit.ServiceGenerator;
import com.ua.viktor.github.utils.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;


public class RepositoriesFragment extends Fragment {
    private Call<ArrayList<Repositories>> call;
    private RepositoriesAdapter mRepositoriesAdapter;
    private static final String KEY_REQ = "key";
    private static final String KEY_LOGIN = "key_login";
    private static String REPO_KEY = "repo";
    private RecyclerView mRecyclerView;
    private ArrayList<Repositories> mRepositoriesList;
    private ErrorView mErrorView;
    private ProgressWheel progressWheel;
    // TODO: Rename and change types of parameters
    private String key;
    private String mLogin;
    private ProgressBar empty;

    public RepositoriesFragment() {
        // Required empty public constructor
    }

    public static RepositoriesFragment newInstance(String key,String mLogin) {
        RepositoriesFragment fragment = new RepositoriesFragment();
        Bundle args = new Bundle();
        args.putString(KEY_REQ, key);
        args.putString(KEY_LOGIN,mLogin);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key = getArguments().getString(KEY_REQ);
            mLogin=getArguments().getString(KEY_LOGIN);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // if (mRepositoriesList != null) {
        outState.putParcelableArrayList(REPO_KEY, mRepositoriesList);
        super.onSaveInstanceState(outState);
        /// }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_repositories, container, false);
        initializeScreen(view);

        if (savedInstanceState == null) {
            getRepositoryRequest(view);
        } else {

            mRepositoriesList = savedInstanceState.getParcelableArrayList(REPO_KEY);
            int size = (mRepositoriesList == null) ? 0 : mRepositoriesList.size();
            if (mRepositoriesList != null&&size!=0) {
                mRepositoriesAdapter.swapList(mRepositoriesList);
            } else {
                setErrorView(view);
            }
        }

        mRecyclerView.setAdapter(mRepositoriesAdapter);
        mRepositoriesAdapter.SetOnClickListener(new RepositoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    public void getRepositoryRequest(final View rootView) {




        RepositoryService client = ServiceGenerator.createService(RepositoryService.class);

        if (key.equals(Constants.KEY_YOUR)) {
            call = client.userRepositories(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        } else if (key.equals(Constants.KEY_STARRED)) {
            call = client.repoStarred(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        } else if (key.equals(Constants.KEY_WATCHED)) {
            call = client.repoWatched(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        }

        call.enqueue(new Callback<ArrayList<Repositories>>() {
            @Override
            public void onResponse(Response<ArrayList<Repositories>> response) {
                mRepositoriesList = response.body();
                Log.v("count", "call");
                int size = (mRepositoriesList == null) ? 0 : mRepositoriesList.size();
                if (mRepositoriesList != null&&size!=0) {
                    mRepositoriesAdapter.swapList(mRepositoriesList);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mErrorView.setVisibility(View.GONE);
                    empty.setVisibility(View.GONE);
                } else {
                    setErrorView(rootView);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.v("Error", t.getLocalizedMessage());
                setErrorView(rootView);
            }
        });
    }

    private void initializeScreen(View rootView) {
        empty= (ProgressBar) rootView.findViewById(android.R.id.empty);
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        mErrorView = (ErrorView) rootView.findViewById(R.id.error_view);
        mRepositoriesAdapter = new RepositoriesAdapter(mRepositoriesList);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.repo_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setErrorView(final View view) {

        mRecyclerView.setVisibility(View.GONE);
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
                                getRepositoryRequest(view);
                            }
                        }, 1000);

            }
        });
    }
}
