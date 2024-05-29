package com.pkyr.brainace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkyr.brainace.R;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ItemViewHolder> {

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_assignment, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ItemViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // write your code here
        }
    }
}
