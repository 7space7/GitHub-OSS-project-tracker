package com.ua.viktor.github.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.viktor.github.R;
import com.ua.viktor.github.model.Users;
import com.ua.viktor.github.retrofit.ServiceGenerator;
import com.ua.viktor.github.retrofit.UserService;
import com.ua.viktor.github.utils.Constants;
import com.ua.viktor.github.utils.TimeStampFormatter;
import com.ua.viktor.github.utils.Utils;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UserDetailFragment extends Fragment {

    private static final String KEY_USER_ID = "user_id";
    private static final String USER_KEY = "USER_KEY";
    private static final String TAG = UserDetailFragment.class.getName();
    private Call<Users> mUsersCall;
    private String mUserID;
    private Users mUsers;
    private ImageView mLocation_image;
    private ImageView mMail_image;
    private ImageView mLink_image;
    private ImageView mRepo_image;
    private ImageView mClock_image;
    private TextView mText_created_at;
    private TextView mText_location;
    private TextView mText_email;
    private TextView mText_blog;
    private TextView mText_public_repos;

    public UserDetailFragment() {
        // Required empty public constructor
    }

    public static UserDetailFragment newInstance(String userID) {
        UserDetailFragment fragment = new UserDetailFragment();
        Bundle args = new Bundle();
        args.putString(KEY_USER_ID, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(USER_KEY, mUsers);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUserID = getArguments().getString(KEY_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_detail, container, false);
        initializeScreen(view);

        if (savedInstanceState == null) {
            getUserResponce();
        } else {
            mUsers = savedInstanceState.getParcelable(USER_KEY);
            setUserData(mUsers);
        }


        return view;
    }

    public void getUserResponce() {
        UserService client = ServiceGenerator.createService(UserService.class);
        mUsersCall = client.getSingleUser(mUserID, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        mUsersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Response<Users> response) {
                Log.v(TAG, "call");
                mUsers = response.body();
                if (mUsers != null)
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setUserData(mUsers);
                        }
                    });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void setUserData(Users users) {
        Date date = Utils.dateIso(users.getCreated_at());
        TimeStampFormatter time = new TimeStampFormatter();
        if (date != null) {
            mRepo_image.setVisibility(View.VISIBLE);
            mClock_image.setVisibility(View.VISIBLE);
            mText_public_repos.setText(users.getPublic_repos() + " Repositories");
            mText_created_at.setText("Joined " + time.format(date));
        }
        if (users.getEmail() != null) {
            mText_email.setVisibility(View.VISIBLE);
            mMail_image.setVisibility(View.VISIBLE);
            mText_email.setText("" + users.getEmail());
        }

        if (users.getLocation() != null) {
            mLocation_image.setVisibility(View.VISIBLE);
            mText_location.setVisibility(View.VISIBLE);
            mText_location.setText("" + users.getLocation());
        }
        if (users.getBlog() != null) {
            mLink_image.setVisibility(View.VISIBLE);
            mText_blog.setVisibility(View.VISIBLE);
            mText_blog.setText("" + users.getBlog());
        }

    }

    private void initializeScreen(View view) {
        mClock_image= (ImageView) view.findViewById(R.id.clock_image);
        mRepo_image= (ImageView) view.findViewById(R.id.repo_image);
        mLink_image = (ImageView) view.findViewById(R.id.link_image);
        mLocation_image = (ImageView) view.findViewById(R.id.location_image);
        mMail_image = (ImageView) view.findViewById(R.id.mail_image);

        mText_created_at = (TextView) view.findViewById(R.id.text_created_at);
        mText_blog = (TextView) view.findViewById(R.id.text_blog);
        mText_location = (TextView) view.findViewById(R.id.text_location);
        mText_public_repos = (TextView) view.findViewById(R.id.text_public_repos);
        mText_email = (TextView) view.findViewById(R.id.text_email);

    }


}
