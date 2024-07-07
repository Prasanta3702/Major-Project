package com.pkyr.brainace.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pkyr.brainace.AssignmentListViewActivity;
import com.pkyr.brainace.AssignmentUploadActivity;
import com.pkyr.brainace.BuildConfig;
import com.pkyr.brainace.MainActivity;
import com.pkyr.brainace.R;
import com.pkyr.brainace.model.AssignmentModel;

import java.io.File;
import java.util.ArrayList;

public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.ItemViewHolder> {

    ArrayList<AssignmentModel> assignmentList;
    AssignmentListViewActivity activity;

    public AssignmentAdapter(AssignmentListViewActivity activity, ArrayList<AssignmentModel> assignmentList) {
        this.activity = activity;
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

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView name, teacher, date, lastDate, subject;
        ImageButton uploadBtn;

        public ItemViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.model_assignment_name);
            date = view.findViewById(R.id.model_assignment_date);
            teacher = view.findViewById(R.id.model_assignment_teacher);
            lastDate = view.findViewById(R.id.model_assignment_last_date);
            subject = view.findViewById(R.id.model_assignment_subject);
            uploadBtn = view.findViewById(R.id.model_assignment_uploadBtn);

            view.setOnClickListener(this);
            view.setOnLongClickListener(this);

            uploadBtn.setOnClickListener(v -> {

                Intent intent = new Intent(activity.getApplicationContext(), AssignmentUploadActivity.class);
                intent.putExtra("assignment_name", name.getText().toString());
                intent.putExtra("assignment_teacher", teacher.getText().toString());
                intent.putExtra("assignment_date", date.getText().toString());
                intent.putExtra("assignment_last_date", lastDate.getText().toString());
                intent.putExtra("assignment_subject", subject.getText().toString());
                activity.startActivity(intent);

            });
        }

        @Override
        public void onClick(View v) {
            // write your code here
            loadAssignmentQuestion(name.getText().toString(), subject.getText().toString());
        }

        @Override
        public boolean onLongClick(View view) {
            activity.deleteAssignment(name.getText().toString());
            return true;
        }
    }

    private void loadAssignmentQuestion(String assignmentName, String subject) {
        try {

            Thread thread = new Thread(() -> {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference databaseReference = database.getReference();

                DatabaseReference ref = databaseReference.child("bwu")
                        .child(MainActivity.userModel.getCourse())
                        .child(MainActivity.userModel.getBatch())
                        .child(MainActivity.userModel.getSem())
                        .child(MainActivity.userModel.getSec())
                        .child("subjects")
                        .child(subject)
                        .child("assignments")
                        .child(assignmentName);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            AssignmentModel assignmentModel = snapshot.getValue(AssignmentModel.class);

                            Uri uri = Uri.parse(assignmentModel.getAssignment_question());
                            downloadAndOpenPDF(uri, subject+"_"+assignmentName);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(activity.getApplicationContext(), "Couldn't load assignment", Toast.LENGTH_SHORT).show();
                    }
                });
            });

            thread.start();

        } catch (Exception e) {
            Toast.makeText(activity.getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadAndOpenPDF(Uri uri, String fileName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(uri.toString());

        final File localFile = new File(activity.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName+".pdf");

        httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Successfully downloaded the file
                // Toast.makeText(context, "File saved", Toast.LENGTH_SHORT).show();
                openPDF(localFile);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                Toast.makeText(activity.getApplicationContext(), "Download failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openPDF(File file) {
        Uri pdfUri = FileProvider.getUriForFile(activity.getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent chooser = Intent.createChooser(intent, "Open PDF");
        try {
            activity.startActivity(chooser);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity.getApplicationContext(), "No application to view PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
