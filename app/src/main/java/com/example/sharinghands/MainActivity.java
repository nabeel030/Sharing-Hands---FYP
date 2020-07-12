package com.example.sharinghands;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.sharinghands.ui.NGO.ActivePosts;
import com.example.sharinghands.ui.NGO.Dashboard;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    Timer timer;
    private SharedPreferences sharedpreferences;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                sharedpreferences = getSharedPreferences("login_session",MODE_PRIVATE);
                String status = sharedpreferences.getString("status","");

                if (firebaseAuth.getCurrentUser() != null && status.equals("donor") && firebaseAuth.getCurrentUser().isEmailVerified()) {
                    // User is logged in
                    Intent intent = new Intent(getApplicationContext(), DonorHome.class);
                    startActivity(intent);
                }

                else if (firebaseAuth.getCurrentUser() != null && status.equals("ngo") && firebaseAuth.getCurrentUser().isEmailVerified()) {
                    // User is logged in
                    Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }

            }

        },3000);
    }
}
