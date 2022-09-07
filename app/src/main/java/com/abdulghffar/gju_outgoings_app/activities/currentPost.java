package com.abdulghffar.gju_outgoings_app.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.adapters.commentAdapter;
import com.abdulghffar.gju_outgoings_app.objects.comment;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class currentPost extends AppCompatActivity {
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
    ImageView accountPic;
    ImageView postImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        // Defines the xml file for the fragment
        setContentView(R.layout.activity_fragment_current_post);


        currentPost = (post) getIntent().getSerializableExtra("currentPost");


        postCommentsArrayList = new ArrayList<>();

        postCommentsRecyclerView = findViewById(R.id.postCommentsRecyclerView);
        postCommentsAdapter = new commentAdapter(postCommentsArrayList);
        postCommentsRecyclerView.setHasFixedSize(true);
        postCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        postCommentsRecyclerView.setAdapter(postCommentsAdapter);


//        getData();
        setup();

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.

    void setup() {
        postTitle = findViewById(R.id.postTitle);
        userName = findViewById(R.id.userName);
        accountPic = findViewById(R.id.accountPic);
        postBody = findViewById(R.id.postBody);
        postImage = findViewById(R.id.postImage);
        timeStamp = findViewById(R.id.postTimeStamp);


        postTitle.setText(currentPost.getTitle());
        userName.setText(currentPost.getUser().getName());
        postBody.setText(currentPost.getBody());
        timeStamp.setText(currentPost.getTimeStamp());
        if (currentPost.getUser().getProfilePic() != null) {
            Picasso.get().load(currentPost.getUser().getProfilePic()).into(accountPic);
        }
        if (currentPost.getImage() != null) {
            Picasso.get().load(currentPost.getImage()).into(postImage);
        } else {
            ((ViewManager) postImage.getParent()).removeView(postImage);
        }


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