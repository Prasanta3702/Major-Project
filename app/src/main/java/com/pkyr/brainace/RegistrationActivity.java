package com.pkyr.brainace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.pkyr.brainace.databinding.ActivityRegistrationBinding;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;

    String email = "";
    String password = "";
    String department = "";
    String batch = "";
    String semester = "";
    String sec = "";

    private ArrayAdapter<String> deptAdapter;
    private String[] deptItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // department spinner
        deptItems = new String[] {"BCA", "MCA", "B.Tech", "M.Tech"};
        deptAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, deptItems);
        binding.registerDeptSpinner.setAdapter(deptAdapter);


        binding.registerSaveBtn.setOnClickListener(v -> {

            email = binding.registerEmail.getText().toString();

            // check the two password is match or not
            String password1 = binding.registerPassword1.getText().toString();
            String password2 = binding.registerPassword2.getText().toString();

            if(password1.equals(password2)) {
                password = password1;
            } else {
                binding.registerPassword2.setError("Password not match");
            }
        });
    }
}