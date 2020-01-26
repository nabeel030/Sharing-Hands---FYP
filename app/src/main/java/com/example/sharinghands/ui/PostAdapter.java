package com.example.sharinghands.ui;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharinghands.ChangePassword;
import com.example.sharinghands.R;
import com.example.sharinghands.SinglePost;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<Post> mposts;
    public Context context;

    public PostAdapter(ArrayList<Post> posts,Context ctx){
        this.mposts = posts;
        context = ctx;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post current_post = mposts.get(position);
        holder.img_logo.setImageResource(current_post.getSrc_image_logo());
        holder.img_post.setImageResource(current_post.getSrc_img_post());
        holder.NGO_title.setText(current_post.getNgo_title());
        holder.post_title.setText(current_post.getPost_title());
        holder.amount_raised.setText(new StringBuilder("Rs: ").append(current_post.getRaised_amount()));
        
    }

    @Override
    public int getItemCount() {
        return mposts.size();
    }

    public static class  PostViewHolder extends RecyclerView.ViewHolder {

        ImageView img_logo, img_post;
        TextView NGO_title;
        TextView post_title;
        TextView post_detail;
        TextView amount_raised;
        TextView amount_required;
        Button donate, view_detail;
        RelativeLayout relativeLayout;

        public PostViewHolder(final View itemView) {
            super(itemView);

            img_logo = itemView.findViewById(R.id.ngo_logo);
            img_post = itemView.findViewById((R.id.post_image));
            NGO_title = itemView.findViewById(R.id.ngo_title);
            post_title = itemView.findViewById(R.id.post_title);
            post_detail = itemView.findViewById(R.id.post_details);
            amount_raised = itemView.findViewById(R.id.amount_raised);
            amount_required = itemView.findViewById(R.id.amount_required);
            donate = itemView.findViewById(R.id.donate_btn);
            view_detail = itemView.findViewById(R.id.post_details_btn);
            relativeLayout = itemView.findViewById(R.id.post_item_r_layout);

            view_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), SinglePost.class));
                }
            });
        }
    }
}
