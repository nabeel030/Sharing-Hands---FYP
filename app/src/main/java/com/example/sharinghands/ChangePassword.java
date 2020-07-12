package com.example.sharinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sharinghands.ui.NGO.Dashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassword extends AppCompatActivity {

    Context context = this;
    Button btn;
    EditText donor_new_password, donor_confirm_password, donor_old_password;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btn = findViewById(R.id.donor_change_password_btn);
        donor_old_password = findViewById(R.id.donor_old_password);
        donor_new_password = findViewById(R.id.donor_new_password);
        donor_confirm_password = findViewById(R.id.donor_confirm_passcode);
        final ProgressBar progressBar = findViewById(R.id.change_progress_circular);

        sharedPreferences = getSharedPreferences("login_session",MODE_PRIVATE);
        final String status = sharedPreferences.getString("status","");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                String userEmail = user.getEmail();
                String oldPassword = donor_old_password.getText().toString();
                final String newPassword = donor_new_password.getText().toString();
                String confirm_password = donor_confirm_password.getText().toString();


                final AlertDialog.Builder alertDialogue = new AlertDialog.Builder(context);

                if (!confirm_password.isEmpty() && !newPassword.isEmpty()) {

                    assert userEmail != null;
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(userEmail, oldPassword);

                    if (newPassword.length() > 6) {
                        if (newPassword.equals(confirm_password)) {

                            progressBar.setVisibility(View.VISIBLE);

                            user.reauthenticate(credential)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                user.updatePassword(newPassword)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                progressBar.setVisibility(View.GONE);

                                                                if (task.isSuccessful()) {
                                                                    alertDialogue.setTitle("Password Changed!");
                                                                    alertDialogue.setMessage("Your Password has been changed successfully!")
                                                                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                                                                @Override
                                                                                public void onClick(DialogInterface dialog, int which) {
                                                                                    if (status.equals("donor")) {
                                                                                        Intent intent = new Intent(ChangePassword.this, DonorHome.class);
                                                                                        startActivity(intent);
                                                                                    }
                                                                                    else if(status.equals("ngo")){
                                                                                        finish();Intent intent = new Intent(ChangePassword.this, Dashboard.class);
                                                                                        startActivity(intent);
                                                                                        finish();
                                                                                    }
                                                                                }
                                                                            });
                                                                    AlertDialog alertDialog = alertDialogue.create();
                                                                    alertDialog.show();
                                                                }else
                                                                    Toast.makeText(getApplicationContext(), "Password Update Failed!", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }
                                    });

                        }else
                            Toast.makeText(getApplicationContext(), "Password Did not Match!", Toast.LENGTH_SHORT).show();

                    }else
                        Toast.makeText(getApplicationContext(), "Password Length must be greate than 6 chars!", Toast.LENGTH_SHORT).show();

                }else
                    Toast.makeText(getApplicationContext(), "Both fields are required!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        sharedPreferences = getSharedPreferences("login_session", MODE_PRIVATE);
        String status = sharedPreferences.getString("status", "");
        if (status.equals("donor")) {
            startActivity(new Intent(this, DonorHome.class));
            finish();
        }
        else if(status.equals("ngo")){
            startActivity(new Intent(this, Dashboard.class));
            finish();
        }
    }
}