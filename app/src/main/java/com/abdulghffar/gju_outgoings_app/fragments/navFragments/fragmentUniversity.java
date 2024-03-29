package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;
import com.abdulghffar.gju_outgoings_app.adapters.commentAdapter;
import com.abdulghffar.gju_outgoings_app.adapters.universityAdapter;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.abdulghffar.gju_outgoings_app.objects.comment;
import com.abdulghffar.gju_outgoings_app.objects.report;
import com.abdulghffar.gju_outgoings_app.objects.university;
import com.abdulghffar.gju_outgoings_app.objects.user;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentUniversity extends Fragment {

    TextView universityName;
    TextView universityNote;
    TextView cityName;
    EditText commentField;
    ImageView addCommentButton;

    ArrayList<comment> commentsArraylist;

    commentAdapter commentAdapter;

    university universityData;

    FirebaseDatabase realTimeDB;
    FirebaseFirestore db;

    RecyclerView commentsRecyclerView;


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_university, parent, false);
        navBarActivities navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;
        universityData = navBarActivities.getUniversity();
        setUpUI();

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentField.getText().toString().matches(""))
                    addComment();
            }

        });
        commentAdapter.setOnItemClickListener(new commentAdapter.OnItemClickListener() {
            @Override
            public void reportItemClick(int position) {
                checkReportDialog(position);
            }
        });


        getComments();

        return view;
    }

    private void setUpUI() {

        universityName = view.findViewById(R.id.universityName);
        universityNote = view.findViewById(R.id.noteField);
        cityName = view.findViewById(R.id.cityNameField);

        universityName.setText(universityData.getUniversityName());
        universityNote.setText(universityData.getNote());
        cityName.setText(universityData.getCityName());

        commentsArraylist = new ArrayList<>();
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);
        commentAdapter = new commentAdapter(commentsArraylist);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentsRecyclerView.setAdapter(commentAdapter);

        commentField = view.findViewById(R.id.commentField);
        addCommentButton = view.findViewById(R.id.sendButton);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    void getComments() {
        realTimeDB = FirebaseDatabase.getInstance("https://gju-outgings-app-24c61-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = realTimeDB.getReference("/Cities/" + universityData.getCityName() + "/" + universityData.getUniversityName() + "/Comments/");
        commentsArraylist.clear();
        myRef
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String Uid = snapshot.child("uid").getValue(String.class);
                            String timeStamp = snapshot.child("timeStamp").getValue(String.class);
                            String comment = snapshot.child("commentText").getValue(String.class);

                            //Get user Data
                            db = FirebaseFirestore.getInstance();

                            DocumentReference docRef = db.collection("Users").document(Uid);
                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    user user = documentSnapshot.toObject(user.class);
                                    com.abdulghffar.gju_outgoings_app.objects.comment commentObject = new comment(comment, Uid, timeStamp, user, null);
                                    commentsArraylist.add(commentObject);
                                    commentAdapter.notifyDataSetChanged();

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
        String Uid = FirebaseAuth.getInstance().getUid();
        String timeStamp = new java.util.Date().toString();
        String ref = "/Cities/" + universityData.getCityName() + "/" + universityData.getUniversityName() + "/Comments/" + timeStamp;
        comment newComment = new comment(commentText, Uid, timeStamp, null, null);
        newComment.setReference(ref);
        DatabaseReference mDatabase = realTimeDB.getReference("/Cities/" + universityData.getCityName() + "/" + universityData.getUniversityName());
        mDatabase.child("Comments").child(timeStamp).setValue(newComment);
        toast("Comment added");
        commentField.setText("");

        getComments();

    }

    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    void reportComment(int position) {

        comment selectedComment = commentsArraylist.get(position);
        ArrayList<String> reportedBy = new ArrayList<>();
        reportedBy.add(FirebaseAuth.getInstance().getUid());
        report report = new report(selectedComment.getCommentText(), selectedComment.getReference(), selectedComment.getTimeStamp(), selectedComment.getUid(), reportedBy);
        String myUid = FirebaseAuth.getInstance().getUid();

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
                        ArrayList<String> reportedBy = existReport.getReportedBy();
                        for (String uid : reportedBy) {
                            assert myUid != null;
                            if (uid.contains(myUid)) {
                                toast("You have already reported this comment");
                                userAlreadyReported = true;
                            }
                        }
                        if (!userAlreadyReported) {
                            reportedBy.add(myUid);
                            db.collection("Reports").document(selectedComment.getTimeStamp())
                                    .set(existReport)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);

                                        }
                                    });
                        }

                    } else {
                        Log.d(TAG, "No such document");
                        db.collection("Reports").document(selectedComment.getTimeStamp())
                                .set(report)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully written!");

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
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

    void checkReportDialog(int position) {
        ViewGroup subView = (ViewGroup) getLayoutInflater().// inflater view
                inflate(R.layout.update_data_dialog, null, false);
        EditText newData = subView.findViewById(R.id.editText);
        subView.removeView(newData);
        TextView messageField = subView.findViewById(R.id.text);

        messageField.setText("Are you sure you want to report this comment?");
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(getActivity(), R.style.AlertDialogCustom);

        builder.setView(subView);

        // Set the message show for the Alert time

        // Set Alert Title
//        builder.setTitle("Update user " + field);

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                reportComment(position);
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
}