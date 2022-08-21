package com.abdulghffar.gju_outgoings_app.fragments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.authentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class fragmentMoreInfo extends Fragment {
    //Buttons
    Button doneButton;

    //EditTexts
    EditText studentIDField;
    EditText majorField;

    //Vars
    HashMap<String, Object> userData;

    //Firebase
    FirebaseFirestore db;

    ProgressBar progressBar;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_more_info, parent, false);

        //Initialize
        doneButton = (Button) view.findViewById(R.id.doneButton);
        studentIDField = (EditText) view.findViewById(R.id.studentID);
        majorField = (EditText) view.findViewById(R.id.major);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });

        Bundle bundle = this.getArguments();
        assert bundle != null;
        if (bundle.getSerializable("HashMap") != null) {
            userData = (HashMap<String, Object>) bundle.getSerializable("HashMap");

        }


        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    void checkData() {
        progressBar.setVisibility(View.VISIBLE);
        String studentID = studentIDField.getText().toString();
        String major = majorField.getText().toString();
        if (studentID.matches("")) {
            toast("Student ID field is empty");
            return;
        }
        if (major.matches("")) {
            toast("Major field is empty");
            return;
        }
        userData.put("studentID", studentID);
        userData.put("major", major);
        userData.put("approval", "Not yet");

        addToFireStore();


    }

    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    void addToFireStore() {
        db = FirebaseFirestore.getInstance();
        String Uid = (String) userData.get("Uid");
        // Add a new document with a generated ID
        assert Uid != null;
        db.collection("Users").document(Uid)
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        checkUserApproval(FirebaseAuth.getInstance().getCurrentUser());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });

    }

    private void updateUI(int x) {
        authentication registration = (authentication) getActivity();
        assert registration != null;
        if (x == 1) {
            fragmentWaiting fragmentWaiting = new fragmentWaiting();
            registration.replaceFragment(fragmentWaiting);
        } else {
            fragmentSignIn fragmentSignIn = new fragmentSignIn();
            registration.replaceFragment(fragmentSignIn);
        }
    }

    private void checkUserApproval(FirebaseUser user) {
        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if (document.get("approval").toString().matches("Not yet")) {
                            updateUI(1);
                        } else {
                            updateUI(0);
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }
}