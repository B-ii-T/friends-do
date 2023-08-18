package com.bit.friendsdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);

        // Set the initial fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame, new TaskListFragment())
                .commit();

        // Handle item clicks in the bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_task_list) {
                selectedFragment = new TaskListFragment();
            } else if (item.getItemId() == R.id.nav_task_done) {
                selectedFragment = new TaskDoneListFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, selectedFragment)
                    .commit();

            return true;
        });

//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference friendsTaskCollection = db.collection("FriendTask");
//
//        FriendTask task = new FriendTask("clean the room", new Date(), "Fatah", false, null);
//        friendsTaskCollection.add(task);
//        Toast.makeText(this, "task added", Toast.LENGTH_SHORT).show();
    }
}