package com.bit.friendsdo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bit.friendsdo.FriendTask;

import java.text.SimpleDateFormat;
import java.util.List;

public class FriendTaskAdapter extends RecyclerView.Adapter<FriendTaskAdapter.ViewHolder> {
    private List<FriendTask> friendTasks;

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
    }

    @Override
    public int getItemCount() {
        return friendTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ownerText, tasktext, dateText, timeText;
        // Declare other views here for other task properties

        public ViewHolder(View itemView) {
            super(itemView);
            ownerText = itemView.findViewById(R.id.owner_textview);
            tasktext = itemView.findViewById(R.id.task_text_textview);
            dateText = itemView.findViewById(R.id.creation_date_textview);
            timeText = itemView.findViewById(R.id.timestamp_text_view);
        }
    }
}
