package com.pkyr.brainace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.pkyr.brainace.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerRegisterBtn.setOnClickListener(v -> {
            binding.registerEmailLayout.setVisibility(View.GONE);
            binding.registerEducationalLayout.setVisibility(View.VISIBLE);
        });
    }
}