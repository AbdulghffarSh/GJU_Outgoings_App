package com.abdulghffar.gju_outgoings_app.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;
import com.abdulghffar.gju_outgoings_app.adapters.citiesAdapter;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentCity;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentRating extends Fragment {

    RecyclerView recyclerView;
    citiesAdapter citiesAdapter;
    ArrayList<city> citiesArrayList;
    FirebaseFirestore db;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_rating, parent, false);


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = FirebaseFirestore.getInstance();
        citiesArrayList = new ArrayList<city>();
        citiesAdapter = new citiesAdapter(citiesArrayList);

        recyclerView.setAdapter(citiesAdapter);
        citiesAdapter.setOnItemClickListener(new citiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");

            }
        });

        EventChangeListener();


        return view;
    }

    private void EventChangeListener() {

        db.collection("Cities").orderBy("cityName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        assert value != null;
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                citiesArrayList.add(dc.getDocument().toObject(city.class));
                            }
                            citiesAdapter.notifyDataSetChanged();
                        }

                    }
                });



    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    public void changeItem(int position, String text) {

        navBarActivities navBarActivities = (navBarActivities) getActivity();
        fragmentCity fragmentCity = new fragmentCity();
        Bundle bundle = new Bundle();
        bundle.putSerializable("city", citiesArrayList.get(position));
        fragmentCity.setArguments(bundle);
        assert navBarActivities != null;
        navBarActivities.replaceFragment(fragmentCity);


    }


}