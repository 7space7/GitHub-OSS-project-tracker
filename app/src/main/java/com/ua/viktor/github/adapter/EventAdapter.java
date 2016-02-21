package com.ua.viktor.github.adapter;

import android.content.Context;
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
import com.ua.viktor.github.model.Event;
import com.ua.viktor.github.utils.CircleTransform;
import com.ua.viktor.github.utils.TimeStampFormatter;
import com.ua.viktor.github.utils.Utils;

import java.util.List;

/**
 * Created by viktor on 04.02.16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<Event> mEvents;
    private OnItemClickListener mOnClickListener;
    private TimeStampFormatter timeStampFormatter;

    public EventAdapter(List<Event> events, TimeStampFormatter timeStampFormatter) {
        this.mEvents = events;
        this.timeStampFormatter = timeStampFormatter;
    }

    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v, viewGroup.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = mEvents.get(position);

        Context context = holder.userLogo.getContext();
        String typeName = "";
        switch (event.getType()) {
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
        if (typeName.equals("")) {
            typeName = event.getType();
        }
        int sizeLogin = event.getActor().getLogin().length();
        int sizeType = typeName.length();
        int sizeRepo = event.getRepo().getName().length();
        int preSize = sizeLogin + sizeType + 1;
        int size = sizeLogin + sizeType + sizeRepo + 2;


        SpannableStringBuilder eventName = new SpannableStringBuilder(event.getActor().getLogin() + " " + typeName + " " + event.getRepo().getName());

        StyleSpan bss = new StyleSpan(Typeface.BOLD);
        StyleSpan iss = new StyleSpan(Typeface.BOLD_ITALIC);

        eventName.setSpan(bss, 0, sizeLogin, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        eventName.setSpan(iss, preSize, size, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        holder.nameEvent.setText(eventName);


        holder.time.setText(timeStampFormatter.format(Utils.dateIso(event.getCreated_at())));

        Picasso.with(context).load(event.getActor().getAvatar_url())
                .transform(new CircleTransform())
                .into(holder.userLogo);

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mEvents == null) {
            return -1;
        }
        return mEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameEvent;
        public TextView time;
        public ImageView userLogo;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            nameEvent = (TextView) itemView.findViewById(R.id.text_eventName);
            time = (TextView) itemView.findViewById(R.id.text_eventTime);
            userLogo = (ImageView) itemView.findViewById(R.id.user_logo);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public void swapList(List<Event> items) {
        this.mEvents = items;
        notifyDataSetChanged();
    }
}
