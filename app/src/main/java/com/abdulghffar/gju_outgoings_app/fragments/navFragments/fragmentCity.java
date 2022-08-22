package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentCity extends Fragment {
    city cityData;

    RecyclerView commentsRecyclerView;
    RecyclerView recyclerView;

    ArrayList<String> universityNames;
    ArrayList<comment> commentsArraylist;

    commentAdapter commentAdapter;
    universityAdapter universityAdapter;


    TextView cityName;
    EditText commentField;
    ImageView cityPic;
    ImageView addCommentButton;

    FirebaseDatabase realTimeDB;
    FirebaseFirestore db;

    navBarActivities navBarActivities;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_city, parent, false);

        setup();
        getComments();


        universityAdapter.setOnItemClickListener(new universityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");

            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentField.getText().toString().matches(""))
                    addComment();
            }

        });


        return view;
    }

    private void setData(city cityData) {
        cityName = view.findViewById(R.id.cityName);
        cityPic = view.findViewById(R.id.img);
        addCommentButton = view.findViewById(R.id.sendButton);

        cityName.setText(cityData.getCityName());
        if (cityData.getPics() != null) {
            Picasso.get().load(cityData.getPics().get(0)).into(cityPic);
        }


    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    public void changeItem(int position, String text) {

        navBarActivities navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;
        navBarActivities.setUniversity(position);
        fragmentUniversity fragmentUniversity = new fragmentUniversity();
        navBarActivities.replaceFragment(fragmentUniversity);


    }

    void getComments() {
        realTimeDB = FirebaseDatabase.getInstance("https://gju-outgings-app-24c61-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = realTimeDB.getReference("/Cities/" + cityData.getCityName() + "/Comments/");
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
                                    comment commentObject = new comment(comment, Uid, timeStamp, user);
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

        comment newComment = new comment(commentText, Uid, timeStamp, null);

        DatabaseReference mDatabase = realTimeDB.getReference("/Cities/" + cityData.getCityName());
        mDatabase.child("Comments").child(timeStamp).setValue(newComment);
        toast("Comment added");
        commentField.setText("");

        getComments();

    }

    void setup() {
        navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;
        cityData = navBarActivities.getCityData();
        setData(cityData);

        universityNames = new ArrayList<>(cityData.getUniversities().keySet());
        commentsArraylist = new ArrayList<>();
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);

        recyclerView = view.findViewById(R.id.universitiesRecyclerView);
        universityAdapter = new universityAdapter(universityNames);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(universityAdapter);

        commentAdapter = new commentAdapter(commentsArraylist);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentsRecyclerView.setAdapter(commentAdapter);

        commentField = view.findViewById(R.id.commentField);
    }

    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

}