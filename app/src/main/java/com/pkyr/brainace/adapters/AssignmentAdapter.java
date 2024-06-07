package com.pkyr.brainace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkyr.brainace.R;
import com.pkyr.brainace.model.AssignmentModel;

import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ItemViewHolder> {

    Context context;
    ArrayList<AssignmentModel> assignmentList;

    public AssignmentAdapter(Context context, ArrayList<AssignmentModel> assignmentList) {
        this.context = context;
        this.assignmentList = assignmentList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_assignment, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        AssignmentModel assignmentModel = assignmentList.get(position);
        holder.name.setText(assignmentModel.getAssignment_name());
        holder.teacher.setText(assignmentModel.getAssignment_teacher());
        holder.lastDate.setText(assignmentModel.getAssignment_last_date());
        holder.date.setText(assignmentModel.getAssignment_date());
        holder.subject.setText(assignmentModel.getAssignment_subject());
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, teacher, date, lastDate, subject;

        public ItemViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.model_assignment_name);
            date = view.findViewById(R.id.model_assignment_date);
            teacher = view.findViewById(R.id.model_assignment_teacher);
            lastDate = view.findViewById(R.id.model_assignment_last_date);
            subject = view.findViewById(R.id.model_assignment_subject);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // write your code here
        }
    }
}
