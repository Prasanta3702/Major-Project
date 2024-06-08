package com.pkyr.brainace;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pkyr.brainace.databinding.ActivityAssignmentCreateBinding;
import com.pkyr.brainace.model.AssignmentModel;

public class AssignmentCreateActivity extends AppCompatActivity {

    ActivityAssignmentCreateBinding binding;
    private String subjectName;
    private String subjectTeacher;
    private Uri pdfURI = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        subjectName = getIntent().getStringExtra("subject_name");
        subjectTeacher = getIntent().getStringExtra("subject_teacher");
        binding.assignmentSubject.setText(subjectName);
        binding.assignmentTeacher.setText(subjectTeacher);


        ActivityResultLauncher<String> pdfLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    try {
                        if (result != null) {
                            pdfURI = result;
                            String pdfName = getFileName(pdfURI);
                            binding.assignmentQuestion.setText(pdfName);
                        }
                    } catch (Exception ignored) {}
                });

        binding.assignmentQuestionChoose.setOnClickListener(v -> {
            pdfLauncher.launch("application/pdf");
        });


        binding.assignmentCreateBtn.setOnClickListener(v -> {
            String name = binding.assignmentName.getText().toString().trim();
            String subject = binding.assignmentSubject.getText().toString().trim();
            String teacher = binding.assignmentTeacher.getText().toString().trim();
            String date = binding.assignmentDate.getText().toString().trim();
            String lastDate = binding.assignmentLastDate.getText().toString().trim();
            String question = "";
            if(pdfURI != null) question = String.valueOf(pdfURI.toString());

            if(name.isEmpty()) binding.assignmentName.setError("Name required");
            else if(subject.isEmpty()) binding.assignmentSubject.setError("Subject required");
            else if(teacher.isEmpty()) binding.assignmentTeacher.setError("Teacher required");
            else if(date.isEmpty()) binding.assignmentDate.setError("Date required");
            else if(lastDate.isEmpty()) binding.assignmentLastDate.setError("Last date required");
            else if(question.isEmpty()) binding.assignmentQuestion.setError("Choose a question");
            else {
                AssignmentModel a = new AssignmentModel();
                a.setAssignment_name(name);
                a.setAssignment_subject(subject);
                a.setAssignment_teacher(teacher);
                a.setAssignment_date(date);
                a.setAssignment_last_date(lastDate);
                a.setAssignment_question(question);

                createAssignment(a);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    
    private void createAssignment(AssignmentModel assignmentModel) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Creating...");
        progressDialog.show();

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference();

            StorageReference assignmentPDFRef = storageReference.child("bwu")
                    .child(MainActivity.userModel.getCourse())
                    .child(MainActivity.userModel.getBatch())
                    .child(MainActivity.userModel.getSem())
                    .child(MainActivity.userModel.getSec())
                    .child("subjects")
                    .child(subjectName)
                    .child("assignments")
                    .child(assignmentModel.getAssignment_name());

            assignmentPDFRef.putFile(Uri.parse(assignmentModel.getAssignment_question())).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()) {
                        assignmentPDFRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()) {
                                    assignmentModel.setAssignment_question(task.getResult().toString());

                                    // load to database
                                    DatabaseReference assignmentRef = databaseReference.child("bwu")
                                            .child(MainActivity.userModel.getCourse())
                                            .child(MainActivity.userModel.getBatch())
                                            .child(MainActivity.userModel.getSem())
                                            .child(MainActivity.userModel.getSec())
                                            .child("subjects")
                                            .child(subjectName)
                                            .child("assignments")
                                            .child(assignmentModel.getAssignment_name());

                                    assignmentRef.setValue(assignmentModel).addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            // created success
                                            progressDialog.dismiss();
                                            AssignmentCreateActivity.this.onBackPressed();

                                        } else {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(), "Assignment creation failed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Couldn't download file", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Couldn't store file", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}