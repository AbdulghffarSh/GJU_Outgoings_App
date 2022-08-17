package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;

import javax.annotation.Nullable;

public class fragmentFeatures extends Fragment {

    RelativeLayout citiesAndUniversitiesLayout;


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_features, parent, false);
        citiesAndUniversitiesLayout = view.findViewById(R.id.citiesAndUniversitiesLayout);
        citiesAndUniversitiesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), navBarActivities.class);
                String fragmentName = "Rating";
                intent.putExtra("fragmentName", fragmentName);
                startActivity(intent);

            }
        });


        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }


}