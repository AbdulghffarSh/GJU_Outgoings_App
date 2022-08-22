package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import static android.content.ContentValues.TAG;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class fragmentCity extends Fragment {

    ArrayList<String> universityNames;

    TextView cityName;
    ImageView cityPic;
    ImageView addComment;

    FirebaseDatabase db;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_city, parent, false);

        navBarActivities navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;
        city cityData = navBarActivities.getCityData();
        setData(cityData);

        universityNames = new ArrayList<>(cityData.getUniversities().keySet());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.universitiesRecyclerView);
        universityAdapter universityAdapter = new universityAdapter(universityNames);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(universityAdapter);

        universityAdapter.setOnItemClickListener(new universityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");

            }
        });

        addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getComments();
            }
        });


        return view;
    }

    private void setData(city cityData) {
        cityName = view.findViewById(R.id.cityName);
        cityPic = view.findViewById(R.id.img);
        addComment = view.findViewById(R.id.sendButton);

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

    public void changeItem(int position, String text) {

        navBarActivities navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;
        navBarActivities.setUniversity(position);
        fragmentUniversity fragmentUniversity = new fragmentUniversity();
        navBarActivities.replaceFragment(fragmentUniversity);


    }

    void getComments(){
        db = FirebaseDatabase.getInstance("https://gju-outgings-app-24c61-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = db.getReference("Cities/Aalen/Comments");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue(Object.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }




}