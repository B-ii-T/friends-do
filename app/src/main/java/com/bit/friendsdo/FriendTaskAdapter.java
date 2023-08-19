package com.bit.friendsdo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.List;

public class FriendTaskAdapter extends RecyclerView.Adapter<FriendTaskAdapter.ViewHolder> {
    private List<FriendTask> friendTasks;
    private CollectionReference taskCollection = FirebaseFirestore.getInstance().collection("FriendTask");

    public FriendTaskAdapter(List<FriendTask> friendTasks) {
        this.friendTasks = friendTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendTask friendTask = friendTasks.get(position);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        holder.ownerText.setText(friendTask.getOwner());
        holder.tasktext.setText(friendTask.getTaskText());
        if (friendTask.isTaskDone()) {
            String formattedDoneDate = dateFormat.format(friendTask.getDoneDate());
            String formattedDoneTime = timeFormat.format(friendTask.getDoneDate());
            holder.dateText.setText(formattedDoneDate);
            holder.timeText.setText(formattedDoneTime);
        } else {
            String formattedCreationDate = dateFormat.format(friendTask.getCreationDate());
            String formattedCreationTime = timeFormat.format(friendTask.getCreationDate());
            holder.dateText.setText(formattedCreationDate);
            holder.timeText.setText(formattedCreationTime);
        }

        // Set click listener
        holder.itemView.setOnLongClickListener(view -> {
            showDeleteConfirmationDialog(holder.itemView, friendTask.getId());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return friendTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ownerText, tasktext, dateText, timeText;

        public ViewHolder(View itemView) {
            super(itemView);
            ownerText = itemView.findViewById(R.id.owner_textview);
            tasktext = itemView.findViewById(R.id.task_text_textview);
            dateText = itemView.findViewById(R.id.creation_date_textview);
            timeText = itemView.findViewById(R.id.timestamp_text_view);
        }
    }

    private void showDeleteConfirmationDialog(View view, String taskId) {
        Context context = view.getContext();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTaskById(taskId, context); // Pass the context here
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTaskById(String taskId, Context context) {
        taskCollection.document(taskId).delete()
                .addOnSuccessListener(aVoid -> {
                    int position = findPositionById(taskId);
                    if (position != -1) {
                        friendTasks.remove(position);
                        notifyItemRemoved(position);
                        Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error deleting task", Toast.LENGTH_SHORT).show();
                });
    }



    private int findPositionById(String taskId) {
        for (int i = 0; i < friendTasks.size(); i++) {
            if (friendTasks.get(i).getId().equals(taskId)) {
                return i;
            }
        }
        return -1; // Task not found
    }
}
