package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.staff;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class staffAdapter extends RecyclerView.Adapter<staffAdapter.viewHolder> {
    private final ArrayList<staff> staffArrayList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void emailOnClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView staffName;
        public TextView staffInfo;
        public ImageView staffPic;
        public ImageView emailButton;


        public viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            staffName = itemView.findViewById(R.id.name);
            staffInfo = itemView.findViewById(R.id.info);
            staffPic = itemView.findViewById(R.id.pic);
            emailButton = itemView.findViewById(R.id.email);

            emailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.emailOnClick(position);
                        }
                    }
                }
            });


        }
    }

    public staffAdapter(ArrayList<staff> staffArrayList) {
        this.staffArrayList = staffArrayList;

    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_item, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        staff currentItem = staffArrayList.get(position);

        holder.staffName.setText(currentItem.getName());
        holder.staffInfo.setText(currentItem.getInfo());
        if (currentItem.getProfilePic() != null) {
            Picasso.get().load(currentItem.getProfilePic()).rotate(0f).into(holder.staffPic);
        }
    }

    @Override
    public int getItemCount() {
        return staffArrayList.size();
    }


}
