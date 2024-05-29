package com.pkyr.brainace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkyr.brainace.AssignmentViewActivity;
import com.pkyr.brainace.R;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ItemViewHolder> {

    private AssignmentViewActivity activity;

    public SubjectAdapter(AssignmentViewActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_subject, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ItemViewHolder(View view) {
            super(view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            activity.navigateToAssignments();
        }
    }
}
