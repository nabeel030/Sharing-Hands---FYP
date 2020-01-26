package com.example.sharinghands.ui.NGO;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sharinghands.DonorHome;
import com.example.sharinghands.NGODashboard;
import com.example.sharinghands.R;
import com.example.sharinghands.ui.Donor.DonorRegistration;

public class NGOLoginFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_ngologin, container, false);
        final EditText email = root.findViewById(R.id.ngo_email);
        final EditText password = root.findViewById(R.id.ngo_password);
        final Button btn = root.findViewById(R.id.ngo_login);
        final TextView register_link = root.findViewById(R.id.ngo_register_link);
        dashboardViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String emailaddress = email.getText().toString();
                        String passcode = password.getText().toString();

                        if (emailaddress.isEmpty() || passcode.isEmpty()) {

                            Toast.makeText(getContext(), "Both Fields Are Required!", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            if (emailaddress.equals("na@gmail.com") && passcode.equals("123")) {
                                Intent intent = new Intent(getContext(), NGODashboard.class);
                                startActivity(intent);
                                (getActivity()).overridePendingTransition(0, 0);
                            }

                            else {
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
                            }
                        }
                    }
                });

                register_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getContext(), NGORegistration.class);
                        startActivity(intent);
                        (getActivity()).overridePendingTransition(0, 0);
                    }
                });
            }
        });
        return root;
    }
}