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

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkyr.brainace.adapters.MaterialsAdapter;
import com.pkyr.brainace.databinding.ActivityStudyMaterialBinding;
import com.pkyr.brainace.model.MaterialModel;

import java.util.ArrayList;

public class StudyMaterialActivity extends AppCompatActivity {

    ActivityStudyMaterialBinding binding;
    private MaterialsAdapter materialsAdapter;
    private ArrayList<MaterialModel> materialList;
    private ShimmerFrameLayout shimmerFrameLayout;

    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyMaterialBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef = FirebaseDatabase.getInstance().getReference();
        shimmerFrameLayout = binding.shimmer;

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        materialList = new ArrayList<>();
        materialsAdapter = new MaterialsAdapter(this, materialList);
        recyclerView.setAdapter(materialsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            loadMaterials();
        } catch (Exception e) {
            e.printStackTrace();
        }
        materialsAdapter.notifyDataSetChanged();
    }

    public void loadMaterials() throws Exception {
        DatabaseReference materialRef = dbRef.child("bwu")
                .child(MainActivity.userModel.getCourse())
                .child(MainActivity.userModel.getBatch())
                .child(MainActivity.userModel.getSem())
                .child(MainActivity.userModel.getSec())
                .child("materials");

        materialRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    materialList.clear();
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        MaterialModel model = snapshot1.getValue(MaterialModel.class);
                        materialList.add(model);
                    }
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_material, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home)
            super.onBackPressed();

        if(menuItem.getItemId() == R.id.menu_material_create) {
            Intent intent = new Intent(this, StudyMaterialCreateActivity.class);
            startActivity(intent);
        }

        return true;
    }
}