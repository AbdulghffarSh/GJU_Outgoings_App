package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;
import com.abdulghffar.gju_outgoings_app.adapters.cityAdapter;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentRating extends Fragment {

    RecyclerView recyclerView;
    cityAdapter citiesAdapter;
    ArrayList<city> citiesArrayList;
    FirebaseFirestore db;
    navBarActivities navBarActivities;

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
        citiesAdapter = new cityAdapter(citiesArrayList);
        navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;

        recyclerView.setAdapter(citiesAdapter);
        citiesAdapter.setOnItemClickListener(new cityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");

            }
        });

        EventChangeListener();


        return view;
    }

    private void EventChangeListener() {
        navBarActivities.progressBarStatus(true);
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

                            navBarActivities.setCitiesArrayList(citiesArrayList);
                            citiesAdapter.notifyDataSetChanged();
                            navBarActivities.progressBarStatus(false);
                        }

                    }
                });


    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    public void changeItem(int position, String text) {

        navBarActivities navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;
        navBarActivities.setCitiesArrayList(citiesArrayList);
        fragmentCity fragmentCity = new fragmentCity();
        navBarActivities.setCityData(citiesArrayList.get(position));
        navBarActivities.replaceFragment(fragmentCity);


    }


}