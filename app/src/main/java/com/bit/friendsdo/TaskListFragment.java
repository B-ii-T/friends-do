package com.bit.friendsdo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskListFragment extends Fragment {

    private RecyclerView recyclerView;
    private static List<FriendTask> friendTasks = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference taskCollection = db.collection("FriendTask");
    FriendTaskAdapter adapter = new FriendTaskAdapter(friendTasks);
    private ProgressBar progressBar;
    public static TextView emptyText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.task_list_fragment, container, false);

        recyclerView = rootView.findViewById(R.id.task_recycler_view);
        progressBar = rootView.findViewById(R.id.progress_bar);
        emptyText = rootView.findViewById(R.id.no_tasks);
        checkEmpty(emptyText);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);

        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyText.setVisibility(View.GONE);


        taskCollection.orderBy("creationDate", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            checkEmpty(emptyText);
            if (task.isSuccessful()) {
                friendTasks.clear();
                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String id = document.getId();
                    String owner = document.getString("owner");
                    String taskText = document.getString("taskText");
                    Boolean taskDone = document.getBoolean("taskDone");
                    Date creationDate = document.getDate("creationDate");
                    Date doneDate = document.getDate("doneDate");
                    String doneOwnwe = document.getString("doneOwner");
                    String imageUrl = document.getString("imageUrl");
                    if (!taskDone) {
                        friendTasks.add(new FriendTask(id, taskText, creationDate, owner, taskDone, doneDate, doneOwnwe, imageUrl));
                        adapter.notifyDataSetChanged();
                        checkEmpty(emptyText);
                    }
                }
            } else {
                Log.e("Firestore", "Error getting documents: ", task.getException());
                Toast.makeText(getContext(), "Err fetching", Toast.LENGTH_SHORT).show();
                checkEmpty(emptyText);
            }
        });

        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                FriendTask task = friendTasks.get(position);

                if (!task.isTaskDone()) {
                    task.setTaskDone(true);
                    task.setDoneDate(new Date());
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                    task.setDoneOwner(sharedPreferences.getString("user_name", ""));
                    String documentId = task.getId();
                    taskCollection.document(documentId).update(
                            "taskDone", true,
                            "doneDate", task.getDoneDate(),
                            "doneOwner", task.getDoneOwner()
                    ).addOnSuccessListener(aVoid -> {
                        friendTasks.remove(position);
                        adapter.notifyItemRemoved(position);
                        checkEmpty(emptyText);
                    }).addOnFailureListener(e -> {
                        Log.e("Firestore", "Error updating task: ", e);
                        Toast.makeText(getContext(), "Error updating task", Toast.LENGTH_SHORT).show();
                        task.setTaskDone(false);
                        task.setDoneDate(null);
                        adapter.notifyItemChanged(position);
                        checkEmpty(emptyText);
                    });
                } else {
                    // Task is already done, remove it from the list and notify the adapter
                    friendTasks.remove(position);
                    adapter.notifyItemRemoved(position);
                    checkEmpty(emptyText);
                }
            }

        }).attachToRecyclerView(recyclerView);

        return rootView;
    }

    public static void checkEmpty(TextView textview) {
        if (friendTasks.isEmpty()) {
            textview.setVisibility(View.VISIBLE);
        } else {
            textview.setVisibility(View.GONE);
        }
    }
}
