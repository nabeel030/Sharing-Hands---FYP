package com.example.sharinghands;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sharinghands.EmailRegex.EmailRegex;
import com.example.sharinghands.ui.NGO.NGOLoginFragment;
import com.example.sharinghands.ui.NGO.NGORegistration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    EditText forget_email;
    Button reset_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        setTitle("Reset Password");

        forget_email = findViewById(R.id.forget_email);
        reset_password = findViewById(R.id.reset_password);

        final String emailRegex = EmailRegex.getInstance().getEmailRegex();


        reset_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forget_email.getText().toString();

                if (!email.isEmpty()) {
                    if (email.matches(emailRegex)) {
                        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                    ForgetPassword.this);
                                            alertDialogBuilder.setTitle("Info!");

                                            alertDialogBuilder.setMessage("Password Reset Link Has Been Sent To Your Email Address!")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            Intent intent = new Intent(ForgetPassword.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    });

                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                        } else {
                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                    ForgetPassword.this);
                                            alertDialogBuilder.setTitle("Info!");

                                            alertDialogBuilder.setMessage("Sorry! This email address is not associated with this application.")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                        }
                                                    });

                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                            alertDialog.show();
                                        }
                                    }
                                });
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter valid email address!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter your email address!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgetPassword.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
