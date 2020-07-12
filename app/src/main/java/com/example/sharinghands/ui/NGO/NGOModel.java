package com.example.sharinghands.ui.NGO;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NGOModel {
    private String ngo_title;
    private String email;
    private String address;
    private int reg_number;


    public NGOModel(){

    }

    public NGOModel(String ngo_title, String email, String address, int reg_number) {
        this.ngo_title = ngo_title;
        this.email = email;
        this.address = address;
        this.reg_number = reg_number;
    }

    public String getNgo_title() {
        return ngo_title;
    }

    public void setNgo_title(String ngo_title) {
        this.ngo_title = ngo_title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getReg_number() {
        return reg_number;
    }

    public void setReg_number(int reg_number) {
        this.reg_number = reg_number;
    }

    public synchronized boolean getNGOList(final String reg_number) {
        final boolean[] isVerified = {false};
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("RegisteredNGOs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    VerifiedNGOS verifiedNGOS = dataSnapshot1.getValue(VerifiedNGOS.class);

                    if (verifiedNGOS.getLicienceNo() == Integer.parseInt(reg_number)) {
                        isVerified[0] = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return isVerified[0];
    }

    public void Donate(String key, final int raisedAmount, final Context context) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Post").child(key);

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        final EditText edittext = new EditText(context);
        edittext.setInputType(InputType.TYPE_CLASS_NUMBER);
        edittext.setPadding(40, 0, 40, 0);
        alert.setTitle("Enter Amount");
        alert.setView(edittext);

        alert.setPositiveButton("Donate", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int donationAmount = Integer.parseInt(edittext.getText().toString());

                if (donationAmount < 20) {
                    Toast.makeText(context,"Minimum Amount that can be donated is Rs. 20", Toast.LENGTH_LONG).show();
                    return;
                }
                databaseReference.child("raised_amount").setValue(raisedAmount + donationAmount);

                Toast toast = Toast.makeText(context, "Thank you for your Donation!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        alert.show();

    }

}

