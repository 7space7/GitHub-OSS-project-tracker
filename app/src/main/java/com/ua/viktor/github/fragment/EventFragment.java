package com.ua.viktor.github.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ua.viktor.github.R;
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

    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_fragment, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Events");


        EventService client = ServiceGenerator.createService(EventService.class);
        call = client.userEvent("7space7", Constants.CLIENT_ID,Constants.CLIENT_SECRET);
        call.enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Response<List<Event>> response) {
                List<Event> list = response.body();
                Log.v("TAG", "" + list.get(0).actor.getLogin());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.v("TAG", "error");
            }
        });
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        call.cancel();
    }


}
