package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.objects.city;

import java.util.HashMap;

import javax.annotation.Nullable;

public class fragmentCity extends Fragment {

    city cityData;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_city, parent, false);
        Bundle bundle = this.getArguments();
        assert bundle != null;
        if (bundle.getSerializable("HashMap") != null) {
            cityData = (city) bundle.getParcelable("city");

        }
        System.out.println("This is the message u asked for\n" + cityData.getCityName()+"\n"+cityData.getUniversities());

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }


}