package com.example.sharinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sharinghands.ui.Donor.DonorLoginFragment;
import com.example.sharinghands.ui.Post;
import com.example.sharinghands.ui.PostAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DonorHome extends AppCompatActivity {

    Context context = this;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_home);

        setTitle("Active Posts");

        recyclerView = findViewById(R.id.post_recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);


        ArrayList<Post> arrayList = new ArrayList<>();

        /*arrayList.add(new Post(R.drawable.logo,"Sharing Hands","Post Title","These are some description",
                        R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                        R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                R.drawable.logo,15000,5000));

        arrayList.add(new Post(R.drawable.logo,"NGO Name","Post Title","These are some description",
                R.drawable.logo,15000,5000));
*/
        arrayList.add(new Post(R.drawable.logo,"Sharing Hands","Wedding","Donate your money to support.",
                R.drawable.logo,100,100000));

        arrayList.add(new Post(R.drawable.icon,"Helping Hands","Health care","Some post description here.",
                R.drawable.post_image,1000,100000));

        arrayList.add(new Post(R.drawable.logo,"Almasad","Home Utilities","share your money with others",
                R.drawable.logo,10000,100000));

        adapter = new PostAdapter(arrayList,context);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


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
