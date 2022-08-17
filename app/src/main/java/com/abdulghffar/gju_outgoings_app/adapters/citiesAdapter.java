package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.city;

import java.util.ArrayList;

//public class citiesAdapter extends RecyclerView.Adapter<citiesAdapter.viewHolder>  {
//
//    ArrayList<city> cities;
//    Context context;
//
//    public citiesAdapter(ArrayList<city> cities, Context context) {
//        this.cities = cities;
//        this.context = context;
//    }
//
//
//    @NonNull
//    @Override
//    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
//
//        return new viewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
//
//        String cityName = cities.get(position).getCityName();
//        holder.cityNameField.setText(cityName);
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return cities.size();
//    }
//
//    public static class viewHolder extends RecyclerView.ViewHolder  {
//        TextView cityNameField;
//
//        public viewHolder(@NonNull View itemView) {
//            super(itemView);
//            cityNameField = itemView.findViewById(R.id.cityName);
//
//        }
//    }
//}


public class citiesAdapter extends RecyclerView.Adapter<citiesAdapter.viewHolder> {
    private ArrayList<city> citiesArrayList;
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

    public citiesAdapter(ArrayList<city> citiesArrayList) {
        this.citiesArrayList = citiesArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
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
