package com.example.sharinghands.ui.Donor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sharinghands.ChangePassword;
import com.example.sharinghands.DatabaseHelper;
import com.example.sharinghands.DonorHome;
import com.example.sharinghands.LoginActivity;
import com.example.sharinghands.R;
import com.example.sharinghands.ui.NGO.ActivePosts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class History extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("History");

        final ListView list = findViewById(R.id.historyList);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        ArrayList<String> arrayList = new ArrayList<>();

        Cursor cursor = databaseHelper.getUserHistory(user.getUid());

        while (cursor.moveToNext()) {
            String info = "You have donated Rs. "+cursor.getString(4) + " to a Post titled (" + cursor.getString(3) + ") posted by NGO ("+ cursor.getString(2) + ")";
            arrayList.add(info);

        }


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.donor_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.posts:
                Intent posts = new Intent(History.this, ActivePosts.class);
                posts.putExtra("post_status", "active");
                startActivity(posts);
                finish();
                return true;

            case R.id.history:
                Intent history = new Intent(History.this, History.class);
                startActivity(history);
                finish();
                return true;

            case R.id.change_password:
                Intent intent = new Intent(History.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.donor_logout:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("status", "");
                editor.apply();

                startActivity(new Intent(History.this, LoginActivity.class));
                finish();
                return true;

            case R.id.donor_delete_account:
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getApplicationContext());
                alertDialogBuilder.setTitle("Caution!");

                alertDialogBuilder.setMessage("Are You Sure? After you delete an account, it's permanently deleted!")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(getApplicationContext(),"Account Not Deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                user.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    startActivity(new Intent(History.this,LoginActivity.class));
                                                    finish();
                                                    Toast.makeText(getApplicationContext(),"Account Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {

    }
}
