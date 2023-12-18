package com.akshar.videochat.fregments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.akshar.videochat.R;
import com.akshar.videochat.VideoCallActivity;
import com.akshar.videochat.utils.FirebaseAuthUtil;
import com.akshar.videochat.utils.Jitsiutils;

import java.util.Random;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private Jitsiutils jitsiutils;
    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Instance of FirebaseAuthUtil and Jitsiutils
        FirebaseAuthUtil firebaseAuthUtil = new FirebaseAuthUtil(requireActivity());
        jitsiutils = new Jitsiutils(requireContext(), firebaseAuthUtil);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageButton joinMeeting = view.findViewById(R.id.joinMeetingButton);
        ImageButton newMeeting = view.findViewById(R.id.newMeetingButton);

        Random random = new Random();
        String roomName = "Meeting" + random.nextInt(1000);

        newMeeting.setOnClickListener(v -> {
            // Start a new meeting when the card is clicked
            jitsiutils.createMeeting(roomName, false, true, true);
        });

        joinMeeting.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), VideoCallActivity.class);
            startActivity(intent);
        });
        return view;
    }
}
