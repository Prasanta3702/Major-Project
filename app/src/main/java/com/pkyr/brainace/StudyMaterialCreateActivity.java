package com.pkyr.brainace;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pkyr.brainace.databinding.ActivityStudyMaterialCreateBinding;
import com.pkyr.brainace.model.MaterialModel;
import com.pkyr.brainace.utils.SystemUtils;

import java.util.UUID;

public class StudyMaterialCreateActivity extends AppCompatActivity {

    private ActivityStudyMaterialCreateBinding binding;
    private Uri pdfUri = null;

    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudyMaterialCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        ActivityResultLauncher<String> pdfLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if(uri != null) {
                        pdfUri = uri;
                        binding.documentNameView.setText(getFileName(uri));
                    }
                });
        binding.documentBrowseBtn.setOnClickListener(v -> pdfLauncher.launch("application/pdf"));
        binding.uploadBtn.setOnClickListener(v -> uploadMaterial());
    }

    public void uploadMaterial() {
        DatabaseReference materialRef = dbRef.child("bwu")
                .child(MainActivity.userModel.getCourse())
                .child(MainActivity.userModel.getBatch())
                .child(MainActivity.userModel.getSem())
                .child(MainActivity.userModel.getSec())
                .child("materials");

        StorageReference storageRef = storageReference.child("bwu")
                .child(MainActivity.userModel.getCourse())
                .child(MainActivity.userModel.getBatch())
                .child(MainActivity.userModel.getSem())
                .child(MainActivity.userModel.getSec())
                .child("materials");

        String subject = binding.subjectField.getText().toString().trim();
        String desc = binding.descField.getText().toString().trim();
        String date = SystemUtils.getDate();

        if(!subject.isEmpty() && !desc.isEmpty() && pdfUri != null) {
            MaterialModel materialModel = new MaterialModel();
            materialModel.setSubjectName(subject);
            materialModel.setDesc(desc);
            materialModel.setDate(date);

            String key = UUID.randomUUID().toString();
            materialModel.setId(key);

            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            storageRef.child(key).putFile(pdfUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()) {
                        storageRef.child(key).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()) {
                                    materialModel.setDocument(task.getResult().toString());
                                    materialRef.child(key).setValue(materialModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                onBackPressed();
                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Failed to download file", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed to store file", Toast.LENGTH_SHORT).show();
                    }
                }
            });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }
}