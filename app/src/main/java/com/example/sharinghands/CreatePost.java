package com.example.sharinghands;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sharinghands.ui.NGO.Dashboard;
import com.example.sharinghands.ui.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreatePost extends AppCompatActivity {

    Button post_image_upload_btn;
    ImageView post_image_container;
    Uri image_uri;
    ProgressBar progressBar;
    Post post;
    EditText title_of_post, description_of_post, raised_amount_of_post, required_amount_of_post;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference mDatabase;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        setTitle("Create New Post");

        post_image_upload_btn = findViewById(R.id.post_image_upload_btn);
        post_image_container = findViewById(R.id.post_image_container);
        title_of_post = findViewById(R.id.title_of_post);
        description_of_post = findViewById(R.id.description_of_post);
        raised_amount_of_post = findViewById(R.id.raised_amount_of_post);
        required_amount_of_post = findViewById(R.id.required_amount_of_post);
        progressBar = findViewById(R.id.progress_circular_post);

        post_image_upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent().setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1 && data.getData() !=null ){
                image_uri = data.getData();
                post_image_container.setImageURI(image_uri);

            storageReference = firebaseStorage.getInstance().getReference().child("post").child(image_uri.getLastPathSegment());

            storageReference.putFile(image_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(),"Uploaded!", Toast.LENGTH_SHORT).show();
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Uploaded!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.post_add_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_post) {
            String title = title_of_post.getText().toString();
            String desc = description_of_post.getText().toString();
            String raised = raised_amount_of_post.getText().toString();
            String required = required_amount_of_post.getText().toString();
            String userID = user.getUid();

            mDatabase = FirebaseDatabase.getInstance().getReference();

            if (!title.isEmpty() && !desc.isEmpty() && !raised.isEmpty() && !required.isEmpty()) {

                int raised_amount = Integer.parseInt(raised);
                int required_amount = Integer.parseInt(required);

                if (raised_amount >=0 && required_amount >= 30)
                {

                if (raised_amount < required_amount) {

                    post = new Post(userID,R.drawable.logo, user.getDisplayName(), title, desc,
                            R.drawable.logo, raised_amount, required_amount);

                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    progressBar.setVisibility(View.VISIBLE);


                    mDatabase.child("Post").push().setValue(post)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplication(), "Post Created Successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreatePost.this, Dashboard.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressBar.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplication(), "Post Failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }else {
                    Toast.makeText(getApplicationContext(),"Raised Amount Cant be greater than Required Amount!",Toast.LENGTH_SHORT).show();
                }
                }

            }else
                Toast.makeText(getApplicationContext(),"All fields are Required!",Toast.LENGTH_SHORT).show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreatePost.this, Dashboard.class));
        finish();
    }
}
