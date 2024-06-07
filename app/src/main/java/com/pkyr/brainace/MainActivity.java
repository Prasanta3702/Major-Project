package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSeekBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.firebase.auth.FirebaseAuth;
import com.pkyr.brainace.databinding.ActivityMainBinding;
import com.pkyr.brainace.model.UserModel;
import com.pkyr.brainace.utils.FirebaseUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ImageSlider imageSlider;
    private AppCompatSeekBar seekBarAttendance;

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

        // disable seekbar


        // search listener
        binding.searchViewHome.setOnClickListener(v -> {
            startActivity(new Intent(this, SearchViewActivity.class));
        });

        // all menu listener
        binding.menuDeptNotice.setOnClickListener(v -> { startActivity(new Intent(this, NoticeViewActivity.class));});
        binding.menuSubjectAssignment.setOnClickListener(v -> { startActivity(new Intent(this, AssignmentViewActivity.class));});
        binding.menuStudyMaterial.setOnClickListener(v -> startActivity(new Intent(this, StudyMaterialActivity.class)));
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

        loadCurrentUserDetails();
    }
    private void loadCurrentUserDetails() {
        try {
            FirebaseUtils.currentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserModel userModel = task.getResult().toObject(UserModel.class);
                    if(userModel.getId() != null)
                        Toast.makeText(getApplicationContext(), userModel.getId(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), userModel.getName(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), userModel.getEmail(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), userModel.getCode(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), userModel.getCourse(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), userModel.getBatch(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), userModel.getSem(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), userModel.getSec(), Toast.LENGTH_SHORT).show();

                }
            });
        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}