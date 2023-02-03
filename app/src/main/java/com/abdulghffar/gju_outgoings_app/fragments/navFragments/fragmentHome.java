package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.MainActivity;
import com.abdulghffar.gju_outgoings_app.activities.currentPost;
import com.abdulghffar.gju_outgoings_app.adapters.postAdapter;
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
    com.abdulghffar.gju_outgoings_app.adapters.postAdapter pinnedPostAdapter;
    MainActivity MainActivity;

    ArrayList<post> postsArraylist;
    RecyclerView postsRecyclerView;
    com.abdulghffar.gju_outgoings_app.adapters.postAdapter postAdapter;

    FirebaseFirestore db;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_home, parent, false);
        pinnedPostsArraylist = new ArrayList<>();
        postsArraylist = new ArrayList<>();
        MainActivity=(MainActivity) getActivity();
        assert MainActivity != null;

        pinnedPostsRecyclerView = view.findViewById(R.id.pinnedPostsRecyclerView);
        pinnedPostAdapter = new postAdapter(pinnedPostsArraylist);
        pinnedPostsRecyclerView.setHasFixedSize(true);
        pinnedPostsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        pinnedPostsRecyclerView.setAdapter(pinnedPostAdapter);


        postsRecyclerView = view.findViewById(R.id.postsRecyclerView);
        postAdapter = new postAdapter(postsArraylist);
        postsRecyclerView.setHasFixedSize(true);
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        postsRecyclerView.setAdapter(postAdapter);

        postAdapter.setOnItemClickListener(new postAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItemPost(position, "Clicked");

            }
        });
        pinnedPostAdapter.setOnItemClickListener(new postAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItemPinnedPost(position, "Clicked");

            }
        });


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
        MainActivity.loadingUI(1);
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
                        MainActivity.loadingUI(0);
                    }

                });
        db.collection("Posts").orderBy("timeStamp", Query.Direction.ASCENDING)
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
                                postsArraylist.add(dc.getDocument().toObject(post.class));
                            }


                        }
                        postAdapter.notifyDataSetChanged();
                        MainActivity.loadingUI(0);
                    }
                });


    }

    public void changeItemPost(int position, String text) {

        Intent intent = new Intent(getActivity(), currentPost.class);
        intent.putExtra("currentPost", postsArraylist.get(position));
        startActivity(intent);


    }

    public void changeItemPinnedPost(int position, String text) {

        Intent intent = new Intent(getActivity(), currentPost.class);
        intent.putExtra("currentPost", pinnedPostsArraylist.get(position));
        startActivity(intent);


    }

}