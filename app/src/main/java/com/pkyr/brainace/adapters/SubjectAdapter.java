package com.pkyr.brainace.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkyr.brainace.AssignmentViewActivity;
import com.pkyr.brainace.R;
import com.pkyr.brainace.model.SubjectModel;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ItemViewHolder> {

    private AssignmentViewActivity activity;
    private ArrayList<SubjectModel> subjectList;

    public SubjectAdapter(AssignmentViewActivity activity, ArrayList<SubjectModel> subjectList) {
        this.activity = activity;
        this.subjectList = subjectList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_subject, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        SubjectModel subjectModel = subjectList.get(position);
        holder.subjectNameView.setText(subjectModel.getSubjectName());
        holder.subjectTeacherView.setText(subjectModel.getSubjectTeacher());
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView subjectNameView, subjectTeacherView;

        public ItemViewHolder(View view) {
            super(view);

            subjectNameView = view.findViewById(R.id.model_subject_name);
            subjectTeacherView = view.findViewById(R.id.model_subject_teacher);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            activity.navigateToAssignments();
        }
    }
}
