package com.bit.friendsdo;

import java.util.Date;

public class FriendTask {
    private String id;
    private String taskText;
    private Date creationDate;
    private String owner;
    private boolean taskDone;
    private Date doneDate;
    private String doneOwner;
    private String imageUrl;

    public FriendTask(String taskText, Date creationDate, String owner, boolean taskDone, Date doneDate, String doneOwner, String imageUrl) {
        this.taskText = taskText;
        this.creationDate = creationDate;
        this.owner = owner;
        this.taskDone = taskDone;
        this.doneDate = doneDate;
        this.doneOwner = doneOwner;
        this.imageUrl = imageUrl;
    }
    public FriendTask(String id, String taskText, Date creationDate, String owner, boolean taskDone, Date doneDate, String doneOwner, String imageUrl) {
        this.id = id;
        this.taskText = taskText;
        this.creationDate = creationDate;
        this.owner = owner;
        this.taskDone = taskDone;
        this.doneDate = doneDate;
        this.doneOwner = doneOwner;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
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

    public String getDoneOwner() {
        return doneOwner;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setTaskDone(boolean taskDone) {
        this.taskDone = taskDone;
    }

    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setDoneOwner(String doneOwner) {
        this.doneOwner = doneOwner;
    }
}
