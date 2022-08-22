package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.comment;

import java.util.ArrayList;
import java.util.List;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.viewHolder> {
    private OnItemClickListener mListener;
    private List<comment> comments;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView commentText;
        public ImageView userPic;
        public TextView userName;


        public viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            commentText = itemView.findViewById(R.id.comment);
            userPic = itemView.findViewById(R.id.userPic);
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

    public commentAdapter(ArrayList<comment> comments) {
        this.comments = comments;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        String commentText = comments.get(position).getCommentText();

        holder.commentText.setText(commentText);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
