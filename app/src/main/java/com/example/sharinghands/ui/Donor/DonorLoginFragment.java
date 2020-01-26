package com.example.sharinghands.ui.Donor;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sharinghands.DonorHome;
import com.example.sharinghands.R;
import com.example.sharinghands.SinglePost;
import com.example.sharinghands.ui.gallery.GalleryFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class DonorLoginFragment extends Fragment {

    private HomeViewModel homeViewModel;

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if (firebaseAuth.getCurrentUser() != null) {
            // User is logged in
            startActivity(new Intent(getActivity(), DonorHome.class));
            getActivity().finish();
        }
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_donorlogin, container, false);
        final EditText email = root.findViewById(R.id.donor_email);
        final EditText password = root.findViewById(R.id.donor_password);
        final Button login = root.findViewById(R.id.donor_login);
        final TextView register_link = root.findViewById(R.id.donor_register_link);
        final ProgressBar progressBar = root.findViewById(R.id.progress_circular);

        homeViewModel.getText().observe(this, new Observer<String>() {
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
                                                Intent intent = new Intent(getActivity(), DonorHome.class);
                                                startActivity(intent);
                                                getActivity().finish();
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