package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.authentication;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;
import com.abdulghffar.gju_outgoings_app.adapters.cityAdapter;
import com.abdulghffar.gju_outgoings_app.adapters.universityAdapter;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class fragmentCity extends Fragment {

    ArrayList<city> citiesData;
    ArrayList<String> universityNames;
    TextView cityName;
    ImageView cityPic;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_city, parent, false);
        Bundle bundle = this.getArguments();
        assert bundle != null;
        int position = bundle.getInt("cityPosition");
        navBarActivities navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;
        citiesData = navBarActivities.getCitiesArrayList();
        city cityData = citiesData.get(position);
        setData(cityData);

        universityNames = new ArrayList<>(cityData.getUniversities().keySet());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.universitiesRecyclerView);
        universityAdapter universityAdapter = new universityAdapter(universityNames);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(universityAdapter);




        return view;
    }

    private void setData(city cityData) {
        cityName = view.findViewById(R.id.cityName);
        cityPic = view.findViewById(R.id.img);

        cityName.setText(cityData.getCityName());
        if (cityData.getPics() != null) {
            Picasso.get().load(cityData.getPics().get(0)).into(cityPic);
        }


    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }


}