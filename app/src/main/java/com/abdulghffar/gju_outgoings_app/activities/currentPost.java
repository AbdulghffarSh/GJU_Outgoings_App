package com.abdulghffar.gju_outgoings_app.activities;

import static android.content.ContentValues.TAG;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.mAuth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.adapters.commentAdapter;
import com.abdulghffar.gju_outgoings_app.objects.comment;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.abdulghffar.gju_outgoings_app.objects.report;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    FirebaseFirestore db;
    FirebaseDatabase realTimeDB;


    //UI
    TextView postTitle;
    TextView userName;
    TextView postBody;
    TextView timeStamp;
    ImageView accountPic;
    ImageView postImage;
    ImageView addCommentButton;
    ImageView loadingLogo;

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
        postCommentsAdapter.setOnItemClickListener(new commentAdapter.OnItemClickListener() {
            @Override
            public void reportItemClick(int position) {
                checkReportDialog(position);
            }
        });
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
        loadingLogo = findViewById(R.id.loadingLogo);
        loadingLogo.setImageResource(R.drawable.loading_logo);


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
            Glide.with(this).load(currentPost.getUser().getProfilePic()).into(accountPic);
        }
        if (currentPost.getImage() != null) {
            Glide.with(this).load(currentPost.getImage()).into(postImage);

        } else {
            ((ViewManager) postImage.getParent()).removeView(postImage);
        }

        db = FirebaseFirestore.getInstance();
        getComments();
    }

    void getComments() {
        loadingUI(1);
        realTimeDB = FirebaseDatabase.getInstance();
        String ref = "/Posts/" + "/Users/" + currentPost.getUser().getUid() + "/UserPosts/" + currentPost.getPostID() + "/Comments";

        DatabaseReference myRef = realTimeDB.getReference(ref);
        postCommentsArrayList.clear();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            loadingUI(0);
                        }
                    });
                }
                if (postCommentsArrayList.isEmpty()) {
                    loadingUI(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingUI(0);
            }
        });
    }



    void addComment() {
        realTimeDB = FirebaseDatabase.getInstance();
        String commentText = commentField.getText().toString();
        String Uid = FirebaseAuth.getInstance().getUid();
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

    void loadingUI(int value) {
        switch (value) {
            case 0:
                ((AnimationDrawable) loadingLogo.getDrawable()).stop();
                loadingLogo.setVisibility(View.INVISIBLE);
                break;
            case 1:
                ((AnimationDrawable) loadingLogo.getDrawable()).start();
                loadingLogo.setVisibility(View.VISIBLE);
                break;
        }
    }

    void checkReportDialog(int position) {
        ViewGroup subView = (ViewGroup) getLayoutInflater().// inflater view
                inflate(R.layout.update_data_dialog, null, false);
        EditText newData = subView.findViewById(R.id.editText);
        subView.removeView(newData);
        TextView messageField = subView.findViewById(R.id.text);

        messageField.setText("Are you sure you want to report this comment?");

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);

        builder.setView(subView);

        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                reportComment(position);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
    void reportComment(int position) {

        comment selectedComment = postCommentsArrayList.get(position);
        ArrayList<String> reportedBy = new ArrayList<>();
        reportedBy.add(mAuth.getUid());
        report report = new report(selectedComment.getCommentText(), selectedComment.getReference(), selectedComment.getTimeStamp(), selectedComment.getUid(), reportedBy);
        String myUid = mAuth.getUid();

        DocumentReference docRef = db.collection("Reports").document(selectedComment.getTimeStamp());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                boolean userAlreadyReported = false;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        report existReport = document.toObject(report.class);
                        assert existReport != null;
                        ArrayList<String> reportedBy = existReport.getUsers();
                        for (String uid : reportedBy) {
                            assert myUid != null;
                            if (uid.contains(myUid)) {
                                toast("You have already reported this comment");
                                userAlreadyReported = true;
                            }
                        }
                        if (!userAlreadyReported) {
                            reportedBy.add(myUid);
                            db.collection("Reports").document(selectedComment.getTimeStamp()).set(existReport).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    toast("Reported");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);

                                }
                            });
                        }

                    } else {
                        Log.d(TAG, "No such document");
                        db.collection("Reports").document(selectedComment.getTimeStamp()).set(report).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);

                            }
                        });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}