package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.db;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;
import com.abdulghffar.gju_outgoings_app.adapters.staffAdapter;
import com.abdulghffar.gju_outgoings_app.objects.staff;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentContactUs extends Fragment {

    RecyclerView recyclerView;
    staffAdapter staffAdapter;
    ArrayList<staff> staffArrayList;

    navBarActivities navBarActivities;


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_contact_us, parent, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        staffArrayList = new ArrayList<staff>();
        staffAdapter = new staffAdapter(staffArrayList);

        recyclerView.setAdapter(staffAdapter);
        navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;


        staffAdapter.setOnItemClickListener(new staffAdapter.OnItemClickListener() {
            @Override
            public void emailOnClick(int position) {
                staff selectedItem = staffArrayList.get(position);
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", selectedItem.getEmail(), null));
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));

            }
        });

        EventChangeListener();

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }


    private void EventChangeListener() {
        navBarActivities.loadingUI(1);

        db.collection("Staff").orderBy("name", Query.Direction.ASCENDING)
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
                                staffArrayList.add(dc.getDocument().toObject(staff.class));
                            }

                            staffAdapter.notifyDataSetChanged();

                        }
                        navBarActivities.loadingUI(0);

                    }

                });

    }


}