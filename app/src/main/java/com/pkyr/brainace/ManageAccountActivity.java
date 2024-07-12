package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        } else if(menuItem.getItemId() == R.id.actionLogout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
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