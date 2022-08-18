package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.city;

import java.util.ArrayList;

public class cityAdapter extends RecyclerView.Adapter<cityAdapter.viewHolder> {
    private final ArrayList<city> citiesArrayList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView cityNameFiled;


        public viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            cityNameFiled = itemView.findViewById(R.id.cityName);


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

    public cityAdapter(ArrayList<city> citiesArrayList) {
        this.citiesArrayList = citiesArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        city currentItem = citiesArrayList.get(position);

        holder.cityNameFiled.setText(currentItem.getCityName());
    }

    @Override
    public int getItemCount() {
        return citiesArrayList.size();
    }
}
