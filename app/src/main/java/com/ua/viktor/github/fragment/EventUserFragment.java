package com.ua.viktor.github.fragment;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.ua.viktor.github.R;
import com.ua.viktor.github.adapter.EventCursorAdapter;
import com.ua.viktor.github.data.GitContract;
import com.ua.viktor.github.sync.GithubSyncAdapter;
import com.ua.viktor.github.utils.TimeStampFormatter;

import tr.xip.errorview.ErrorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventUserFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EventFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private ErrorView mErrorView;
    private ProgressBar empty;
    private ProgressWheel progressWheel;
    private EventCursorAdapter mCursorAdapter;
    private static final int CURSOR_LOADER_ID = 0;

    public EventUserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "resume called");
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Cursor c = getActivity().getContentResolver().query(GitContract.EventEntry.CONTENT_URI,
                null, null, null, null);
        Log.i(LOG_TAG, "cursor count: " + c.getCount());
        if (c == null || c.getCount() == 0) {
            GithubSyncAdapter.syncImmediately(getActivity());
        }


        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);

        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.event_fragment, container, false);
        getActivity().setTitle("Events");
        initializeScreen(view);

        mCursorAdapter = new EventCursorAdapter(getActivity(), null, new TimeStampFormatter());
        mRecyclerView.setAdapter(mCursorAdapter);
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);


        return view;
    }


    private void initializeScreen(View rootView) {
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel_org);
        empty = (ProgressBar) rootView.findViewById(android.R.id.empty);
        mErrorView = (ErrorView) rootView.findViewById(R.id.error_view_event);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.event_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setErrorView(final View view) {
        empty.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                progressWheel.setVisibility(View.GONE);
                               updateEvent();
                            }
                        }, 1000);
            }
        });

    }

private void updateEvent(){
    GithubSyncAdapter.syncImmediately(getContext());
    Cursor c = getActivity().getContentResolver().query(GitContract.EventEntry.CONTENT_URI,
            null, null, null, null);
    Log.i(LOG_TAG, "cursor count: " + c.getCount());
    if (c != null || c.getCount() != 0) {
        mErrorView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }
}
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), GitContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
