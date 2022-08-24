package com.abdulghffar.gju_outgoings_app.fragments.Messeging;

import android.annotation.SuppressLint;
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
import com.abdulghffar.gju_outgoings_app.adapters.userAdapter;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentChat extends Fragment {

//    RecyclerView recyclerView;
//    userAdapter userAdapter;
//    ArrayList<user> userArrayList;
    FirebaseFirestore db;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_chat, parent, false);
//
//        recyclerView = view.findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = FirebaseFirestore.getInstance();
//        userArrayList = new ArrayList<>();
//        userAdapter = new userAdapter(userArrayList);
//
//        recyclerView.setAdapter(userAdapter);

        

//        EventChangeListener();




        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }


//    private void EventChangeListener() {
//
//        db.collection("Users").orderBy("name", Query.Direction.ASCENDING)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @SuppressLint("NotifyDataSetChanged")
//                    @Override
//                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
//                        if (error != null) {
//                            Log.e("Firestore error", error.getMessage());
//                            return;
//                        }
//
//                        assert value != null;
//                        for (DocumentChange dc : value.getDocumentChanges()) {
//                            if (dc.getType() == DocumentChange.Type.ADDED) {
//                                userArrayList.add(dc.getDocument().toObject(user.class));
//                            }
//                            navBarActivities navBarActivities = (navBarActivities) getActivity();
//                            assert navBarActivities != null;
//                            userAdapter.notifyDataSetChanged();
//
//                        }
//
//                    }
//                });
//    }


}