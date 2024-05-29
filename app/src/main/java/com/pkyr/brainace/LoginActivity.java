package com.pkyr.brainace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.pkyr.brainace.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        getWindow().setBackgroundDrawableResource(R.drawable.background_login);
        getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));


    }

    private void login(String email, String password) throws Exception {

    }
}