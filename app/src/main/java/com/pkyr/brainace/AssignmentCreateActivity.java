package com.pkyr.brainace;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pkyr.brainace.databinding.ActivityAssignmentCreateBinding;
import com.pkyr.brainace.model.AssignmentModel;

public class AssignmentCreateActivity extends AppCompatActivity {

    ActivityAssignmentCreateBinding binding;
    private String subjectName;
    private String subjectTeacher;
    private Uri pdfURI = null;
    private String pdfName = null;

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
                    if(result != null) {
                        pdfURI = result;
                        pdfName = getFileName(pdfURI);
                        binding.assignmentQuestion.setText(pdfName);
                    }
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
            String question = binding.assignmentQuestion.getText().toString().trim();

            AssignmentModel assignmentModel = new AssignmentModel(name, subject, teacher, date, lastDate, question);
            assignmentModel.setAssignment_question(pdfName);
            
            if(!name.isEmpty() && !subject.isEmpty() && !teacher.isEmpty() && !date.isEmpty() && !lastDate.isEmpty() && pdfName != null)
                createAssignment(assignmentModel);
            else
                Toast.makeText(getApplicationContext(), "Field required", Toast.LENGTH_SHORT).show();
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
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference();

            DatabaseReference assignmentRef = databaseReference.child("bwu")
                    .child(MainActivity.userModel.getCourse())
                    .child(MainActivity.userModel.getBatch())
                    .child(MainActivity.userModel.getSem())
                    .child(MainActivity.userModel.getSec())
                    .child("subjects")
                    .child(subjectName)
                    .child("assignments")
                    .child(assignmentModel.getAssignment_name());

            assignmentRef.setValue(assignmentModel).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    super.onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), "Assignment creation failed", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
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