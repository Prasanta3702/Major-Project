package com.pkyr.brainace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkyr.brainace.R;

public class NoticeViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public NoticeViewAdapter() {
        // receive context
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_notice, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(@NonNull View view) {
            super(view);
        }
    }

}