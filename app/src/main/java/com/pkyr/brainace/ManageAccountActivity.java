package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.pkyr.brainace.databinding.ActivityManageAccountBinding;
import com.pkyr.brainace.model.UserModel;
import com.pkyr.brainace.utils.FirebaseUtils;

public class ManageAccountActivity extends AppCompatActivity {

    ActivityManageAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserDetails();
    }

    public void loadUserDetails() {
        try {
            FirebaseUtils.currentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserModel userModel = task.getResult().toObject(UserModel.class);

                    binding.profileName.setText(userModel.getName());
                    binding.profileEmail.setText(userModel.getEmail());
                    binding.profileCode.setText(userModel.getCode());
                    binding.profileCourse.setText(userModel.getCourse());
                    binding.profileBatch.setText(userModel.getBatch());
                    binding.profileSemester.setText(userModel.getSem());
                    binding.profileSec.setText(userModel.getSec());
                }
            });
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}