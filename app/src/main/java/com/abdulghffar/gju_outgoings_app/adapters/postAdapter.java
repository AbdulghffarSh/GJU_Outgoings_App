package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class postAdapter extends RecyclerView.Adapter<postAdapter.viewHolder> {
    private final ArrayList<post> postsArrayList;
    private OnItemClickListener mListener;
    int layoutItem;


    public interface OnItemClickListener {
        void emailOnClick(int position);
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
//            emailButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.emailOnClick(position);
//                        }
//                    }
//                }
//            });


        }
    }

    public postAdapter(ArrayList<post> postsArrayList, int layoutItem) {
        this.postsArrayList = postsArrayList;
        this.layoutItem = layoutItem;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutItem, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        post currentItem = postsArrayList.get(position);
        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            Date parsedDate = dateFormat.parse(currentItem.getTimeStamp());
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm   dd/MM/yyyy");
            holder.postTimeStamp.setText(outputFormat.format(parsedDate));


        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of exception
            System.out.println("this is the error " + e);
        }

        holder.postTitle.setText(currentItem.getTitle());
        holder.postBody.setText(currentItem.getBody());
        if (currentItem.getImage() != null) {
            Picasso.get().load(currentItem.getImage()).into(holder.postImage);
        }
        else{
            ((ViewManager)holder.postImage.getParent()).removeView(holder.postImage);

        }

        if (currentItem.getUser() != null) {
            System.out.println(currentItem.getUser().getName());
            Picasso.get().load(currentItem.getUser().getProfilePic()).into(holder.accountPic);
            holder.userName.setText(currentItem.getUser().getName());

        }

    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }


}
