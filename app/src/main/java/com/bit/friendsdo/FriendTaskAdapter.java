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

import com.bit.friendsdo.FriendTask;
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

        // Set long click listener
        holder.itemView.setOnLongClickListener(view -> {
            showDeleteConfirmationDialog(holder.itemView, position);
            return true; // Return true to consume the long click event
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

    private void showDeleteConfirmationDialog( View view, int position) {
        Context context = view.getContext(); // Get the context from the itemView

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this task?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(view, position);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTask(View view, int position) {
        Context context = view.getContext();

        FriendTask task = friendTasks.get(position);
        String documentId = task.getId();

        taskCollection.document(documentId).delete()
                .addOnSuccessListener(aVoid -> {
                    friendTasks.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Task deleted", Toast.LENGTH_SHORT).show();
                    TaskDoneListFragment.checkEmpty(TaskDoneListFragment.emptyDoneText);
                    TaskListFragment.checkEmpty(TaskListFragment.emptyText);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Error deleting task", Toast.LENGTH_SHORT).show();
                });
    }
}
