package com.example.sharinghands.ui.Donor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sharinghands.DonorHome;
import com.example.sharinghands.EmailRegex.EmailRegex;
import com.example.sharinghands.R;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.net.Inet4Address;

public class DonorRegistration extends AppCompatActivity {

    Context context = this;
    private FirebaseAuth firebaseAuth;

    EditText name, email, password, confirm_password;
    Button btn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_registration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final String emailRegex = EmailRegex.getInstance().getEmailRegex();

        firebaseAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.donor_name);
        email = findViewById(R.id.donor_email_address);
        password = findViewById(R.id.donor_passcode);
        confirm_password = findViewById(R.id.donor_confirm_password);
        progressBar = findViewById(R.id.progress_circular_reg);

        btn = findViewById(R.id.donor_register);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String username = name.getText().toString();
                String emailAddress = email.getText().toString();
                String donorPassword = password.getText().toString();
                String confirmPassword = confirm_password.getText().toString();

                if (username.isEmpty() || emailAddress.isEmpty() || donorPassword.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),"All Fields Are Required!" , Toast.LENGTH_SHORT).show();

                }
                else{
                    if (emailAddress.matches(emailRegex)){
                        if (donorPassword.length() > 5){
                            if (donorPassword.equals(confirmPassword)){

                                progressBar.setVisibility(View.VISIBLE);

                                firebaseAuth.createUserWithEmailAndPassword(emailAddress, donorPassword)
                                        .addOnCompleteListener(DonorRegistration.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                progressBar.setVisibility(View.GONE);

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username).build();

                                                user.updateProfile(profileUpdates);

                                                if (!task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Registration Failed!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Intent intent = new Intent(DonorRegistration.this, DonorHome.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Password did not Match!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Password Must be greater than 5 chars", Toast.LENGTH_SHORT).show();

                    }
                    else
                        Toast.makeText(getApplicationContext(),"Invalid Email Address!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
}
