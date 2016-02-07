package com.ua.viktor.github.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ua.viktor.github.R;
import com.ua.viktor.github.adapter.RepositoriesAdapter;
import com.ua.viktor.github.model.Repositories;
import com.ua.viktor.github.retrofit.RepositoryService;
import com.ua.viktor.github.retrofit.ServiceGenerator;
import com.ua.viktor.github.utils.Constants;
import com.ua.viktor.github.utils.Utils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;


public class RepositoriesFragment extends Fragment {
    private Call<ArrayList<Repositories>> call;
    private RepositoriesAdapter mRepositoriesAdapter;
    private static final String KEY_REQ = "key";
    private static String REPO_KEY = "repo";
    private RecyclerView mRecyclerView;
    private ArrayList<Repositories> mRepositoriesList;
    private ErrorView mErrorView;

    // TODO: Rename and change types of parameters
    private String key;

    public RepositoriesFragment() {
        // Required empty public constructor
    }

    public static RepositoriesFragment newInstance(String key) {
        RepositoriesFragment fragment = new RepositoriesFragment();
        Bundle args = new Bundle();
        args.putString(KEY_REQ, key);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            key = getArguments().getString(KEY_REQ);
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
            mRepositoriesAdapter.swapList(mRepositoriesList);
        }

        mRecyclerView.setAdapter(mRepositoriesAdapter);
        mRepositoriesAdapter.SetOnClickListener(new RepositoriesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "" + position, Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    public void getRepositoryRequest(View rootView) {

        mErrorView = (ErrorView) rootView.findViewById(R.id.error_view);
        String authName = Utils.getUserAuthName(getActivity());

        RepositoryService client = ServiceGenerator.createService(RepositoryService.class);

        if (key.equals(Constants.KEY_YOUR)) {
            call = client.userRepositories(authName, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        } else if (key.equals(Constants.KEY_STARRED)) {
            call = client.repoStarred(authName, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        } else if (key.equals(Constants.KEY_WATCHED)) {
            call = client.repoWatched(authName, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        }

        call.enqueue(new Callback<ArrayList<Repositories>>() {
            @Override
            public void onResponse(Response<ArrayList<Repositories>> response) {
                mRepositoriesList = response.body();
                Log.v("count", "call");
                mRepositoriesAdapter.swapList(mRepositoriesList);

            }

            @Override
            public void onFailure(Throwable t) {
                Log.v("Error", t.getLocalizedMessage());


            }
        });
    }

    private void initializeScreen(View rootView) {
        mRepositoriesAdapter = new RepositoriesAdapter(mRepositoriesList);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.repo_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
