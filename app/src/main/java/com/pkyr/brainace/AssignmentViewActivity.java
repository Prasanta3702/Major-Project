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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
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
    private ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<SubjectModel> subjectList;

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

        shimmerFrameLayout = binding.shimmer;

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        subjectList = new ArrayList<>();
        subjectAdapter = new SubjectAdapter(getApplicationContext(), subjectList);
        recyclerView.setAdapter(subjectAdapter);


        // search view
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<SubjectModel> searchList = new ArrayList<>();
                for(SubjectModel subjectModel : subjectList) {
                    if(subjectModel.getSubjectName().toLowerCase().contains(s.toLowerCase())) {
                        searchList.add(subjectModel);
                    }
                }
                subjectAdapter.showSearchList(searchList);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(NetworkUtils.isNetworkActive(getApplicationContext())) {
            try {
                loadSubjects();
                subjectAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                onResume();
            }
        }
    }

    public void loadSubjects() throws Exception {

        DatabaseReference subjectRef = dbRef.child("bwu")
                .child(MainActivity.userModel.getCourse())
                .child(MainActivity.userModel.getBatch())
                .child(MainActivity.userModel.getSem())
                .child(MainActivity.userModel.getSec())
                .child("subjects");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                subjectRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        subjectList.clear();
                        if(snapshot.exists()) {
                            for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                                SubjectModel subjectModel = snapshot1.getValue(SubjectModel.class);
                                subjectList.add(subjectModel);
                            }

                            shimmerFrameLayout.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        thread.start();
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
}