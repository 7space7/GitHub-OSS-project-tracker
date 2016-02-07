package com.ua.viktor.github.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;
import com.ua.viktor.github.R;
import com.ua.viktor.github.model.Organizations;

import java.util.ArrayList;

/**
 * Created by viktor on 07.02.16.
 */
public class PeopleOrgAdapter extends RecyclerView.Adapter<PeopleOrgAdapter.ViewHolder> {
    private ArrayList<Organizations> mOrganizationsList;
    private OnItemClickListener mOnClickListener;

    public PeopleOrgAdapter(ArrayList<Organizations> organizationses) {
        this.mOrganizationsList = organizationses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.org_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v, parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url=mOrganizationsList.get(position).getAvatar_url();
        holder.userLogin.setText(mOrganizationsList.get(position).getLogin());
        Context context = holder.userLogo.getContext();
        Picasso.with(context).load(url)
                .fit().centerInside()
                .into(holder.userLogo,
                        PicassoPalette.with(url, holder.userLogo)
                                .use(PicassoPalette.Profile.VIBRANT)
                                .intoBackground(holder.userLogin)
                                .intoTextColor(holder.userLogin)
                        );

    }

    @Override
    public int getItemCount() {
        if (mOrganizationsList == null){
            return -1;
        }
        return mOrganizationsList.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView userLogo;
        public TextView userLogin;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            userLogo = (ImageView) itemView.findViewById(R.id.img_thumbnail);
            userLogin = (TextView) itemView.findViewById(R.id.tv_species);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public void swapList(ArrayList<Organizations> items) {
        this.mOrganizationsList = items;
        notifyDataSetChanged();
    }
}
