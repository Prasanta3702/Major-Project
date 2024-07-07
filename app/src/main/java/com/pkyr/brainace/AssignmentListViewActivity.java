package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

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

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));
    }

    @Override
    public void onResume() {
        super.onResume();

        // load all assignments
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
                        assignmentList.clear();
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

    public void deleteOrEditAssignment(String assignmentName) {

        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_assignment_delete, null, false);
        dialog.setContentView(view);

        LinearLayout deleteBtn = view.findViewById(R.id.delete_btn);
        LinearLayout editBtn = view.findViewById(R.id.edit_btn);

        deleteBtn.setOnClickListener(v -> {
            dialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Ok", ((dialogInterface, i) -> {
                        builder.create().dismiss();
                        delete(assignmentName);
                    }))
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                        builder.create().dismiss();
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        editBtn.setOnClickListener(v -> {
            dialog.dismiss();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alert");
            builder.setIcon(R.drawable.icon_alert);
            builder.setMessage("You can change only the Assignment last date and question.");
            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                // write the code
            });

            AlertDialog editAlertDialog = builder.create();
            editAlertDialog.show();
        });

        dialog.show();
    }

    private void delete(String assignmentName) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Deleting...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        DatabaseReference assignmentRef = databaseReference.child("bwu")
                .child(MainActivity.userModel.getCourse())
                .child(MainActivity.userModel.getBatch())
                .child(MainActivity.userModel.getSem())
                .child(MainActivity.userModel.getSec())
                .child("subjects")
                .child(subjectName)
                .child("assignments")
                .child(assignmentName);

        StorageReference assignmentPDFRef = storageReference.child("bwu")
                .child(MainActivity.userModel.getCourse())
                .child(MainActivity.userModel.getBatch())
                .child(MainActivity.userModel.getSem())
                .child(MainActivity.userModel.getSec())
                .child("subjects")
                .child(subjectName)
                .child("assignments")
                .child(assignmentName);

        assignmentPDFRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                assignmentRef.removeValue().addOnSuccessListener(avoid -> {
                    onResume();
                    progressDialog.dismiss();
                }).addOnFailureListener(error -> {
                    SystemUtils.showToast(getApplicationContext(), "Failed");
                    progressDialog.dismiss();
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assignmentRef.removeValue().addOnSuccessListener(avoid -> {
                    onResume();
                    progressDialog.dismiss();
                }).addOnFailureListener(error -> {
                    SystemUtils.showToast(getApplicationContext(), "Failed");
                    progressDialog.dismiss();
                });
            }
        });

    }
}