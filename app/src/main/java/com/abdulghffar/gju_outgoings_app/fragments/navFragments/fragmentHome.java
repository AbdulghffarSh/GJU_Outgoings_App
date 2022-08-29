package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

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
import com.abdulghffar.gju_outgoings_app.adapters.pinnedPostAdapter;

import com.abdulghffar.gju_outgoings_app.objects.post;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentHome extends Fragment {
    ArrayList<post> pinnedPostsArraylist;
    RecyclerView pinnedPostsRecyclerView;
    pinnedPostAdapter pinnedPostAdapter;

    FirebaseFirestore db;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_home, parent, false);
        pinnedPostsArraylist = new ArrayList<>();

        pinnedPostsRecyclerView = view.findViewById(R.id.pinnedPostsRecyclerView);
        pinnedPostAdapter = new pinnedPostAdapter(pinnedPostsArraylist);
        pinnedPostsRecyclerView.setHasFixedSize(true);
        pinnedPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        pinnedPostsRecyclerView.setAdapter(pinnedPostAdapter);
        getData();


        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    void getData() {

        db = FirebaseFirestore.getInstance();

        db.collection("PinnedPosts").orderBy("timeStamp", Query.Direction.ASCENDING)
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
                                pinnedPostsArraylist.add(dc.getDocument().toObject(post.class));
                            }


                        }
                        pinnedPostAdapter.notifyDataSetChanged();
                    }
                });


    }

}