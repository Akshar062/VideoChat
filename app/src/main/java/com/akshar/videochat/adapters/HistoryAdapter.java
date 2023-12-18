package com.akshar.videochat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.videochat.R;
import com.akshar.videochat.models.Meeting;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {

    private List<Meeting> meetingList;

    public HistoryAdapter(List<Meeting> meetingList) {
        this.meetingList = meetingList;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        Meeting meeting = meetingList.get(position);
        holder.tvMeetingCode.setText(meeting.getMeetingName());
        // Format and set createdAt
        holder.tvMeetingDate.setText(meeting.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return meetingList.size();
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvMeetingCode;
        TextView tvMeetingDate;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMeetingCode = itemView.findViewById(R.id.tvMeetingCode);
            tvMeetingDate = itemView.findViewById(R.id.tvMeetingDate);
        }
    }
}