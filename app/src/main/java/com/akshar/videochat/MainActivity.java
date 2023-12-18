package com.akshar.videochat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.akshar.videochat.utils.FirebaseAuthUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuthUtil firebaseAuthUtil;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton = findViewById(R.id.bt_sign_in);

        firebaseAuthUtil = new FirebaseAuthUtil(this);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("869623115308-n2dm06iviomod32tgr6q7aq3c525ac99.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        // Check if the user is already signed in
        if (firebaseAuthUtil.getCurrentUser() != null) {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
            finish();
        }

        signInButton.setOnClickListener(v -> {
            Intent intent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(intent, 100);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            firebaseAuthUtil.handleGoogleSignInResult(data, task -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(MainActivity.this, HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    firebaseAuthUtil.pushDataToFirestore();
                    Toast.makeText(this, "Firebase authentication successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Authentication Failed: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuthUtil.changeStatus("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        firebaseAuthUtil.changeStatus("offline");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseAuthUtil.changeStatus("offline");
    }

    @Override
    protected void onResume() {
        super.onResume();
        firebaseAuthUtil.changeStatus("online");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        firebaseAuthUtil.changeStatus("online");
    }
}