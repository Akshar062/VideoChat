package com.akshar.videochat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshar.videochat.utils.FirebaseAuthUtil;
import com.akshar.videochat.utils.Jitsiutils;
public class VideoCallActivity extends AppCompatActivity {
    private ImageView micBtn, videoBtn;
    boolean isMuted = false;
    boolean isVideo = false;
    private EditText etRoomName , etUserName;
    String roomName, userName;
    private FirebaseAuthUtil firebaseAuthUtil;
    private Jitsiutils jitsiutils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        firebaseAuthUtil = new FirebaseAuthUtil(this);
        jitsiutils = new Jitsiutils(this);

        micBtn = findViewById(R.id.ivMic);
        videoBtn = findViewById(R.id.ivVideo);

        etRoomName = findViewById(R.id.etMeetingCode);
        etUserName = findViewById(R.id.etName);

        TextView joinBtn = findViewById(R.id.tvJoin);

        roomName = etRoomName.getText().toString();
        userName = etUserName.getText().toString();

        etUserName.setText(firebaseAuthUtil.getCurrentUser().getDisplayName());

        micBtn.setOnClickListener(v -> {
            if (isMuted) {
                isMuted = false;
                micBtn.setImageResource(R.drawable.mic_off_24px);
            } else {
                isMuted = true;
                micBtn.setImageResource(R.drawable.keyboard_voice_24px);
            }
        });

        videoBtn.setOnClickListener(v -> {
            if (isVideo) {
                isVideo = false;
                videoBtn.setImageResource(R.drawable.videocam_off_24px);
            } else {
                isVideo = true;
                videoBtn.setImageResource(R.drawable.videocam_24px);
            }
        });


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roomName = etRoomName.getText().toString();
                userName = etUserName.getText().toString();
                if (roomName.isEmpty()) {
                    etRoomName.setError("Please enter room name");
                    etRoomName.requestFocus();
                    return;
                }
                if (userName.isEmpty()) {
                    userName = firebaseAuthUtil.getCurrentUser().getDisplayName();
                } else {
                    userName = etUserName.getText().toString();
                }
                joinRoom();

            }
        });
    }

    public void joinRoom() {
        jitsiutils.createMeeting(roomName,userName,false, false, false);
    }
}