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

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.pkyr.brainace.adapters.AssignmentAdapter;
import com.pkyr.brainace.adapters.SubjectAdapter;
import com.pkyr.brainace.databinding.ActivityAssignmentViewBinding;
import com.pkyr.brainace.model.SubjectModel;
import com.pkyr.brainace.utils.NetworkUtils;

import java.util.ArrayList;

public class AssignmentViewActivity extends AppCompatActivity {

    ActivityAssignmentViewBinding binding;
    private SubjectAdapter subjectAdapter;
    private RecyclerView recyclerView;

    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

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
        
        if(NetworkUtils.isNetworkActive(getApplicationContext())) {
            // database query
            Query query = dbRef.child("bwu")
                    .child(MainActivity.userModel.getCourse())
                    .child(MainActivity.userModel.getBatch())
                    .child(MainActivity.userModel.getSem())
                    .child(MainActivity.userModel.getSec())
                    .child("subjects");

            FirebaseRecyclerOptions<SubjectModel> options = new FirebaseRecyclerOptions.Builder<SubjectModel>()
                    .setQuery(query, SubjectModel.class)
                    .build();

            subjectAdapter = new SubjectAdapter(getApplicationContext(), options);
            recyclerView.setAdapter(subjectAdapter);
            subjectAdapter.startListening();
        }
    }
}