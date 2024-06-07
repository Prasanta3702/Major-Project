package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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


        binding.assignmentCreateBtn.setOnClickListener(v -> {
            String name = binding.assignmentName.getText().toString().trim();
            String subject = binding.assignmentSubject.getText().toString().trim();
            String teacher = binding.assignmentTeacher.getText().toString().trim();
            String date = binding.assignmentDate.getText().toString().trim();
            String lastDate = binding.assignmentLastDate.getText().toString().trim();
            String question = binding.assignmentQuestion.getText().toString().trim();
            
            if(!name.isEmpty() && !subject.isEmpty() && !teacher.isEmpty() && !date.isEmpty() && !lastDate.isEmpty() && !question.isEmpty())
                createAssignment(new AssignmentModel(name, subject, teacher, date, lastDate, question));
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
}