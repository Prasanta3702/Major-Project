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
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pkyr.brainace.adapters.NoticeViewAdapter;
import com.pkyr.brainace.databinding.ActivityNoticeViewBinding;
import com.pkyr.brainace.model.NoticeModel;
import com.pkyr.brainace.utils.NetworkUtils;

import java.util.ArrayList;

public class NoticeViewActivity extends AppCompatActivity {

    ActivityNoticeViewBinding binding;
    NoticeViewAdapter noticeViewAdapter;
    private ArrayList<NoticeModel> noticeList;

    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            this.setSupportActionBar(binding.toolbar);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        shimmerFrameLayout = binding.shimmer;

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));

        noticeList = new ArrayList<>();
        noticeViewAdapter = new NoticeViewAdapter(this, noticeList);
        recyclerView.setAdapter(noticeViewAdapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<NoticeModel> searchList = new ArrayList<>();
                for(NoticeModel notice : noticeList) {
                    if(notice.getNotice_sender().toLowerCase().contains(s.toLowerCase())
                    || notice.getNotice_message().toLowerCase().contains(s.toLowerCase())) {
                        searchList.add(notice);
                    }
                }
                noticeViewAdapter.showSearchList(searchList);
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!NetworkUtils.isNetworkActive(getApplicationContext())) {
            shimmerFrameLayout.startShimmer();
        } else {
            loadNotices();
        }
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
                    noticeList.clear();
                    if(snapshot.exists()) {
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                            NoticeModel m = snapshot1.getValue(NoticeModel.class);
                            noticeList.add(m);
                            noticeViewAdapter.notifyDataSetChanged();
                        }

                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        binding.recyclerView.setVisibility(View.VISIBLE);
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