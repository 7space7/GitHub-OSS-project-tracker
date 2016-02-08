package com.ua.viktor.github.fragment;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ua.viktor.github.R;
import com.ua.viktor.github.adapter.EventAdapter;
import com.ua.viktor.github.model.Event;
import com.ua.viktor.github.retrofit.EventService;
import com.ua.viktor.github.retrofit.ServiceGenerator;
import com.ua.viktor.github.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    private Call<List<Event>> call;
    private EventAdapter mEventAdapter;
    private RecyclerView mRecyclerView;
    private List<Event> mEvents;

    public EventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_fragment, container, false);
        getActivity().setTitle("Events");
        initializeScreen(view);
        AccountManager accountManager = AccountManager.get(getContext());
        String accountType = "com.github";
        String authType = "password";
        Account[] accounts = accountManager.getAccountsByType(accountType);
        Account account = accounts.length != 0 ?  accounts[0] : null;
        if (account!=null) {
            String authToken = accountManager.peekAuthToken(account, authType);
            Log.v("auth",authToken);
        }
        EventService client = ServiceGenerator.createService(EventService.class);
        call = client.userEvent("7space7", Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Response<List<Event>> response) {
                mEvents = response.body();
                mEventAdapter.swapList(mEvents);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.v("TAG", "error");
            }
        });

        mRecyclerView.setAdapter(mEventAdapter);
        return view;
    }


    private void initializeScreen(View rootView) {
        mEventAdapter = new EventAdapter(mEvents);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
