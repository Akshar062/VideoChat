package com.akshar.videochat.fregments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.akshar.videochat.R;
import com.akshar.videochat.adapters.HistoryAdapter;
import com.akshar.videochat.models.Meeting;
import com.akshar.videochat.utils.FirebaseAuthUtil;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class HistoryFragment extends Fragment {

    private View view;

    private HistoryAdapter historyAdapter;

    private RecyclerView rvHistory;
    private ProgressBar pbHistory;

    private FirebaseAuthUtil firebaseAuthUtil;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        rvHistory = view.findViewById(R.id.rvHistory);
        pbHistory = view.findViewById(R.id.progressBar);

        firebaseAuthUtil = new FirebaseAuthUtil(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvHistory.setLayoutManager(linearLayoutManager);
        fetchMeetingHistory();
        return view;
    }

    private void fetchMeetingHistory() {
        firebaseAuthUtil.getMeetingHistory(task -> {
            if (task.isSuccessful()) {
                pbHistory.setVisibility(View.GONE);
                List<Meeting> meetingList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Meeting meeting = document.toObject(Meeting.class);
                    meetingList.add(meeting);
                }
                historyAdapter = new HistoryAdapter(meetingList);
                rvHistory.setAdapter(historyAdapter);
            }  else {
            pbHistory.setVisibility(View.VISIBLE);
                Timber.tag("HistoryFragment").e(task.getException(), "Error fetching meeting history");
        }
    });
    }
}