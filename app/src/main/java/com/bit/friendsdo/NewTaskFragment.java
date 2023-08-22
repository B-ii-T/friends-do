package com.bit.friendsdo;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bit.friendsdo.FriendTask;
import com.bit.friendsdo.MainActivity;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public class NewTaskFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPref";
    private static final String KEY_NAME = "user_name";

    private ImageView attachBtn;
    private Uri attachedImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_task_fragment, container, false);

        EditText taskEditText = rootView.findViewById(R.id.task_edit_text_view);
        Button publishBtn = rootView.findViewById(R.id.publish_btn);
        attachBtn = rootView.findViewById(R.id.attach_btn);

        attachBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        publishBtn.setOnClickListener(v -> {
            if (taskEditText.getText().toString().trim().isEmpty()) {
                taskEditText.setError("Required");
            } else {
                publishBtn.setClickable(false);
                publishBtn.setText("Uploading ..");
                attachBtn.setVisibility(View.GONE);
                sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                String name = sharedPreferences.getString(KEY_NAME, "");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference friendsTaskCollection = db.collection("FriendTask");

                if (attachedImageUri != null) {
                    new Thread(() -> {
                        try {
                            InputStream inputStream = getContext().getContentResolver().openInputStream(attachedImageUri);
                            if (inputStream != null) {
                                Map<String, Object> uploadResult = MainActivity.cloudinary.uploader().upload(inputStream, ObjectUtils.emptyMap());
                                String imageUrl = (String) uploadResult.get("secure_url");
                                FriendTask task = new FriendTask(taskEditText.getText().toString().trim(), new Date(), name, false, null, "", imageUrl);
                                friendsTaskCollection.add(task);
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    publishBtn.setClickable(true);
                                    publishBtn.setText("Publish task");
                                    attachBtn.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                                    taskEditText.setText("");
                                });
                            } else {
                                new Handler(Looper.getMainLooper()).post(() -> {
                                    publishBtn.setClickable(true);
                                    publishBtn.setText("Publish task");
                                    attachBtn.setVisibility(View.VISIBLE);
                                    Toast.makeText(getContext(), "Error reading image", Toast.LENGTH_SHORT).show();
                                });
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("UploadError", "Error uploading image: " + e.getMessage());
                            new Handler(Looper.getMainLooper()).post(() -> {
                                publishBtn.setText("Publish task");
                                publishBtn.setClickable(true);
                                attachBtn.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), "Error uploading image", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }).start();
                } else {
                    FriendTask task = new FriendTask(taskEditText.getText().toString().trim(), new Date(), name, false, null, "", null);
                    friendsTaskCollection.add(task);
                    publishBtn.setText("Publish task");
                    publishBtn.setClickable(true);

                    Toast.makeText(getContext(), "Task added", Toast.LENGTH_SHORT).show();
                    taskEditText.setText("");
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            attachedImageUri = imageUri;
//            Toast.makeText(getContext(), "Image attached: " + imageUri.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
