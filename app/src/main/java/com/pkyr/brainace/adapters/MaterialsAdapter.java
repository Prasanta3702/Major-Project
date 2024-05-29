package com.pkyr.brainace.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkyr.brainace.R;
import com.pkyr.brainace.StudyMaterialActivity;

import java.util.ArrayList;

public class MaterialsAdapter extends RecyclerView.Adapter<MaterialsAdapter.ItemViewHolder> {

    private ArrayList<?> itemList;
    private StudyMaterialActivity activity;

    public MaterialsAdapter() {

    }
    public MaterialsAdapter(StudyMaterialActivity activity, ArrayList<?> itemList) {
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

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ItemViewHolder(@NonNull View view) {
            super(view);
        }
    }
}
