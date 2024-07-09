package com.pkyr.brainace.adapters;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pkyr.brainace.BuildConfig;
import com.pkyr.brainace.MainActivity;
import com.pkyr.brainace.R;
import com.pkyr.brainace.StudyMaterialActivity;
import com.pkyr.brainace.model.AssignmentModel;
import com.pkyr.brainace.model.MaterialModel;

import java.io.File;
import java.util.ArrayList;

public class MaterialsAdapter extends RecyclerView.Adapter<MaterialsAdapter.ItemViewHolder> {

    private ArrayList<MaterialModel> itemList;
    private StudyMaterialActivity activity;

    public MaterialsAdapter(StudyMaterialActivity activity, ArrayList<MaterialModel> itemList) {
        this.activity = activity;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_materials, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.subjectName.setText(itemList.get(position).getSubjectName());
        if(itemList.get(position).getDesc().length() > 100) {
            holder.desc.setText(itemList.get(position).getDesc().substring(0, 100)+"...");
        } else {
            holder.desc.setText(itemList.get(position).getDesc());
        }
        holder.date.setText(itemList.get(position).getDate());
        holder.uri = itemList.get(position).getDocument();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView subjectName, desc, date;
        String uri = "";

        public ItemViewHolder(@NonNull View view) {
            super(view);

            subjectName = view.findViewById(R.id.model_material_title);
            desc = view.findViewById(R.id.model_material_desc);
            date = view.findViewById(R.id.model_material_date);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            loadMaterialDocs(uri);
        }
    }

    private void loadMaterialDocs(String key) {
        Thread thread = new Thread(() -> {
            try {
                downloadAndOpenPDF(Uri.parse(key), "Materials");
            } catch (Exception e) {
                Toast.makeText(activity.getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        thread.start();
    }

    private void downloadAndOpenPDF(Uri uri, String fileName) throws Exception {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference httpsReference = storage.getReferenceFromUrl(uri.toString());

        final File localFile = new File(activity.getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName+".pdf");

        httpsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Successfully downloaded the file
                // Toast.makeText(context, "File saved", Toast.LENGTH_SHORT).show();
                try {
                    openPDF(localFile);
                } catch (Exception e) {
                    try {
                        throw new Exception(e.getLocalizedMessage());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                Toast.makeText(activity.getApplicationContext(), "Download failed: " + exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openPDF(File file) throws Exception {
        Uri pdfUri = FileProvider.getUriForFile(activity.getApplicationContext(), BuildConfig.APPLICATION_ID + ".fileprovider", file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        Intent chooser = Intent.createChooser(intent, "Open PDF");
        try {
            activity.startActivity(chooser);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(activity.getApplicationContext(), "No application to view PDF", Toast.LENGTH_SHORT).show();
        }
    }
}
