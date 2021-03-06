package com.ua.viktor.github.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ua.viktor.github.R;
import com.ua.viktor.github.adapter.EventAdapter;
import com.ua.viktor.github.model.Event;
import com.ua.viktor.github.rest.EventService;
import com.ua.viktor.github.rest.ServiceGenerator;
import com.ua.viktor.github.utils.Constants;
import com.ua.viktor.github.utils.TimeStampFormatter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tr.xip.errorview.ErrorView;


/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {

    private static final String KEY_LOGIN ="KEY_LOGIN" ;
    private Call<List<Event>> call;
    private EventAdapter mEventAdapter;
    private RecyclerView mRecyclerView;
    private List<Event> mEvents;
    private ErrorView mErrorView;
    private String mLogin;
    private ProgressBar empty;



    public EventFragment() {
        // Required empty public constructor
    }

    public static EventFragment newInstance(String mLogin) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(KEY_LOGIN, mLogin);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mLogin=getArguments().getString(KEY_LOGIN);
        }
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
        empty= (ProgressBar) rootView.findViewById(android.R.id.empty);
        mErrorView = (ErrorView) rootView.findViewById(R.id.error_view_event);
        mEventAdapter = new EventAdapter(mEvents,new TimeStampFormatter());
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void setErrorView(final View view) {
        empty.setVisibility(View.GONE);
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

        EventService client = ServiceGenerator.createService(EventService.class);
        if(mLogin!=null)
            call = client.userEvent(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);

        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Response<List<Event>> response) {
                mEvents = response.body();
                int size = (mEvents == null) ? 0 : mEvents.size();
                if (mEvents != null && size != 0) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mErrorView.setVisibility(View.GONE);
                    empty.setVisibility(View.GONE);
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
