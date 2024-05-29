package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.pkyr.brainace.adapters.AssignmentAdapter;
import com.pkyr.brainace.databinding.ActivityAssignmentListViewBinding;

public class AssignmentListViewActivity extends AppCompatActivity {

    private ActivityAssignmentListViewBinding binding;
    private AssignmentAdapter assignmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentListViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        assignmentAdapter = new AssignmentAdapter();
        recyclerView.setAdapter(assignmentAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }
}