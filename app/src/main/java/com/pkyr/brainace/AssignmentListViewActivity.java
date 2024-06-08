package com.pkyr.brainace;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkyr.brainace.adapters.AssignmentAdapter;
import com.pkyr.brainace.databinding.ActivityAssignmentListViewBinding;
import com.pkyr.brainace.model.AssignmentModel;
import com.pkyr.brainace.utils.SystemUtils;

import java.net.URI;
import java.util.ArrayList;

public class AssignmentListViewActivity extends AppCompatActivity {

    private ActivityAssignmentListViewBinding binding;
    private AssignmentAdapter assignmentAdapter;
    private RecyclerView recyclerView;
    private ArrayList<AssignmentModel> assignmentList;

    private String subjectName;
    private String subjectTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentListViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        subjectName = getIntent().getStringExtra("subject_name");
        subjectTeacher = getIntent().getStringExtra("subject_teacher");

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(subjectName);
        }

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

    }

    @Override
    public void onResume() {
        super.onResume();

        assignmentList = new ArrayList<>();
        loadAssignment();
        assignmentAdapter = new AssignmentAdapter(this, assignmentList);
        recyclerView.setAdapter(assignmentAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assignment_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        if(menuItem.getItemId() == R.id.menu_assignment_create) {
            Intent intent = new Intent(this, AssignmentCreateActivity.class);
            intent.putExtra("subject_name", subjectName);
            intent.putExtra("subject_teacher", subjectTeacher);
            startActivity(intent);
        }
        return true;
    }

    private void loadAssignment() {
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
                    .child("assignments");

            assignmentRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            AssignmentModel assignmentModel = snapshot1.getValue(AssignmentModel.class);
                            assignmentList.add(assignmentModel);
                            assignmentAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Couldn't load assignments", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}