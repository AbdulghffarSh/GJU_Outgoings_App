package com.abdulghffar.gju_outgoings_app.adapters;

import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.comment;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.viewHolder> {
    private final ArrayList<comment> commentArrayList;
    private commentAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void reportItemClick(int position);
    }

    public void setOnItemClickListener(commentAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView comment;
        public ImageView userPic;
        public ImageView reportButton;
        public TextView timeStamp;


        public viewHolder(View itemView, final commentAdapter.OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            comment = itemView.findViewById(R.id.comment);
            userPic = itemView.findViewById(R.id.userPic);
            reportButton = itemView.findViewById(R.id.reportButton);
            timeStamp = itemView.findViewById(R.id.timeStamp);


//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null) {
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            listener.reportItemClick(position);
//                        }
//                    }
//                }
//            });


            reportButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.reportItemClick(position);
                        }
                    }
                }
            });


        }
    }

    public commentAdapter(ArrayList<comment> commentArrayList) {
        this.commentArrayList = commentArrayList;
        System.out.println(commentArrayList.size());
    }

    @Override
    public commentAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        commentAdapter.viewHolder evh = new commentAdapter.viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        comment currentItem = commentArrayList.get(position);
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            Date parsedDate = dateFormat.parse(currentItem.getTimeStamp());
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm   dd/MM/yyyy");
            holder.timeStamp.setText(outputFormat.format(parsedDate).toString());
        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of exception
            System.out.println("this is the error " + e);
        }

        holder.userName.setText(currentItem.getUser().getName());
        holder.comment.setText(currentItem.getCommentText());
        if (currentItem.getUser().getProfilePic() != null) {
            Picasso.get().load(currentItem.getUser().getProfilePic()).into(holder.userPic);
        }

    }


    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }


}
