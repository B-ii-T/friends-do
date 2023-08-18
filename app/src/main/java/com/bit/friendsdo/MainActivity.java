package com.bit.friendsdo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference friendsTaskCollection = db.collection("FriendTask");

        FriendTask task = new FriendTask("clean the room", new Date(), "Fatah", false, null);
        friendsTaskCollection.add(task);
        Toast.makeText(this, "task added", Toast.LENGTH_SHORT).show();
    }
}