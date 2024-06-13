package com.pkyr.brainace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pkyr.brainace.R;
import com.pkyr.brainace.model.NoticeModel;

import java.util.ArrayList;

public class NoticeViewAdapter extends RecyclerView.Adapter<NoticeViewAdapter.ItemViewHolder> {
    private Context context;
    private ArrayList<NoticeModel> noticeList;

    public NoticeViewAdapter(Context context, ArrayList<NoticeModel> noticeList) {
        this.context = context;
        this.noticeList = noticeList;
    }

    @NonNull
    @Override
    public NoticeViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.model_notice, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewAdapter.ItemViewHolder holder, int position) {
        NoticeModel noticeModel = noticeList.get(position);
        holder.senderView.setText(noticeModel.getNotice_sender());
        holder.msgView.setText(noticeModel.getNotice_message());
        holder.dateView.setText(noticeModel.getNotice_date());
        holder.timeStampView.setText(noticeModel.getNotice_timestamp());
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView senderView, msgView, dateView, timeStampView;

        public ItemViewHolder(@NonNull View view) {
            super(view);

            senderView = view.findViewById(R.id.model_notice_sender);
            msgView = view.findViewById(R.id.model_notice_msg);
            dateView = view.findViewById(R.id.model_notice_date);
            timeStampView = view.findViewById(R.id.model_notice_time_stamp);
        }
    }

}