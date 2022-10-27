package com.abdulghffar.gju_outgoings_app.admin.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.report;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class reportsAdapter extends RecyclerView.Adapter<reportsAdapter.viewHolder> {
    private final ArrayList<report> reportsArrayList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void OnItemClickListener(reportsAdapter.OnItemClickListener listener) {
        mListener = listener;
    }


    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView reportTitle;
        public TextView userName;
        public TextView postTimeStamp;
        public ImageView accountPic;
        public ImageView deleteCommentButton;
        public ImageView discardButton;


        public viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            reportTitle = itemView.findViewById(R.id.reportTitle);
            postTimeStamp = itemView.findViewById(R.id.postTimeStamp);
            accountPic = itemView.findViewById(R.id.accountPic);
            userName = itemView.findViewById(R.id.userName);
            deleteCommentButton = itemView.findViewById(R.id.deleteButton);
            discardButton = itemView.findViewById(R.id.discard);

            discardButton.setOnClickListener(new View.OnClickListener() {
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

            deleteCommentButton.setOnClickListener(new View.OnClickListener() {
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

    public reportsAdapter(ArrayList<report> reportsArrayList) {
        this.reportsArrayList = reportsArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        report currentItem = reportsArrayList.get(position);
        holder.userName.setText(currentItem.getUsers().size() + "...");

        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            Date parsedDate = dateFormat.parse(currentItem.getTimeStamp());
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm   dd/MM/yyyy");
            holder.postTimeStamp.setText(outputFormat.format(parsedDate));


        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of exception
            System.out.println("this is the error " + e);
        }

        if (currentItem.getCommentText().length() > 30) {
            holder.reportTitle.setText(currentItem.getCommentText().substring(0, 30) + "...");
        } else {
            holder.reportTitle.setText(currentItem.getCommentText());
        }


    }

    @Override
    public int getItemCount() {
        return reportsArrayList.size();
    }


}
