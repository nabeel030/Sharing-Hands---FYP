package com.example.sharinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.sharinghands.ui.NGO.ActivePosts;
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

public class DonorHome extends AppCompatActivity {

    Context context = this;
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;
    ArrayList<Post> arrayList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("Post");
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home);

        setTitle("Active Posts");


        recyclerView = findViewById(R.id.post_recyclerview);
        refreshLayout = findViewById(R.id.swipeRefresh);
        progressBar = findViewById(R.id.donor_home_progressbar);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        progressBar.setVisibility(View.VISIBLE);

        loadPosts(databaseReference);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                loadPosts(databaseReference);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                    }
                },3000);

            }
        });


    }

    public void loadPosts(DatabaseReference databaseReference) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                arrayList.clear();

                for (DataSnapshot posts : dataSnapshot.getChildren()){

                    Post post = posts.getValue(Post.class);

                    if (post.getRaised_amount() < post.getRequired_amount()) {
                        post.setPostKey(posts.getKey());
                        arrayList.add(post);
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
        inflater.inflate(R.menu.donor_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Toast.makeText(getApplicationContext(),"History Tab", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.change_password:
                Intent intent = new Intent(DonorHome.this, ChangePassword.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.donor_logout:
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("login_session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("status", "");
                editor.apply();

                startActivity(new Intent(DonorHome.this,LoginActivity.class));
                finish();
                return true;

            case R.id.donor_delete_account:
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
                                                    startActivity(new Intent(DonorHome.this,LoginActivity.class));
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
