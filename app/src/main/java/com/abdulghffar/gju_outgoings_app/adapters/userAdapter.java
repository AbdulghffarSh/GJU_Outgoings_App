package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class userAdapter extends RecyclerView.Adapter<userAdapter.viewHolder> {
    private final ArrayList<user> userArrayList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void sendMessage(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView userName;
        public TextView userRole;
        public ImageView userPic;


        public viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userRole = itemView.findViewById(R.id.userRole);
            userPic = itemView.findViewById(R.id.userPic);


        }
    }

    public userAdapter(ArrayList<user> userArrayList) {
        this.userArrayList = userArrayList;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        user currentItem = userArrayList.get(position);

        holder.userName.setText(currentItem.getName());
        holder.userRole.setText(currentItem.getRole());
        if (currentItem.getProfilePic() != null) {
            Picasso.get().load(currentItem.getProfilePic()).into(holder.userPic);
        }
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


}
