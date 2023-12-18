package com.akshar.videochat.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.akshar.videochat.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class FirebaseAuthUtil {

    private final Activity activity;
    private final FirebaseAuth mAuth;
    private final GoogleSignInClient mGoogleSignInClient;
    private final FirebaseFirestore firebaseFirestore;

    public FirebaseAuthUtil(Activity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mGoogleSignInClient = configureGoogleSignIn();
    }

    public void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, 100);
    }

    public void handleGoogleSignInResult(Intent data, OnCompleteListener<AuthResult> onCompleteListener) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if (account != null) {
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(activity, onCompleteListener);
            }
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public void signOut() {
        mAuth.signOut();
        mGoogleSignInClient.signOut();
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

    private GoogleSignInClient configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("869623115308-n2dm06iviomod32tgr6q7aq3c525ac99.apps.googleusercontent.com")
                .requestEmail()
                .build();
        return GoogleSignIn.getClient(activity, gso);
    }

    public void pushDataToFirestore() {
        FirebaseUser firebaseUser = getCurrentUser();
        if (firebaseUser != null) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", firebaseUser.getDisplayName());
            userMap.put("image", firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : null);
            userMap.put("uid", firebaseUser.getUid());
            userMap.put("status", "offline");
            userMap.put("email", firebaseUser.getEmail());

            firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                    .set(userMap)
                    .addOnSuccessListener(aVoid -> {
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors here
                        Toast.makeText(activity, "Error writing to Firestore", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void changeStatus(String status) {
        FirebaseUser firebaseUser = getCurrentUser();
        if (firebaseUser != null) {
            firebaseFirestore.collection("Users").document(firebaseUser.getUid())
                    .update("status", status)
                    .addOnSuccessListener(aVoid -> {
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors here
                        Toast.makeText(activity, "Error writing to Firestore", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    public void addMeetingHistory(String meetingName){
        try {
            firebaseFirestore.collection("Users").document(getCurrentUser().getUid())
                    .collection("meetings").add(new HashMap<String, Object>() {{
                        put("meetingName", meetingName);
                        put("created_at", System.currentTimeMillis());
                    }}
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void getMeetingHistory(OnCompleteListener<QuerySnapshot> onCompleteListener) {
        firebaseFirestore.collection("Users").document(getCurrentUser().getUid())
                .collection("meetings")
                .orderBy("created_at", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(onCompleteListener);
    }

    public void updateProfileImage(Uri uri) {
        FirebaseUser firebaseUser = getCurrentUser();
        if (firebaseUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();

            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void updateProfile(String name) {
        FirebaseUser firebaseUser = getCurrentUser();
        if (firebaseUser != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            firebaseUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(activity, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

}
