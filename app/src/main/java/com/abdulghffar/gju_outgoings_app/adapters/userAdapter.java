package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.user;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.viewHolder> {
    private final ArrayList<user> usersArraylist;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void setAsModeratorButton(int position);

        void acceptButton(int position);

        void rejectButton(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView userName, userEmail, userMajor;
        public ImageView userGender, acceptButton, rejectButton, setAsModeratorButton;


        public viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userEmail = itemView.findViewById(R.id.userEmail);
            userMajor = itemView.findViewById(R.id.userMajor);
            userGender = itemView.findViewById(R.id.userGender);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            rejectButton = itemView.findViewById(R.id.rejectButton);
            setAsModeratorButton = itemView.findViewById(R.id.setAsModerator);


            setAsModeratorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.setAsModeratorButton(position);
                        }
                    }
                }
            });

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.acceptButton(position);
                        }
                    }
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.rejectButton(position);
                        }
                    }
                }
            });


        }
    }

    public userAdapter(ArrayList<user> usersArraylist) {
        this.usersArraylist = usersArraylist;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_user_item, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        user currentItem = usersArraylist.get(position);
        holder.userName.setText(currentItem.getName());
        holder.userEmail.setText(currentItem.getEmail());
        holder.userMajor.setText(currentItem.getMajor());
        if (currentItem.getGender().equals("Female")) {
            holder.userGender.setImageResource(R.drawable.ic_female);
        }

    }

    @Override
    public int getItemCount() {
        return usersArraylist.size();
    }


}
