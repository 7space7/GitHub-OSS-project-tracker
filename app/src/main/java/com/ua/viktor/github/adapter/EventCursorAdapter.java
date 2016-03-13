package com.ua.viktor.github.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ua.viktor.github.R;
import com.ua.viktor.github.data.GitContract;
import com.ua.viktor.github.utils.CircleTransform;
import com.ua.viktor.github.utils.TimeStampFormatter;
import com.ua.viktor.github.utils.Utils;

/**
 * Created by viktor on 04.03.16.
 */
public class EventCursorAdapter extends CursorRecyclerViewAdapter<EventCursorAdapter.ViewHolder> {
    private Context mContext;
    private TimeStampFormatter timeStampFormatter;

    public EventCursorAdapter(Context context, Cursor cursor,TimeStampFormatter timeStampFormatter) {
        super(context, cursor);
        mContext = context;
        this.timeStampFormatter = timeStampFormatter;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameEvent;
        public TextView time;
        public ImageView userLogo;

        public ViewHolder(View view) {
            super(view);
            nameEvent = (TextView) itemView.findViewById(R.id.text_eventName);
            time = (TextView) itemView.findViewById(R.id.text_eventTime);
            userLogo = (ImageView) itemView.findViewById(R.id.user_logo);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_item, parent, false);
        ViewHolder vh = new ViewHolder(itemView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Cursor cursor) {
       // DatabaseUtils.dumpCursor(cursor);

        String login=cursor.getString(cursor.getColumnIndex(GitContract.EventEntry.COLUMN_LOGIN));
        String icon=cursor.getString(cursor.getColumnIndex(GitContract.EventEntry.COLUMN_ICON));
        String typeEvent=cursor.getString(cursor.getColumnIndex(GitContract.EventEntry.COLUMN_EVENT_TYPE));
        String repo_name=cursor.getString(cursor.getColumnIndex(GitContract.EventEntry.COLUMN_REPO_NAME));
        String date=cursor.getString(cursor.getColumnIndex(GitContract.EventEntry.COLUMN_DATE));

        String typeName = getEvent(typeEvent);
        int sizeLogin = login.length();
        int sizeType = typeName.length();
        int sizeRepo = repo_name.length();
        int preSize = sizeLogin + sizeType + 1;
        int size = sizeLogin + sizeType + sizeRepo + 2;

        SpannableStringBuilder eventName = new SpannableStringBuilder(login + " " + typeName + " " + repo_name);

        StyleSpan bss = new StyleSpan(Typeface.BOLD);
        StyleSpan iss = new StyleSpan(Typeface.BOLD_ITALIC);

        eventName.setSpan(bss, 0, sizeLogin, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        eventName.setSpan(iss, preSize, size, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        viewHolder.nameEvent.setText(eventName);


        viewHolder.time.setText(timeStampFormatter.format(Utils.dateIso(date)));

        Picasso.with(mContext).load(icon)
                .transform(new CircleTransform())
                .into(viewHolder.userLogo);



    }

    private String getEvent(String event) {
        String typeName = "";
        switch (event) {
            case "PushEvent":
                typeName = "pushed to";
                break;
            case "IssueCommentEvent":
                typeName = "commented on issue";
                break;
            case "MemberEvent":
                typeName = "";
                break;
            case "IssuesEvent":
                typeName = "commented on issue";
                break;
            case "GollumEvent":
                typeName = "opened";
                break;
            case "WatchEvent":
                typeName = "starred";
                break;
            case "CreateEvent":
                typeName = "created repository";
                break;
        }
        return typeName;
    }
}
