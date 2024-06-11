package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkyr.brainace.adapters.AssignmentAdapter;
import com.pkyr.brainace.databinding.ActivitySearchViewBinding;
import com.pkyr.brainace.model.AssignmentModel;
import com.pkyr.brainace.model.SubjectModel;
import com.pkyr.brainace.utils.FirebaseUtils;

import java.util.ArrayList;

public class SearchViewActivity extends AppCompatActivity {

    private ActivitySearchViewBinding binding;
    private AssignmentAdapter adapter;
    private ArrayList<AssignmentModel> assignmentList;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.searchActivityBackBtn.setOnClickListener(v -> super.onBackPressed());

        // request focus on search view
        binding.searchActivitySearchView.requestFocus();
        // Show the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.searchActivitySearchView, InputMethodManager.SHOW_IMPLICIT);


        recyclerView = binding.searchActivityRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        assignmentList = new ArrayList<>();
        adapter = new AssignmentAdapter(this, assignmentList);
    }
    @Override
    public void onResume() {
        super.onResume();

        loadAllAssignments(assignmentList);
        recyclerView.setAdapter(adapter);
    }


    private void loadAllAssignments(ArrayList<AssignmentModel> assignmentList) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        DatabaseReference assignmentRef = dbRef.child("bwu")
                .child(MainActivity.userModel.getCourse())
                .child(MainActivity.userModel.getBatch())
                .child(MainActivity.userModel.getSem())
                .child(MainActivity.userModel.getSec())
                .child("subjects");
        // get the all subjects and find the assignments
        assignmentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        SubjectModel subjectModel = snapshot1.getValue(SubjectModel.class);
                        loadAssignmentInSubject(subjectModel.getSubjectName(), assignmentList);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAssignmentInSubject(String subjectName, ArrayList<AssignmentModel> assignmentList) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference();

        DatabaseReference aRef = dbRef.child("bwu")
                .child(MainActivity.userModel.getCourse())
                .child(MainActivity.userModel.getBatch())
                .child(MainActivity.userModel.getSem())
                .child(MainActivity.userModel.getSec())
                .child("subjects")
                .child(subjectName)
                .child("assignments");

        aRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                        AssignmentModel a = snapshot1.getValue(AssignmentModel.class);
                        assignmentList.add(a);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    };
}