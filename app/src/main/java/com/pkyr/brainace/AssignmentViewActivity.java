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

import com.pkyr.brainace.adapters.AssignmentAdapter;
import com.pkyr.brainace.adapters.SubjectAdapter;
import com.pkyr.brainace.databinding.ActivityAssignmentViewBinding;

public class AssignmentViewActivity extends AppCompatActivity {

    ActivityAssignmentViewBinding binding;
    private SubjectAdapter subjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        subjectAdapter = new SubjectAdapter(this);
        recyclerView.setAdapter(subjectAdapter);
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
        return true;
    }

    public void navigateToAssignments() {
        startActivity(new Intent(this, AssignmentListViewActivity.class));
    }
}