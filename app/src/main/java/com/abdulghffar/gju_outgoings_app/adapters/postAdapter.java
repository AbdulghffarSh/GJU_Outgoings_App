package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.bumptech.glide.Glide;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class postAdapter extends RecyclerView.Adapter<postAdapter.viewHolder> {
    private final ArrayList<post> postsArrayList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView postTitle;
        public TextView postBody;
        public TextView userName;
        public TextView postTimeStamp;
        public ImageView accountPic;
        public ImageView postImage;


        public viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            postTitle = itemView.findViewById(R.id.postTitle);
            postBody = itemView.findViewById(R.id.postBody);
            postTimeStamp = itemView.findViewById(R.id.postTimeStamp);
            accountPic = itemView.findViewById(R.id.accountPic);
            postImage = itemView.findViewById(R.id.postImage);
            userName = itemView.findViewById(R.id.userName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }

    public postAdapter(ArrayList<post> postsArrayList) {
        this.postsArrayList = postsArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        // Sort the postsArrayList by timestamp
        Collections.sort(postsArrayList, (post1, post2) -> post2.getTimeStamp().compareTo(post1.getTimeStamp()));


        // Get the current item at the specified position
        post currentItem = postsArrayList.get(position);

        try {
            // Convert the timestamp to a Date object
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            Date parsedDate = dateFormat.parse(currentItem.getTimeStamp());
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm   dd/MM/yyyy");
            holder.postTimeStamp.setText(outputFormat.format(parsedDate));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        // Set the post title and body
        String textBody, textTitle;
        if (currentItem.getTitle().length() > 30) {
            textTitle = currentItem.getTitle().substring(0, 30) + "...";
        } else {
            textTitle = currentItem.getTitle();
        }
        holder.postTitle.setText(textTitle);
        if (currentItem.getBody().length() > 50) {
            textBody = currentItem.getBody().substring(0, 50) + "...";
        } else {
            textBody = currentItem.getBody();
        }
        holder.postBody.setText(textBody);

        // Set the post image if it exists
        if (currentItem.getImage() != null) {
            holder.postImage.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView)
                    .load(currentItem.getImage())
                    .into(holder.postImage);
        }

        // Set the user's profile picture and name
        if (currentItem.getUser() != null) {
            Glide.with(holder.itemView)
                    .load(currentItem.getUser().getProfilePic())
                    .into(holder.accountPic);
            holder.userName.setText(currentItem.getUser().getName());
        }
    }


    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }


}
