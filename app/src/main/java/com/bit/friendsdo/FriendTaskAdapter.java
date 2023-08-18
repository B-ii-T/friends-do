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

        // Bind data to the views in the ViewHolder
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

        String formattedCreationDate = dateFormat.format(friendTask.getCreationDate());

        holder.ownerText.setText(friendTask.getOwner());
        holder.tasktext.setText(friendTask.getTaskText());
        holder.dateText.setText(formattedCreationDate);
        // Bind more data here for other task properties
    }

    @Override
    public int getItemCount() {
        return friendTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView ownerText, tasktext, dateText;
        // Declare other views here for other task properties

        public ViewHolder(View itemView) {
            super(itemView);
            ownerText = itemView.findViewById(R.id.owner_textview);
            tasktext = itemView.findViewById(R.id.task_text_textview);
            dateText = itemView.findViewById(R.id.creation_date_textview);
            // Initialize other views here
        }
    }
}
