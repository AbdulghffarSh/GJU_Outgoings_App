package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class eventAdapter extends RecyclerView.Adapter<eventAdapter.viewHolder> {
    ArrayList<event> eventsArrayList;
    private OnItemClickListener mListener;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm aa", Locale.US);


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView eventNameField;
        TextView eventStartDateField;
        TextView eventStartTimeField;


        public viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            eventNameField = itemView.findViewById(R.id.eventName);
            eventStartDateField = itemView.findViewById(R.id.eventStartDate);
            eventStartTimeField = itemView.findViewById(R.id.eventStartTime);


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

    public eventAdapter(ArrayList<event> eventsArrayList) {
        this.eventsArrayList = eventsArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        event currentItem = eventsArrayList.get(position);
        holder.eventNameField.setText(currentItem.getTitle());
        holder.eventStartDateField.setText(dateFormatter.format(currentItem.getStartDate()));
        holder.eventStartTimeField.setText(timeFormatter.format(currentItem.getStartDate()));


    }

    @Override
    public int getItemCount() {
        return eventsArrayList.size();
    }


}
