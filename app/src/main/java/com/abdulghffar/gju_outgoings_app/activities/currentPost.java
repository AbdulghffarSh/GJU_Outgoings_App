package com.abdulghffar.gju_outgoings_app.activities;

import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.db;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.mAuth;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.adapters.commentAdapter;
import com.abdulghffar.gju_outgoings_app.database.firebaseDb;
import com.abdulghffar.gju_outgoings_app.objects.comment;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class currentPost extends AppCompatActivity {
    post currentPost;
    ArrayList<comment> postCommentsArrayList;
    RecyclerView postCommentsRecyclerView;
    commentAdapter postCommentsAdapter;

    com.abdulghffar.gju_outgoings_app.adapters.postAdapter postAdapter;


    FirebaseDatabase realTimeDB = firebaseDb.realDb;



    //UI
    TextView postTitle;
    TextView userName;
    TextView postBody;
    TextView timeStamp;
    ImageView accountPic;
    ImageView postImage;
    ImageView addCommentButton;
    ProgressBar progressBar;

    EditText commentField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        // Defines the xml file for the fragment
        setContentView(R.layout.activity_fragment_current_post);


        currentPost = (post) getIntent().getSerializableExtra("currentPost");


        postCommentsArrayList = new ArrayList<>();

        postCommentsRecyclerView = findViewById(R.id.postCommentsRecyclerView);
        postCommentsAdapter = new commentAdapter(postCommentsArrayList);
        postCommentsRecyclerView.setHasFixedSize(true);
        postCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        postCommentsRecyclerView.setAdapter(postCommentsAdapter);


//        getData();
        setup();

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });


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
        commentField = findViewById(R.id.commentField);
        addCommentButton = findViewById(R.id.addCommentButton);


        postTitle.setText(currentPost.getTitle());
        userName.setText(currentPost.getUser().getName());
        postBody.setText(currentPost.getBody());
        timeStamp.setText(currentPost.getTimeStamp());

        progressBar = findViewById(R.id.progressBar);


        try {

            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy");
            Date parsedDate = dateFormat.parse(currentPost.getTimeStamp());
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm   dd/MM/yyyy");
            timeStamp.setText(outputFormat.format(parsedDate));


        } catch (Exception e) { //this generic but you can control another types of exception
            // look the origin of exception
            System.out.println("this is the error " + e);
        }


        if (currentPost.getUser().getProfilePic() != null) {
            Picasso.get().load(currentPost.getUser().getProfilePic()).into(accountPic);
        }
        if (currentPost.getImage() != null) {
            Picasso.get().load(currentPost.getImage()).into(postImage);
        } else {
            ((ViewManager) postImage.getParent()).removeView(postImage);
        }

        getComments();
    }

//    void getData() {
//
//        db = FirebaseFirestore.getInstance();
//
//        db.collection("PinnedPosts").orderBy("timeStamp", Query.Direction.ASCENDING)
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
//                                postCommentsArrayList.add(dc.getDocument().toObject(comment.class));
//                            }
//
//
//                        }
//                        postCommentsAdapter.notifyDataSetChanged();
//                    }
//                });
//        db.collection("Posts").orderBy("timeStamp", Query.Direction.ASCENDING)
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
//                                postCommentsArrayList.add(dc.getDocument().toObject(comment.class));
//                            }
//
//
//                        }
//                        postAdapter.notifyDataSetChanged();
//                    }
//                });
//
//
//    }

    void getComments() {
        progressBar.setVisibility(View.VISIBLE);
        String ref = "/Posts/" + "/Users/" + currentPost.getUser().getUid() + "/UserPosts/" + currentPost.getPostID() + "/Comments";

        DatabaseReference myRef = realTimeDB.getReference(ref);
        postCommentsArrayList.clear();
        myRef
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String Uid = snapshot.child("uid").getValue(String.class);
                            String timeStamp = snapshot.child("timeStamp").getValue(String.class);
                            String comment = snapshot.child("commentText").getValue(String.class);
                            String ref = snapshot.child("reference").getValue(String.class);
                            System.out.println(comment);

                            DocumentReference docRef = db.collection("Users").document(Uid);
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    user user = documentSnapshot.toObject(user.class);
                                    comment commentObject = new comment(comment, Uid, timeStamp, user, ref);
                                    postCommentsArrayList.add(commentObject);
                                    postCommentsAdapter.notifyDataSetChanged();
                                    progressBar.setVisibility(View.INVISIBLE);


                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


    }

    void addComment() {
        String commentText = commentField.getText().toString();
        String Uid = mAuth.getUid();
        String timeStamp = new java.util.Date().toString();
        comment newComment = new comment(commentText, Uid, timeStamp, null, null);


        String ref = "/Posts/" + "/Users/" + currentPost.getUser().getUid() + "/UserPosts/" + currentPost.getPostID();
        DatabaseReference mDatabase = realTimeDB.getReference(ref);
        newComment.setReference(ref);
        mDatabase.child("Comments").child(timeStamp).setValue(newComment).toString();
        toast("Comment added");
        commentField.setText("");
        closeKeyboard();
        getComments();

    }

    void toast(String message) {
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) com.abdulghffar.gju_outgoings_app.activities.currentPost.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != com.abdulghffar.gju_outgoings_app.activities.currentPost.this.getCurrentFocus())
            imm.hideSoftInputFromWindow(currentPost.this.getCurrentFocus()
                    .getApplicationWindowToken(), 0);
    }


}