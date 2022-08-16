package com.abdulghffar.gju_outgoings_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.city;

import java.util.ArrayList;

public class citiesAdapter extends RecyclerView.Adapter<citiesAdapter.viewHolder> {

    ArrayList<city> cities;
    Context context;

    public citiesAdapter(ArrayList<city> cities, Context context) {
        this.cities = cities;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        String cityName = cities.get(position).getCityName();
        holder.cityNameField.setText(cityName);

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView cityNameField;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            cityNameField = itemView.findViewById(R.id.cityName);

        }
    }
}
