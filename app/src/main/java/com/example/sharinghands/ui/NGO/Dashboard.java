package com.example.sharinghands.ui.NGO;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.sharinghands.ChangePassword;
import com.example.sharinghands.CreatePost;
import com.example.sharinghands.DonorHome;
import com.example.sharinghands.LoginActivity;
import com.example.sharinghands.ui.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sharinghands.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ConcurrentHashMap;

public class Dashboard extends AppCompatActivity {

    Context context = this;
    TextView post_count;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Post");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        post_count = findViewById(R.id.posts_count);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = 0;
                String ngo_id = user.getUid();

                for (DataSnapshot posts : dataSnapshot.getChildren()){
                    Post post = posts.getValue(Post.class);

                    assert post != null;
                    if (post.getNgo_id().equals(ngo_id)){
                        count++;
                    }
                }

                post_count.setText(String.valueOf(count));
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, CreatePost.class));
                finish();
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.ngo_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ngo_posts:
                Toast.makeText(getApplicationContext(),"Posts", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.ngo_change_password:
                Intent intent = new Intent(Dashboard.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.ngo_logout:
                FirebaseAuth.getInstance().signOut();

                SharedPreferences sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("status", "");
                editor.apply();

                startActivity(new Intent(Dashboard.this, LoginActivity.class));
                finish();
                return true;

            case R.id.ngo_delete_account:
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context);
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
                                                    startActivity(new Intent(Dashboard.this,LoginActivity.class));
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
