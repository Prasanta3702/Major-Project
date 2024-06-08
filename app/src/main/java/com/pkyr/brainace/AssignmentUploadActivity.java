package com.pkyr.brainace;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pkyr.brainace.databinding.ActivityAssignmentUploadBinding;
import com.pkyr.brainace.model.AssignmentModel;
import com.pkyr.brainace.model.UploadAssignmentModel;
import com.pkyr.brainace.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

public class AssignmentUploadActivity extends AppCompatActivity {

    private String assignmentName;
    private String assignmentTeacher;
    private String assignmentDate;
    private String assignmentLastDate;
    private String assignmentSubject;

    private ActivityAssignmentUploadBinding binding;
    private ActivityResultLauncher<String> pdfLauncher;
    private Uri pdfUri = null;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAssignmentUploadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        assignmentName = getIntent().getStringExtra("assignment_name");
        assignmentTeacher = getIntent().getStringExtra("assignment_teacher");
        assignmentDate = getIntent().getStringExtra("assignment_date");
        assignmentLastDate = getIntent().getStringExtra("assignment_last_date");
        assignmentSubject = getIntent().getStringExtra("assignment_subject");

        binding.assignmentName.setText(assignmentName);
        binding.subjectName.setText(assignmentSubject);
        binding.assignmentDate.setText(SystemUtils.getDate());

        pdfLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), task-> {
            if(task != null) {
                pdfUri = task;
                String fileName = getFileName(pdfUri);
                binding.filename.setText(fileName);
                binding.uploadBtn.setEnabled(true);
            }
        });

        binding.assignmentAnswerChoose.setOnClickListener(v -> {
            pdfLauncher.launch("application/pdf");
        });

        binding.uploadBtn.setOnClickListener(v-> {
            binding.uploadBtn.setVisibility(View.GONE);
            binding.progressbar.setVisibility(View.VISIBLE);

            uploadAssignment();
        });
    }

    private void uploadAssignment() {
        try {
            String studentName = MainActivity.userModel.getName();
            String code = MainActivity.userModel.getCode();
            // replace the code xxx/xxx/xxx/xxx to xxx_xxx_xxx_xxx
            String studentCode = code.replaceAll("/", "_");

            String subject = assignmentSubject;
            String name = assignmentName;
            String date = SystemUtils.getDate();
            String answer = pdfUri.toString();

            UploadAssignmentModel a = new UploadAssignmentModel();
            a.setStudentName(studentName);
            a.setStudentCode(studentCode);
            a.setAssignmentSubject(subject);
            a.setAssignmentName(name);
            a.setAssignmentDate(date);

            StorageReference storageRef = storageReference.child("bwu")
                    .child(MainActivity.userModel.getCourse())
                    .child(MainActivity.userModel.getBatch())
                    .child(MainActivity.userModel.getSem())
                    .child(MainActivity.userModel.getSec())
                    .child("subjects")
                    .child(subject)
                    .child("uploaded_assignments")
                    .child(studentCode)
                    .child(assignmentName);

            storageRef.putFile(pdfUri).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    storageRef.getDownloadUrl().addOnCompleteListener(task1-> {
                        if(task1.isSuccessful()) {

                            // set the pdf url to assignment
                            a.setAssignmentAnswer(task1.toString());

                            // now store on database
                            DatabaseReference assignmentRef = databaseReference.child("bwu")
                                    .child(MainActivity.userModel.getCourse())
                                    .child(MainActivity.userModel.getBatch())
                                    .child(MainActivity.userModel.getSem())
                                    .child(MainActivity.userModel.getSec())
                                    .child("subjects")
                                    .child(subject)
                                    .child("uploaded_assignments")
                                    .child(studentCode)
                                    .child(assignmentName);

                            assignmentRef.setValue(a).addOnCompleteListener(task2-> {
                                if(task2.isSuccessful()) {

                                    binding.progressbar.setVisibility(View.GONE);
                                    binding.uploadBtn.setVisibility(View.VISIBLE);
                                    AssignmentUploadActivity.this.onBackPressed();
                                }
                                else {
                                    binding.progressbar.setVisibility(View.GONE);
                                    binding.uploadBtn.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "Couldn't save data", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else {
                            binding.progressbar.setVisibility(View.GONE);
                            binding.uploadBtn.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), "Couldn't download file", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    binding.progressbar.setVisibility(View.GONE);
                    binding.uploadBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Couldn't store file", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            binding.progressbar.setVisibility(View.GONE);
            binding.uploadBtn.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}