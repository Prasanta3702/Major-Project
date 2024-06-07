package com.pkyr.brainace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.firestore.auth.User;
import com.pkyr.brainace.databinding.ActivityRegistrationBinding;
import com.pkyr.brainace.model.UserModel;
import com.pkyr.brainace.utils.FirebaseUtils;

public class RegistrationActivity extends AppCompatActivity {

    ActivityRegistrationBinding binding;

    String name = "";
    String code = "";
    String email = "";
    String password = "";
    String department = "";
    String batch = "";
    String semester = "";
    String sec = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerDeptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                department = binding.registerDeptSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.registerBatchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                batch = binding.registerBatchSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.registerSemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                semester = binding.registerSemSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.registerSecSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sec = binding.registerSecSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.registerSaveBtn.setOnClickListener(v -> {

            name = binding.registerName.getText().toString();
            code = binding.registerCode.getText().toString();
            code = code.toLowerCase();
            email = binding.registerEmail.getText().toString();

            // check the two password is match or not
            String password1 = binding.registerPassword1.getText().toString();
            String password2 = binding.registerPassword2.getText().toString();
            if(password1.equals(password2)) {
                password = password1;
            } else {
                binding.registerPassword2.setError("Password not match");
            }


            if(!name.isEmpty() && !code.isEmpty() && !email.isEmpty() && !password.isEmpty() && !department.isEmpty() && !batch.isEmpty() && !semester.isEmpty()
            && !sec.isEmpty()) {

                UserModel userModel = new UserModel();
                userModel.setId(FirebaseUtils.currentUserId());
                userModel.setName(name);
                userModel.setCode(code);
                userModel.setEmail(email);
                userModel.setCourse(department);
                userModel.setBatch(batch);
                userModel.setSem(semester);
                userModel.setSec(sec);

                FirebaseUtils.createUser(getApplicationContext(), userModel);

            } else {
                Toast.makeText(getApplicationContext(), "Filed empty", Toast.LENGTH_LONG).show();
            }

        });
    }
}