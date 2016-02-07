package com.ua.viktor.github.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ua.viktor.github.R;
import com.ua.viktor.github.model.Repositories;

import java.util.ArrayList;

/**
 * Created by viktor on 28.01.16.
 */
public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.ViewHolder> {
    private ArrayList<Repositories> mRepositories;
    private OnItemClickListener mOnClickListener;

    public RepositoriesAdapter(ArrayList<Repositories> repositories) {
        this.mRepositories = repositories;
    }

    @Override
    public RepositoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.repo_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v, viewGroup.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Repositories repositories = mRepositories.get(position);
        holder.nameRepo.setText(repositories.getName());
        holder.starred.setText("" + repositories.getStargazers_count());
        holder.forks.setText("" + repositories.getForks_count());
        holder.textDescr.setText(repositories.getDescription());
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mRepositories == null) {
            return -1;
        }
        return mRepositories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameRepo;
        public TextView starred;
        public TextView forks;
        public TextView textDescr;

        public ViewHolder(View itemView, Context context) {
            super(itemView);
            nameRepo = (TextView) itemView.findViewById(R.id.reponame);
            starred = (TextView) itemView.findViewById(R.id.starredNum);
            forks = (TextView) itemView.findViewById(R.id.forksNum);
            textDescr = (TextView) itemView.findViewById(R.id.textdescr);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }

    public void swapList(ArrayList<Repositories> items) {
        this.mRepositories = items;
        notifyDataSetChanged();
    }
}
