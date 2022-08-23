package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;
import com.abdulghffar.gju_outgoings_app.adapters.commentAdapter;
import com.abdulghffar.gju_outgoings_app.adapters.universityAdapter;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.abdulghffar.gju_outgoings_app.objects.comment;
import com.abdulghffar.gju_outgoings_app.objects.university;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.google.android.gms.tasks.OnSuccessListener;
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

}