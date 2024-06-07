package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkyr.brainace.adapters.AssignmentAdapter;
import com.pkyr.brainace.adapters.SubjectAdapter;
import com.pkyr.brainace.databinding.ActivityAssignmentViewBinding;
import com.pkyr.brainace.model.SubjectModel;

import java.util.ArrayList;

public class AssignmentViewActivity extends AppCompatActivity {

    ActivityAssignmentViewBinding binding;
    private SubjectAdapter subjectAdapter;
    ArrayList<SubjectModel> subjectList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_subject_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }

        if(menuItem.getItemId() == R.id.menu_subject_create) {
            startActivity(new Intent(this, SubjectCreateActivity.class));
        }

        if(menuItem.getItemId() == R.id.menu_subject_delete) {

        }

        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        subjectList = new ArrayList<>();
        loadSubjects();

        subjectAdapter = new SubjectAdapter(this, subjectList);
        recyclerView.setAdapter(subjectAdapter);
    }

    public void loadSubjects() {
        try {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference();
            DatabaseReference subjectRef = databaseReference.child("bwu")
                    .child(MainActivity.userModel.getCourse())
                    .child(MainActivity.userModel.getBatch())
                    .child(MainActivity.userModel.getSem())
                    .child(MainActivity.userModel.getSec())
                    .child("subjects");
            subjectRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            SubjectModel subjectModel = snapshot1.getValue(SubjectModel.class);
                            subjectList.add(subjectModel);
                            subjectAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Couldn't load subject", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void navigateToAssignments(String subjectName, String teacher) {
        Intent intent = new Intent(this, AssignmentListViewActivity.class);
        intent.putExtra("subject_name", subjectName);
        intent.putExtra("subject_teacher", teacher);
        startActivity(intent);
    }


}