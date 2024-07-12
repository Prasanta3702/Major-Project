package com.pkyr.brainace;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.pkyr.brainace.databinding.ActivityMainBinding;
import com.pkyr.brainace.model.UserModel;
import com.pkyr.brainace.utils.FirebaseUtils;
import com.pkyr.brainace.utils.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ImageSlider imageSlider;
    private boolean loadStatus = false;

    public static UserModel userModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = getWindow();
        window.setBackgroundDrawableResource(R.drawable.background_top);
        window.setStatusBarColor(getResources().getColor(android.R.color.transparent));

        /*
            Image slider
         */
        imageSlider = findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.brainware_image, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image1, ScaleTypes.CENTER_CROP));
        slideModels.add(new SlideModel(R.drawable.image2, ScaleTypes.CENTER_CROP));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        // account manage btn
        binding.manageAccountBtn.setOnClickListener(v -> startActivity(new Intent(this, ManageAccountActivity.class)));

        // search listener
        binding.searchViewHome.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchViewActivity.class));
        });

        // all menu listener
        binding.menuDeptNotice.setOnClickListener(v -> {
            if(loadStatus) {
                startActivity(new Intent(this, NoticeViewActivity.class));
            } else {
                loadCurrentUserDetails();
                startActivity(new Intent(this, NoticeViewActivity.class));
            }
        });

        binding.menuSubjectAssignment.setOnClickListener(v -> {
            if(loadStatus) {
                startActivity(new Intent(this, AssignmentViewActivity.class));
            } else {
                loadCurrentUserDetails();
                startActivity(new Intent(this, AssignmentViewActivity.class));
            }
        });

        binding.menuStudyMaterial.setOnClickListener(v -> {
            if(loadStatus) {
                startActivity(new Intent(this, StudyMaterialActivity.class));
            } else {
                loadCurrentUserDetails();
                startActivity(new Intent(this, StudyMaterialActivity.class));

            }
        });

        binding.menuTextQuiz.setOnClickListener(v -> {});

        binding.menuSubjectMarks.setOnClickListener(v -> {});
        binding.menuUploadedAssignment.setOnClickListener(v -> {});
        binding.menuMarksEvaluation.setOnClickListener(v -> {});
        binding.menuEvents.setOnClickListener(v -> {});

        binding.menuFeedback.setOnClickListener(v -> {});
        binding.menuCgpaSgpa.setOnClickListener(v -> {});
        binding.menuJobs.setOnClickListener(v -> {});
        binding.menuSyllabus.setOnClickListener(v -> {});

        binding.menuCProgram.setOnClickListener(v -> {});
        binding.menuJavaProgram.setOnClickListener(v -> {});
        binding.menuPythonProgram.setOnClickListener(v -> {});
        binding.menuSeeAllProgram.setOnClickListener(v -> {});
    }

    @Override
    public void onStart() {
        super.onStart();
        // check network info
        if(!NetworkUtils.isNetworkActive(this.getApplicationContext())) {
            Snackbar.make(binding.searchViewHome, "No internet connection", Snackbar.LENGTH_LONG).show();
        } else {
            loadCurrentUserDetails();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void loadCurrentUserDetails() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FirebaseUtils.currentUserDetails().get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            userModel = task.getResult().toObject(UserModel.class);
                            if(userModel != null) {
                                loadStatus = true;
                                // set title as the name
                                binding.title2.setText(userModel.getName());
                            } else {
                                Toast.makeText(getApplicationContext(), "User not exist", Toast.LENGTH_SHORT).show();
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}