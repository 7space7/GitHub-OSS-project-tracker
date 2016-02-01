package com.ua.viktor.github.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ua.viktor.github.R;
import com.ua.viktor.github.model.Repositories;

import java.util.List;

/**
 * Created by viktor on 28.01.16.
 */
public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.ViewHolder> {
    private List<Repositories> mRepositories;
    private OnItemClickListener mOnClickListener;
    public RepositoriesAdapter(List<Repositories> repositories) {
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
      Repositories repositories=mRepositories.get(position);
        holder.nameRepo.setText(repositories.getName());
    }
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnClickListener(final OnItemClickListener onItemClickListener) {
        this.mOnClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mRepositories == null){
            return -1;
        }
        return mRepositories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameRepo;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            nameRepo= (TextView) itemView.findViewById(R.id.reponame);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener != null) {
                mOnClickListener.onItemClick(v, getLayoutPosition());
            }
        }
    }
    public void swapList(List<Repositories> items){
        this.mRepositories = items;
        notifyDataSetChanged();
    }
}
