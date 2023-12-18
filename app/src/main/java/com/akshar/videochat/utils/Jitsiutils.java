package com.akshar.videochat.utils;

import android.content.Context;
import android.net.Uri;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;

import java.net.MalformedURLException;
import java.net.URL;

public class Jitsiutils {

    Context context;
    private final FirebaseAuthUtil firebaseAuthUtil;

    public Jitsiutils(Context context ,FirebaseAuthUtil firebaseAuthUtil) {
        this.firebaseAuthUtil = firebaseAuthUtil;
        this.context = context;
    }

    public Jitsiutils(Context context) {
        this.context = context;
        firebaseAuthUtil = null;
    }

    public void createMeeting(String roomName,boolean isAudioOnly, boolean isVideoMuted, boolean isAudioMuted) {
        createMeeting(roomName,null,false,false,false);
    }

    public void createMeeting(String roomName,String name,boolean isAudioOnly, boolean isVideoMuted, boolean isAudioMuted) {

        // Set Jitsi Meet options using the builder pattern
        JitsiMeetConferenceOptions.Builder builder = new JitsiMeetConferenceOptions.Builder()
                .setRoom(roomName)
                .setAudioMuted(isAudioMuted)
                .setVideoMuted(isVideoMuted)
                .setAudioOnly(isAudioOnly);

        // Get user information from FirebaseAuthUtil
        if (firebaseAuthUtil != null) {
            String displayName = firebaseAuthUtil.getCurrentUser().getDisplayName();
            Uri photoUrl = firebaseAuthUtil.getCurrentUser().getPhotoUrl();

            // Set user information in JitsiMeetUserInfo
            JitsiMeetUserInfo userInfo = new JitsiMeetUserInfo();

            if (name != null){
                userInfo.setDisplayName(name);
            } else {
                userInfo.setDisplayName(displayName);
            }

            // Convert Uri to URL
            if (photoUrl != null) {
                try {
                    userInfo.setAvatar(new URL(photoUrl.toString()));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            // Set the user info in the Jitsi Meet options
            builder.setUserInfo(userInfo);
        }
        // Build the JitsiMeetConferenceOptions
        JitsiMeetConferenceOptions options = builder.build();
        // Launch the Jitsi Meet activity with the specified options
        if (firebaseAuthUtil != null){
            firebaseAuthUtil.addMeetingHistory(roomName);
        }
        JitsiMeetActivity.launch(context, options);
    }
}
