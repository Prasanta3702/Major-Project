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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkyr.brainace.adapters.NoticeViewAdapter;
import com.pkyr.brainace.databinding.ActivityNoticeViewBinding;
import com.pkyr.brainace.model.NoticeModel;

import java.util.ArrayList;

public class NoticeViewActivity extends AppCompatActivity {

    ActivityNoticeViewBinding binding;
    NoticeViewAdapter noticeViewAdapter;
    private ArrayList<NoticeModel> noticeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            this.setSupportActionBar(binding.toolbar);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        noticeList = new ArrayList<>();
        noticeViewAdapter = new NoticeViewAdapter(this, noticeList);
        recyclerView.setAdapter(noticeViewAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadNotices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notice_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) { super.onBackPressed(); }
        if(item.getItemId()==R.id.menu_notice_create) {
            Intent intent = new Intent(this, NoticeCreateActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNotices() {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference noticeRef = ref.child("bwu")
                    .child(MainActivity.userModel.getCourse())
                    .child(MainActivity.userModel.getBatch())
                    .child(MainActivity.userModel.getSem())
                    .child(MainActivity.userModel.getSec())
                    .child("notices");
            noticeRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            NoticeModel m = snapshot1.getValue(NoticeModel.class);
                            noticeList.add(m);
                            noticeViewAdapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}