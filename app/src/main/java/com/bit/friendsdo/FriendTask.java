package com.bit.friendsdo;

import java.util.Date;

public class FriendTask {
    private String taskText;
    private Date creationDate;
    private String owner;
    private boolean taskDone;
    private Date doneDate;

    public FriendTask(String taskText, Date creationDate, String owner, boolean taskDone, Date doneDate) {
        this.taskText = taskText;
        this.creationDate = creationDate;
        this.owner = owner;
        this.taskDone = taskDone;
        this.doneDate = doneDate;
    }

    public String getTaskText() {
        return taskText;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String getOwner() {
        return owner;
    }

    public boolean isTaskDone() {
        return taskDone;
    }

    public Date getDoneDate() {
        return doneDate;
    }
}
