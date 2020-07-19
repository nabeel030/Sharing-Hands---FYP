package com.example.sharinghands.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Post {

    private String ngo_id;
    private String ngo_title;
    private String post_title;
    private String post_details;
    private int src_image_logo;
    private int src_img_post;
    private int raised_amount;
    private int required_amount;
    private String key;
    public Post() {
    }

    public Post(String ngo_id,int src_image_logo, String ngo_title, String post_title, String post_details, int src_img_post, int raised_amount, int remaining_amount) {
        this.ngo_id = ngo_id;
        this.src_image_logo = src_image_logo;
        this.ngo_title = ngo_title;
        this.post_title = post_title;
        this.post_details = post_details;
        this.src_img_post = src_img_post;
        this.raised_amount = raised_amount;
        this.required_amount = remaining_amount;
    }

    public String getNgo_id() {
        return ngo_id;
    }

    public void setNgo_id(String ngo_id) {
        this.ngo_id = ngo_id;
    }

    public int getSrc_image_logo() {
        return src_image_logo;
    }

    public void setSrc_image_logo(int src_image_logo) {
        this.src_image_logo = src_image_logo;
    }

    public String getNgo_title() {
        return ngo_title;
    }

    public void setNgo_title(String ngo_title) {
        this.ngo_title = ngo_title;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_details() {
        return post_details;
    }

    public void setPost_details(String post_details) {
        this.post_details = post_details;
    }

    public int getSrc_img_post() {
        return src_img_post;
    }

    public void setSrc_img_post(int src_img_post) {
        this.src_img_post = src_img_post;
    }

    public int getRaised_amount() {
        return raised_amount;
    }

    public void setRaised_amount(int raised_amount) {
        this.raised_amount = raised_amount;
    }

    public int getRequired_amount() {
        return required_amount;
    }

    public void setRequired_amount(int required_amount) {
        this.required_amount = required_amount;
    }

    public void setPostKey(String key) {
        this.key = key;
    }

    public String getPostKey() {
        return key;
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


