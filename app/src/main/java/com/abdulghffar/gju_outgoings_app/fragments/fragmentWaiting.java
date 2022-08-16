package com.abdulghffar.gju_outgoings_app.fragments;

import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.authentication;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import javax.annotation.Nullable;

public class fragmentWaiting extends Fragment {

    //Buttons
    ImageView logOutButton;

    //Fields
    TextView userDisplayName;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_waiting, parent, false);
        logOutButton = (ImageView) view.findViewById(R.id.signOutButton);
        userDisplayName = (TextView)view.findViewById(R.id.userDisplayName);
        userDisplayName.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());
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


}