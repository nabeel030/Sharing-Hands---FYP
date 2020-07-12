package com.example.sharinghands.ui.NGO;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.Tasks;


public class VerifiedNGOS {
    private String name;
    private int licienceNo;

    public VerifiedNGOS() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLicienceNo() {
        return licienceNo;
    }

    public void setLicienceNo(int licienceNo) {
        this.licienceNo = licienceNo;
    }

    public boolean getNGOList(final int reg_number) {
        final boolean[] isVerified = {false};
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("RegisteredNGOs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    VerifiedNGOS verifiedNGOS = dataSnapshot1.getValue(VerifiedNGOS.class);

                    if (verifiedNGOS.getLicienceNo() == reg_number) {
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
}
