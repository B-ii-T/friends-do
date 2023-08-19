package com.bit.friendsdo;

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

public class TaskDoneListFragment extends Fragment {
    private RecyclerView recyclerView;
    private static List<FriendTask> friendTasks = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference taskCollection = db.collection("FriendTask");
    FriendTaskAdapter adapter = new FriendTaskAdapter(friendTasks);

    ProgressBar doneSpinner;
    public static TextView emptyDoneText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.done_task_list_fragment, container, false);
        emptyDoneText = rootView.findViewById(R.id.no_done_tasks);


        recyclerView = rootView.findViewById(R.id.done_task_recycler_view);
        doneSpinner = rootView.findViewById(R.id.progress_bar_done);
        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(layoutManager);

        checkEmpty(emptyDoneText);

        doneSpinner.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyDoneText.setVisibility(View.GONE);

        taskCollection.orderBy("doneDate", Query.Direction.DESCENDING).get().addOnCompleteListener(task -> {
            doneSpinner.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            checkEmpty(emptyDoneText);
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
                    String doneOwner = document.getString("doneOwner");
                    if (taskDone) {
                        friendTasks.add(new FriendTask(id, taskText, creationDate, owner, taskDone, doneDate, doneOwner));
                        adapter.notifyDataSetChanged();
                        checkEmpty(emptyDoneText);
                    }
                }
            } else {
                Log.e("Firestore", "Error getting documents: ", task.getException());
                Toast.makeText(getContext(), "Err fetching", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                FriendTask task = friendTasks.get(position);

                if (task.isTaskDone()) {
                    task.setTaskDone(false);
                    task.setCreationDate(new Date());
                    task.setDoneDate(null);
                    task.setDoneOwner("");

                    String documentId = task.getId();

                    taskCollection.document(documentId).update(
                            "taskDone", false,
                            "creationDate", task.getCreationDate()
                    ).addOnSuccessListener(aVoid -> {
                        friendTasks.remove(position);
                        adapter.notifyItemRemoved(position);
                        checkEmpty(emptyDoneText);
                    }).addOnFailureListener(e -> {
                        // Handle the error
                        Log.e("Firestore", "Error updating task: ", e);
                        Toast.makeText(getContext(), "Error updating task", Toast.LENGTH_SHORT).show();
                        // Revert changes
                        task.setTaskDone(true);
                        adapter.notifyItemChanged(position);
                        checkEmpty(emptyDoneText);
                    });
                } else {
                    friendTasks.remove(position);
                    adapter.notifyItemRemoved(position);
                    checkEmpty(emptyDoneText);

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
