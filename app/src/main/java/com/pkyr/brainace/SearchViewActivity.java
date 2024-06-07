package com.pkyr.brainace;

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

import com.pkyr.brainace.adapters.AssignmentAdapter;
import com.pkyr.brainace.databinding.ActivitySearchViewBinding;
import com.pkyr.brainace.model.AssignmentModel;

import java.util.ArrayList;

public class SearchViewActivity extends AppCompatActivity {

    private ActivitySearchViewBinding binding;
    private AssignmentAdapter adapter;

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


        RecyclerView recyclerView = binding.searchActivityRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayout.VERTICAL));

        adapter = new AssignmentAdapter(getApplicationContext(), new ArrayList<AssignmentModel>());
        recyclerView.setAdapter(adapter);

    }
}