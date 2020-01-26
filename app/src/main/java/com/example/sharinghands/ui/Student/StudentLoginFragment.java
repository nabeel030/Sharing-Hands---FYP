package com.example.sharinghands.ui.Student;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.sharinghands.R;
import com.example.sharinghands.ui.NGO.NGORegistration;

public class StudentLoginFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_stdlogin, container, false);
        final EditText email = root.findViewById(R.id.std_email);
        final EditText password = root.findViewById(R.id.std_password);
        final Button login = root.findViewById(R.id.std_login);
        final TextView register_link = root.findViewById(R.id.std_register_link);
        notificationsViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();
                    }
                });

                register_link.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getContext(), StudentRegistration.class));
                        (getActivity()).overridePendingTransition(0, 0);
                    }
                });
            }
        });
        return root;
    }
}