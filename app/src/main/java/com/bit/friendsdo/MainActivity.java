package com.bit.friendsdo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPref";
    private static final String KEY_NAME = "user_name";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        if (!sharedPreferences.contains(KEY_NAME)) {
            showNameDialog();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bar);
        bottomNavigationView.setItemActiveIndicatorEnabled(false);

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
            } else if(item.getItemId() == R.id.nav_add_task) {
                selectedFragment  = new NewTaskFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, selectedFragment)
                    .commit();

            return true;
        });
    }

    private void showNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_enter_name, null);
        builder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.editTextName);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);

        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // Prevent dismissing the dialog by clicking outside

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                if (!name.isEmpty()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(KEY_NAME, name);
                    editor.apply();
                    dialog.dismiss();
                } else {
                    // Show an error message indicating that the name is required
                    editTextName.setError("Name is required");
                }
            }
        });

        dialog.show();
    }
}