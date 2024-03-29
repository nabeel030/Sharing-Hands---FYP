package com.example.sharinghands.ui.Donor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sharinghands.DonorHome;
import com.example.sharinghands.ForgetPassword;
import com.example.sharinghands.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class DonorLoginFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private SharedPreferences sharedpreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_donorlogin, container, false);
        final EditText email = root.findViewById(R.id.donor_email);
        final EditText password = root.findViewById(R.id.donor_password);
        final Button login = root.findViewById(R.id.donor_login);
        final TextView register_link = root.findViewById(R.id.donor_register_link);
        final ProgressBar progressBar = root.findViewById(R.id.progress_circular);
        final TextView forget_password = root.findViewById(R.id.forget_password_donor);

        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ForgetPassword.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        sharedpreferences = requireActivity().getSharedPreferences("login_session", Context.MODE_PRIVATE);

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String emailaddress = email.getText().toString();
                        final String passcode = password.getText().toString();

                        if (emailaddress.isEmpty() || passcode.isEmpty()) {

                            Toast.makeText(getContext(), "Both Fields Are Required!", Toast.LENGTH_SHORT).show();
                        } else {

                            progressBar.setVisibility(View.VISIBLE);

                            firebaseAuth.signInWithEmailAndPassword(emailaddress, passcode)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {

                                            progressBar.setVisibility(View.GONE);

                                            if (!task.isSuccessful()) {

                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        getContext());
                                                alertDialogBuilder.setTitle("Error!");

                                                alertDialogBuilder.setMessage("Given credentials did not Match!")
                                                        .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {

                                                            }
                                                        });

                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                alertDialog.show();



                                            }else {

                                                if (firebaseAuth.getCurrentUser().isEmailVerified()) {
                                                    if (firebaseAuth.getCurrentUser().getDisplayName().equals("donor")) {
                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                        editor.putString("status", "donor");
                                                        editor.apply();

                                                        Intent intent = new Intent(getActivity(), DonorHome.class);
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                    } else if (firebaseAuth.getCurrentUser().getDisplayName().equals("ngo")){
                                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                                getContext());
                                                        alertDialogBuilder.setTitle("Info!");

                                                        alertDialogBuilder.setMessage("This email address is registered as NGO account. Kindly Login as NGO!")
                                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                    }
                                                                });

                                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                                        alertDialog.show();
                                                    }
                                                }else{
                                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                            getContext());
                                                    alertDialogBuilder.setTitle("Info!");

                                                    alertDialogBuilder.setMessage("Please Verify Your Email Address Before Login!")
                                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            });

                                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();
                                                }
                                            }

                                        }
                                    });
                        }
                    }
                });

                register_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), DonorRegistration.class);
                        startActivity(intent);
                        (getActivity()).overridePendingTransition(0, 0);
                    }
                });

            }
        });
        return root;
    }
}