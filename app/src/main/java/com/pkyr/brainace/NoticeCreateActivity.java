package com.pkyr.brainace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pkyr.brainace.databinding.ActivityNoticeCreateBinding;
import com.pkyr.brainace.model.NoticeModel;
import com.pkyr.brainace.utils.SystemUtils;

import java.util.Objects;
import java.util.UUID;

public class NoticeCreateActivity extends AppCompatActivity {

    ActivityNoticeCreateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(getSupportActionBar() == null) {
            setSupportActionBar(binding.toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.sendBtn.setOnClickListener(v -> {
            String msg = Objects.requireNonNull(binding.msgField.getText()).toString();
            if(!msg.isEmpty()) {
                String date = SystemUtils.getDate();
                String sender = MainActivity.userModel.getName();

                NoticeModel noticeModel = new NoticeModel();
                noticeModel.setNotice_sender(sender);
                noticeModel.setNotice_message(msg);
                noticeModel.setNotice_date(date);
                noticeModel.setNotice_timestamp("5 second ago");
                uploadNotice(noticeModel);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if(menuItem.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return true;
    }

    private void uploadNotice(NoticeModel noticeModel) {
        binding.progressBar.setVisibility(View.VISIBLE);

        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            DatabaseReference noticeRef = ref.child("bwu")
                    .child(MainActivity.userModel.getCourse())
                    .child(MainActivity.userModel.getBatch())
                    .child(MainActivity.userModel.getSem())
                    .child(MainActivity.userModel.getSec())
                    .child("notices")
                    .child(UUID.randomUUID().toString());

            noticeRef.setValue(noticeModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
//                                Toast.makeText(getApplicationContext(), "Notice send", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            } else {
                                binding.progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Notice sending failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        } catch (Exception e) {
            binding.progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}