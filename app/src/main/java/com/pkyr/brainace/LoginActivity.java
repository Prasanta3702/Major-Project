package com.pkyr.brainace;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pkyr.brainace.databinding.ActivityLoginBinding;
import com.pkyr.brainace.utils.FirebaseUtils;

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
            login(binding.loginEmail.getText().toString(), binding.loginPassword.getText().toString());
        });

        // registration
        binding.loginRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistrationActivity.class));
        });
    }

    private void login(String email, String password) {
        try {
            if(email.isEmpty()) {
                binding.loginEmail.setError("Email required");
            }
            else if(password.isEmpty()) {
                binding.loginPassword.setError("Password required");
            } else {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                // navigate the main page
                                startActivity(new Intent(this, MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Incorrect username & password", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}