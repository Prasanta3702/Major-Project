package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pkyr.brainace.databinding.ActivitySubjectCreateBinding;
import com.pkyr.brainace.model.SubjectModel;
import com.pkyr.brainace.model.UserModel;
import com.pkyr.brainace.utils.FirebaseUtils;

public class SubjectCreateActivity extends AppCompatActivity {

    ActivitySubjectCreateBinding binding;
    UserModel currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubjectCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        binding.subjectCreateBtn.setOnClickListener(v -> {

            String name = binding.subjectName.getText().toString().trim();
            String code = binding.subjectCode.getText().toString().trim();
            String teacher = binding.subjectTeacher.getText().toString().trim();

            // creating subject
            if(!name.isEmpty() && !code.isEmpty() && !teacher.isEmpty())
                createSubject(new SubjectModel(code, name, teacher));
            else {
                if (name.isEmpty()) binding.subjectName.setError("Name required");
                if (code.isEmpty()) binding.subjectCode.setError("Code required");
                if (teacher.isEmpty()) binding.subjectTeacher.setError("Teacher required");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserDetails();
    }

    public void loadUserDetails() {
        try {
            FirebaseUtils.currentUserDetails().get().addOnCompleteListener(task-> {
                if(task.isSuccessful()) {
                    currentUser = task.getResult().toObject(UserModel.class);
                }
            });
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void createSubject(SubjectModel subjectModel) {
        try {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            DatabaseReference subjectRef = databaseReference.child("bwu")
                    .child(currentUser.getCourse())
                    .child(currentUser.getBatch())
                    .child(currentUser.getSem())
                    .child(currentUser.getSec())
                    .child("subjects")
                    .child(subjectModel.getSubjectName());
            subjectRef.setValue(subjectModel).addOnCompleteListener(task-> {
                if(task.isSuccessful()) {
                    // subject created
                    super.onBackPressed();
                } else {
                    // subject failed to create
                    Toast.makeText(getApplicationContext(), "Subject creation failed", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}