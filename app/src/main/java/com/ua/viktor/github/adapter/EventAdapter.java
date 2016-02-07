package com.ua.viktor.github.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ua.viktor.github.R;
import com.ua.viktor.github.model.Event;
import com.ua.viktor.github.utils.CircleTransform;

import java.util.List;

/**
 * Created by viktor on 04.02.16.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private List<Event> mEvents;
    private OnItemClickListener mOnClickListener;
    public EventAdapter(List<Event> events) {
        this.mEvents = events;
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
        Event event=mEvents.get(position);

        holder.nameEvent.setText(event.getActor().getLogin()+" "+event.getRepo().getName());
        Context context=holder.userLogo.getContext();

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
        if (mEvents == null){
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
            nameEvent= (TextView) itemView.findViewById(R.id.text_eventName);
            time=(TextView)itemView.findViewById(R.id.text_eventTime);
            userLogo=(ImageView)itemView.findViewById(R.id.user_logo);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }
    public void swapList(List<Event> items){
        this.mEvents = items;
        notifyDataSetChanged();
    }
}
