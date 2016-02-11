package com.ua.viktor.github.fragment;


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
import com.ua.viktor.github.utils.Utils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    private Call<List<Event>> call;
    private EventAdapter mEventAdapter;
    private RecyclerView mRecyclerView;
    private List<Event> mEvents;
    private ErrorView mErrorView;

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

        final View view = inflater.inflate(R.layout.event_fragment, container, false);
        getActivity().setTitle("Events");
        initializeScreen(view);
        getEventRequest(view);

        mRecyclerView.setAdapter(mEventAdapter);

        return view;
    }


    private void initializeScreen(View rootView) {
        mErrorView = (ErrorView) rootView.findViewById(R.id.error_view_event);
        mEventAdapter = new EventAdapter(mEvents);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setErrorView(final View view) {

        mRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                getEventRequest(view);
            }
        });
    }

    private void getEventRequest(final View view) {
        String authName = Utils.getUserAuthName(getActivity());
        EventService client = ServiceGenerator.createService(EventService.class);
        call = client.userEvent(authName, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Response<List<Event>> response) {
                mEvents = response.body();
                int size = (mEvents == null) ? 0 : mEvents.size();
                if (mEvents != null && size != 0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mErrorView.setVisibility(View.GONE);
                    mEventAdapter.swapList(mEvents);
                } else {
                    setErrorView(view);
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Log.v("TAG", "error");
                setErrorView(view);
            }
        });

    }
}
