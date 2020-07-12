package com.example.sharinghands.ui.NGO;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sharinghands.EmailRegex.EmailRegex;
import com.example.sharinghands.LoginActivity;
import com.example.sharinghands.R;
import com.example.sharinghands.ui.Donor.DonorRegistration;
import com.example.sharinghands.ui.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Tasks;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import static android.view.View.GONE;

public class NGORegistration extends AppCompatActivity {

    Button ngo_register;
    EditText name, reg_no, email, address, password, confirm_password;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngoregistration);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        final String emailRegex = EmailRegex.getInstance().getEmailRegex();

        ngo_register = findViewById(R.id.ngo_register);
        name = findViewById(R.id.ngo_name);
        reg_no = findViewById(R.id.reg_no);
        email = findViewById(R.id.ngo_email_address);
        address = findViewById(R.id.address);
        password = findViewById(R.id.ngo_passcode);
        confirm_password = findViewById(R.id.ngo_confirm_password);
        progressBar = findViewById(R.id.progress_circular_ngo);

        firebaseAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        ngo_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String ngo_name = name.getText().toString();
                final String reg_number = reg_no.getText().toString();
                final String emailAddress = email.getText().toString();
                final String ngoAddress = address.getText().toString();
                String ngoPassword = password.getText().toString();
                String confirmPassword = confirm_password.getText().toString();

                if (ngo_name.isEmpty() || reg_number.isEmpty() || emailAddress.isEmpty()
                        || ngoAddress.isEmpty() || ngoPassword.isEmpty() || confirmPassword.isEmpty()){

                    Toast.makeText(getApplicationContext(),"All Fields Are Required!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if (emailAddress.matches(emailRegex)){
                        if (ngoPassword.equals(confirmPassword))
                        {
                            progressBar.setVisibility(View.VISIBLE);

                            firebaseAuth.createUserWithEmailAndPassword(emailAddress, ngoPassword)
                                    .addOnCompleteListener(NGORegistration.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            progressBar.setVisibility(GONE);

                                            if (task.isSuccessful()) {

                                                NGOModel ngoModel = new NGOModel(ngo_name,emailAddress,ngoAddress,Integer.parseInt(reg_number));

                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                String userID = user.getUid();

                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(ngo_name).build();

                                                user.updateProfile(profileUpdates);

                                                mDatabase = FirebaseDatabase.getInstance().getReference();

                                                mDatabase.child("NGOs").child(userID).setValue(ngoModel);

                                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                    NGORegistration.this);
                                                            alertDialogBuilder.setTitle("Info!");

                                                            alertDialogBuilder.setMessage("Verification Link Has Been Sent To Your Email Address! Please Verify")
                                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            Intent intent = new Intent(NGORegistration.this, NGOLoginFragment.class);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                    });

                                                            AlertDialog alertDialog = alertDialogBuilder.create();
                                                            alertDialog.show();

                                                        }else
                                                        {
                                                            Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                            } else {
                                                Toast.makeText(getApplicationContext(), "Registration Failed!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                        else
                            Toast.makeText(getApplicationContext(),"Password Did not Match!", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getApplicationContext(),"Invalid Email Address!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
