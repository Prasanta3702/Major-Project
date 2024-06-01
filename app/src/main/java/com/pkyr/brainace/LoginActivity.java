package com.pkyr.brainace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


        // just by pass the login
        binding.loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    private void login(String email, String password) throws Exception {
        // write the login code here
    }
}