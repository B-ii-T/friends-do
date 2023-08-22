package com.bit.friendsdo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;

public class FriendTaskAdapter extends RecyclerView.Adapter<FriendTaskAdapter.ViewHolder> {
    private List<FriendTask> friendTasks;
    private CollectionReference taskCollection = FirebaseFirestore.getInstance().collection("FriendTask");

    public FriendTaskAdapter(List<FriendTask> friendTasks) {
        this.friendTasks = friendTasks;
    }

    private SharedPreferences sharedPreferences;

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
            holder.doneOwnerText.setText(friendTask.getDoneOwner());
        } else {
            String formattedCreationDate = dateFormat.format(friendTask.getCreationDate());
            String formattedCreationTime = timeFormat.format(friendTask.getCreationDate());
            holder.dateText.setText(formattedCreationDate);
            holder.timeText.setText(formattedCreationTime);
            holder.doneOwnerText.setVisibility(View.GONE);
        }

        // Handle imgIndicator visibility based on imageUrl
        if (friendTask.getImageUrl() == null) {
            holder.imgIndicator.setVisibility(View.GONE);
        } else {
            holder.imgIndicator.setVisibility(View.VISIBLE);
        }

        // Set click listener for image toggle
        holder.itemView.setOnClickListener(v -> {
            if (friendTask.getImageUrl() == null) {
                holder.taskImg.setVisibility(View.GONE);
            } else {
                if (holder.taskImg.getVisibility() == View.VISIBLE) {
                    holder.taskImg.setVisibility(View.GONE);
                } else {
                    holder.taskImg.setVisibility(View.VISIBLE);
                    Picasso.get()
                            .load(friendTask.getImageUrl())
                            .placeholder(R.drawable.friendsdo_logo) // Optional: Placeholder image while loading
                            .error(R.drawable.add_photo) // Optional: Image to display in case of loading error
                            .into(holder.taskImg);
                }
            }
        });

        holder.taskImg.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Image clicked", Toast.LENGTH_SHORT).show();
            showImageDialog(friendTask.getImageUrl(), v.getContext());
        });

        // Handle long click listener for deleting tasks
        holder.itemView.setOnLongClickListener(view -> {
            sharedPreferences = holder.itemView.getContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            if (friendTask.getOwner().equals(sharedPreferences.getString("user_name", ""))) {
                showDeleteConfirmationDialog(holder.itemView, friendTask.getId());
            } else {
                Toast.makeText(view.getContext(), "You can't delete a task you did not create", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void showImageDialog(String imageUrl, Context context) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_image_preview);

        ImageView imageView = dialog.findViewById(R.id.imageView);
        Picasso.get().load(imageUrl).into(imageView);

        dialog.show();
    }

    @Override
    public int getItemCount() {
        return friendTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ownerText, tasktext, dateText, timeText, doneOwnerText;
        ImageView taskImg, imgIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            ownerText = itemView.findViewById(R.id.owner_textview);
            tasktext = itemView.findViewById(R.id.task_text_textview);
            dateText = itemView.findViewById(R.id.creation_date_textview);
            timeText = itemView.findViewById(R.id.timestamp_text_view);
            doneOwnerText = itemView.findViewById(R.id.done_owner_text_view);
            taskImg = itemView.findViewById(R.id.task_img);
            imgIndicator = itemView.findViewById(R.id.image_indicator);
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
                        deleteTaskById(taskId, context);
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
                        TaskListFragment.checkEmpty(TaskListFragment.emptyText);
                        TaskDoneListFragment.checkEmpty(TaskDoneListFragment.emptyDoneText);
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
        TaskListFragment.checkEmpty(TaskListFragment.emptyText);
        TaskDoneListFragment.checkEmpty(TaskDoneListFragment.emptyDoneText);
        return -1;
    }
}
