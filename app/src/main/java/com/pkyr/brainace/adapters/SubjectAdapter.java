package com.pkyr.brainace.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.pkyr.brainace.AssignmentListViewActivity;
import com.pkyr.brainace.AssignmentViewActivity;
import com.pkyr.brainace.R;
import com.pkyr.brainace.model.SubjectModel;

import java.util.ArrayList;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ItemViewHolder> {

    private Context context;
    private ArrayList<SubjectModel> subjectList;

    public SubjectAdapter(Context context, ArrayList<SubjectModel> subjectList) {
        this.context = context;
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
        holder.subjectNameView.setText(subjectList.get(position).getSubjectName());
        holder.subjectTeacherView.setText(subjectList.get(position).getSubjectTeacher());
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
            Intent intent = new Intent(context, AssignmentListViewActivity.class);
            intent.putExtra("subject_name", subjectNameView.getText().toString());
            intent.putExtra("subject_teacher", subjectTeacherView.getText().toString());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}
