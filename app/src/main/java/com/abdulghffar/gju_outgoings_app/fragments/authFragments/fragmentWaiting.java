package com.abdulghffar.gju_outgoings_app.fragments.authFragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.authentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

import javax.annotation.Nullable;

public class fragmentWaiting extends Fragment {

    //Buttons
    ImageView logOutButton;

    //Fields
    TextView userDisplayName;

    View view;
    FirebaseFirestore db;
    authentication authentication;

    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_waiting, parent, false);
        logOutButton = view.findViewById(R.id.signOutButton);
        userDisplayName = view.findViewById(R.id.userDisplayName);
        userDisplayName.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        updateToken();


        //subscribe to waiting topic
        FirebaseMessaging.getInstance().subscribeToTopic("waiting").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "Subscribed";
                if (!task.isSuccessful()) {
                    msg = "Subscribe failed";
                }
                Log.d(TAG, msg);
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                authentication registration = (authentication) getActivity();
                fragmentSignIn fragmentSignIn = new fragmentSignIn();
                assert registration != null;
                registration.replaceFragment(fragmentSignIn);
            }
        });


        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }


    private void updateToken() {
        DocumentReference documentReference = db.collection("Users").document(user.getUid());
        documentReference.update("playerId", com.abdulghffar.gju_outgoings_app.activities.splashScreen.getPlayerId()).addOnSuccessListener(unused -> toast("Token updated successfully")).addOnFailureListener(e -> toast("Unable to update token"));

    }

    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

}