package com.akshar.videochat.fregments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.akshar.videochat.R;
import com.akshar.videochat.utils.FirebaseAuthUtil;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ContactFragment extends Fragment {

    private View view;
    EditText name;
    ImageView profileImage, editProfileImage;
    TextView updateBtn, email;
    FirebaseAuthUtil firebaseAuthUtil;

    private static final int PICK_IMAGE = 1;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_meeting, container, false);

        firebaseAuthUtil = new FirebaseAuthUtil(requireActivity());

        profileImage = view.findViewById(R.id.profilePic);
        editProfileImage = view.findViewById(R.id.ivEdit);
        name = view.findViewById(R.id.etName);
        email = view.findViewById(R.id.etEmail);
        updateBtn = view.findViewById(R.id.tvUpdateBtn);

        name.setText(firebaseAuthUtil.getCurrentUser().getDisplayName());
        email.setText(firebaseAuthUtil.getCurrentUser().getEmail());

        Picasso.get().load(firebaseAuthUtil.getCurrentUser().getPhotoUrl()).into(profileImage);

        updateBtn.setOnClickListener(v -> firebaseAuthUtil.updateProfile(name.getText().toString()));

        editProfileImage.setOnClickListener(v -> {
            openImagePicker(); // Call the method to open the image picker
        });

        return view;
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                firebaseAuthUtil.updateProfileImage(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}