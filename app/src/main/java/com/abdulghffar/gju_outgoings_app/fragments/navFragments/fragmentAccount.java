package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import static android.content.ContentValues.TAG;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Map;

import javax.annotation.Nullable;

public class fragmentAccount extends Fragment {

    FirebaseUser user;
    FirebaseFirestore db;
    Map<String, Object> userData;

    ImageView profileImage;
    TextView fullName;
    TextView uID;
    TextView email;
    TextView major;
    TextView status;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_account, parent, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();


        profileImage = (ImageView) view.findViewById(R.id.uPic);
        fullName = (TextView) view.findViewById(R.id.uFullName);
        uID = (TextView) view.findViewById(R.id.uID);
        email = (TextView) view.findViewById(R.id.uEmail);
        major = (TextView) view.findViewById(R.id.uMajor);
        status = (TextView) view.findViewById(R.id.uStatus);


        setProfileImage();

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    void setProfileImage() {


        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        userData = document.getData();
                        assert userData != null;
                        //Using Picasso
                        if (userData.get("profilePic") != null) {

                            Picasso.get().load(userData.get("profilePic").toString()).into(profileImage);


                        }
                        fullName.setText(userData.get("name").toString());
                        uID.setText(userData.get("studentID").toString());
                        email.setText(userData.get("email").toString());
                        major.setText(userData.get("major").toString());
                        status.setText("None");

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }


}