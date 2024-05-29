package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.pkyr.brainace.adapters.MaterialsAdapter;
import com.pkyr.brainace.databinding.ActivityStudyMaterialBinding;

import java.util.ArrayList;

public class StudyMaterialActivity extends AppCompatActivity {

    ActivityStudyMaterialBinding binding;
    private MaterialsAdapter materialsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyMaterialBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        materialsAdapter = new MaterialsAdapter();
        recyclerView.setAdapter(materialsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home)
            super.onBackPressed();

        return true;
    }
}