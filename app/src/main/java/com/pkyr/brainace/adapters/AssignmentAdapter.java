package com.pkyr.brainace.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.pkyr.brainace.AssignmentListViewActivity;
import com.pkyr.brainace.AssignmentUploadActivity;
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
        if(assignmentModel.getStatus().equals("true")) {
            holder.uploadBtn.setText("Uploaded");
            holder.uploadBtn.setEnabled(false);
        } else {
            holder.uploadBtn.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return assignmentList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name, teacher, date, lastDate, subject;
        Button uploadBtn;
        ProgressBar progressBar;

        public ItemViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.model_assignment_name);
            date = view.findViewById(R.id.model_assignment_date);
            teacher = view.findViewById(R.id.model_assignment_teacher);
            lastDate = view.findViewById(R.id.model_assignment_last_date);
            subject = view.findViewById(R.id.model_assignment_subject);
            uploadBtn = view.findViewById(R.id.model_assignment_uploadBtn);

            view.setOnClickListener(this);
            uploadBtn.setOnClickListener(v -> {

                Intent intent = new Intent(context, AssignmentUploadActivity.class);

                intent.putExtra("assignment_name", name.getText().toString());
                intent.putExtra("assignment_teacher", teacher.getText().toString());
                intent.putExtra("assignment_date", date.getText().toString());
                intent.putExtra("assignment_last_date", lastDate.getText().toString());
                intent.putExtra("assignment_subject", subject.getText().toString());
                context.startActivity(intent);

            });
        }

        @Override
        public void onClick(View v) {
            // write your code here
        }
    }
}
