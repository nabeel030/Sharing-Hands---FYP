package com.example.sharinghands.ui.NGO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sharinghands.ChangePassword;
import com.example.sharinghands.LoginActivity;
import com.example.sharinghands.LogoUpload;
import com.example.sharinghands.R;
import com.example.sharinghands.ui.Post;
import com.example.sharinghands.ui.PostAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ActivePosts extends AppCompatActivity {

    Context context = this;
    RecyclerView recyclerView;
    ArrayList<Post> arrayList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    SwipeRefreshLayout refreshLayout;
    RecyclerView.Adapter adapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Post");
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_posts);

        recyclerView = findViewById(R.id.active_post_recyclerview);
        refreshLayout = findViewById(R.id.refresh);
        progressBar = findViewById(R.id.active_post_progressbar);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));


        progressBar.setVisibility(View.VISIBLE);

        final String current_ngo_id = firebaseAuth.getUid();
        Intent intent = getIntent();
        final String postStatus =  intent.getStringExtra("post_status");

        if (postStatus.equals("active")) {
            setTitle("Active Posts");
        } else if(postStatus.equals("all")) {
            setTitle("All Posts");
        } else {
            setTitle("Completed Posts");
        }

        laodPosts(databaseReference, current_ngo_id, postStatus);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                laodPosts(databaseReference, current_ngo_id, postStatus);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },4000);

            }
        });

    }

    public void laodPosts(DatabaseReference databaseReference, final String current_ngo_id, final String postStatus) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrayList.clear();

                for (DataSnapshot posts : dataSnapshot.getChildren()){

                    Post post = posts.getValue(Post.class);
                    post.setPostKey(posts.getKey());

                    if (post.getNgo_id().equals(current_ngo_id)) {
                        if (postStatus.equals("active")) {
                            if (post.getRaised_amount() < post.getRequired_amount()) {
                                arrayList.add(post);
                            }
                        }

                        if (postStatus.equals("all")) {
                            if (post.getRaised_amount() >= post.getRequired_amount()) {
                                post.setPost_status(true);
                            }
                            arrayList.add(post);
                        }

                        if (postStatus.equals("completed")) {
                            if (post.getRaised_amount() >= post.getRequired_amount()) {
                                post.setPost_status(true);
                                arrayList.add(post);
                            }
                        }
                    }
                }

                adapter = new PostAdapter(arrayList,context);

                layoutManager = new LinearLayoutManager(getApplicationContext());
                ((LinearLayoutManager) layoutManager).setReverseLayout(true);
                ((LinearLayoutManager) layoutManager).setStackFromEnd(true);

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                progressBar.setVisibility(View.GONE);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Failed!", Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(ActivePosts.this, Dashboard.class);
                startActivity(i);
                finish();
                return true;

            case R.id.ngo_change_password:
                Intent intent = new Intent(ActivePosts.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.ngo_change_logo:
                Intent logo_intent = new Intent(this, LogoUpload.class);
                startActivity(logo_intent);
                finish();
            return true;

            case R.id.ngo_logout:
                FirebaseAuth.getInstance().signOut();

                SharedPreferences sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("status", "");
                editor.apply();

                startActivity(new Intent(ActivePosts.this, LoginActivity.class));
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
                                                    startActivity(new Intent(ActivePosts.this,LoginActivity.class));
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
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
    }
}
