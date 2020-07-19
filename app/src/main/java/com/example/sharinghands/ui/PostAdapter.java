package com.example.sharinghands.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.sharinghands.R;
import com.example.sharinghands.SinglePost;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

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
    public void onBindViewHolder(final @NonNull PostViewHolder holder, int position) {

        Post current_post = mposts.get(position);

        String BaseUrl = "gs://sharing-hands.appspot.com";
        StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BaseUrl);

        storageReference.child("logos/"+current_post.getNgo_id()+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri.toString())
                        .into(holder.img_logo);
            }
        });

        storageReference.child("Posts/Images/"+current_post.getPostKey()+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context)
                        .load(uri.toString())
                        .into(holder.img_post);
            }
        });


        holder.NGO_title.setText(current_post.getNgo_title());
        holder.post_title.setText(current_post.getPost_title());
        holder.post_detail.setText(current_post.getPost_details());
        holder.amount_raised.setText(new StringBuilder("Rs: ").append(current_post.getRaised_amount()));
        holder.amount_required.setText(new StringBuilder("Rs: ").append(current_post.getRequired_amount()));

    }

    @Override
    public int getItemCount() {
        return mposts.size();
    }

    public  class  PostViewHolder extends RecyclerView.ViewHolder {

        ImageView img_post;
        CircleImageView img_logo;
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

            donate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Post selectedPost = mposts.get(position);
                    selectedPost.Donate(selectedPost.getPostKey(), selectedPost.getRaised_amount(), context);
                }
            });

            view_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.getContext().startActivity(new Intent(itemView.getContext(), SinglePost.class));
                }
            });
        }
    }
}
