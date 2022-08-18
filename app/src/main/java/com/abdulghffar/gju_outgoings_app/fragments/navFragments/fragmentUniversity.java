package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

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
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;
import com.abdulghffar.gju_outgoings_app.adapters.universityAdapter;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.abdulghffar.gju_outgoings_app.objects.university;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentUniversity extends Fragment {

    TextView universityName;
    TextView universityNote;
    TextView cityName;

    university universityData;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_university, parent, false);
        navBarActivities navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;
        universityData = navBarActivities.getUniversity();
        setUpUI();

        return view;
    }

    private void setUpUI() {

        universityName = view.findViewById(R.id.universityName);
        universityNote = view.findViewById(R.id.noteField);
        cityName = view.findViewById(R.id.cityNameField);

        universityName.setText(universityData.getUniversityName());
        universityNote.setText(universityData.getNote());
        cityName.setText(universityData.getCityName());
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }


}