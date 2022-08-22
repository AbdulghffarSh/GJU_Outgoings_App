package com.abdulghffar.gju_outgoings_app.adapters;

import android.media.Image;
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

import java.util.ArrayList;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.viewHolder> {
    private final ArrayList<comment> commentArrayList;
    private commentAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(commentAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView comment;
        public ImageView userPic;


        public viewHolder(View itemView, final commentAdapter.OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            comment = itemView.findViewById(R.id.comment);
            userPic = itemView.findViewById(R.id.userPic);



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
