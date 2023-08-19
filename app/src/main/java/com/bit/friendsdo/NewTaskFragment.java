package com.bit.friendsdo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class NewTaskFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPref";
    private static final String KEY_NAME = "user_name";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.new_task_fragment, container, false);

        EditText taskEditText = rootView.findViewById(R.id.task_edit_text_view);
        Button publishBtn = rootView.findViewById(R.id.publish_btn);

        publishBtn.setOnClickListener(v -> {
            if (taskEditText.getText().toString().trim().isEmpty()) {
                taskEditText.setError("Required");
            } else {
                sharedPreferences = getActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                String name = sharedPreferences.getString(KEY_NAME, "");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference friendsTaskCollection = db.collection("FriendTask");

                FriendTask task = new FriendTask(taskEditText.getText().toString().trim(), new Date(), name, false, null, "");
                friendsTaskCollection.add(task);
                Toast.makeText(getContext(), "task added", Toast.LENGTH_SHORT).show();
                taskEditText.setText("");
//                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                TaskListFragment targetFragment = new TaskListFragment();
//                fragmentTransaction.replace(R.id.frame, targetFragment);
//                fragmentTransaction.commit();
            }
        });
        return rootView;
    }
}
