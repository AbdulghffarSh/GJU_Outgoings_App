package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.MainActivity;
import com.abdulghffar.gju_outgoings_app.adapters.commentAdapter;
import com.abdulghffar.gju_outgoings_app.objects.comment;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentCurrentPost extends Fragment {
    MainActivity MainActivity;
    post currentPost;
    ArrayList<comment> postCommentsArrayList;
    RecyclerView postCommentsRecyclerView;
    commentAdapter postCommentsAdapter;

    RecyclerView postsRecyclerView;
    com.abdulghffar.gju_outgoings_app.adapters.postAdapter postAdapter;

    FirebaseFirestore db;

    //UI
    TextView postTitle;
    TextView userName;
    TextView postBody;
    TextView timeStamp;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_current_post, parent, false);

        MainActivity = (MainActivity) getActivity();
        assert MainActivity != null;
        currentPost = MainActivity.getCurrentPost();

        postCommentsArrayList = new ArrayList<>();

        postCommentsRecyclerView = view.findViewById(R.id.postCommentsRecyclerView);
        postCommentsAdapter = new commentAdapter(postCommentsArrayList);
        postCommentsRecyclerView.setHasFixedSize(true);
        postCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        postCommentsRecyclerView.setAdapter(postCommentsAdapter);


//        getData();
        setup();

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    void setup() {
        postTitle = view.findViewById(R.id.postTitle);
        userName = view.findViewById(R.id.userName);
        postBody = view.findViewById(R.id.postBody);
        timeStamp = view.findViewById(R.id.postTimeStamp);

        postTitle.setText(currentPost.getTitle());
        userName.setText(currentPost.getUser().getName());
        postBody.setText(currentPost.getBody());
        timeStamp.setText(currentPost.getTimeStamp());

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
                                postCommentsArrayList.add(dc.getDocument().toObject(comment.class));
                            }


                        }
                        postCommentsAdapter.notifyDataSetChanged();
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
                                postCommentsArrayList.add(dc.getDocument().toObject(comment.class));
                            }


                        }
                        postAdapter.notifyDataSetChanged();
                    }
                });


    }

}