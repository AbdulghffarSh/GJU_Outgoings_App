package com.abdulghffar.gju_outgoings_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.city;

import java.util.ArrayList;
import java.util.List;

public class universitiesAdapter extends RecyclerView.Adapter<universitiesAdapter.viewHolder> {
    private final city cityData;
    private OnItemClickListener mListener;
    private final List<String> universityNames;
    private List<Object> universityRefs;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView universityNameField;


        public viewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            universityNameField = itemView.findViewById(R.id.universityName);


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

    public universitiesAdapter(city cityData) {
        this.cityData = cityData;
        universityNames = new ArrayList<>(cityData.getUniversities().keySet());
        universityRefs = new ArrayList<>(cityData.getUniversities().values());


    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.university_item, parent, false);
        viewHolder evh = new viewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        String currentItem = universityNames.get(position);

        holder.universityNameField.setText(currentItem);
    }

    @Override
    public int getItemCount() {
        return cityData.getUniversities().size();
    }
}
