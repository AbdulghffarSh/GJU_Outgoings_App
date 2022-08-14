package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activites.MainActivity;
import com.abdulghffar.gju_outgoings_app.activites.authentication;
import com.abdulghffar.gju_outgoings_app.fragments.fragmentSignIn;
import com.abdulghffar.gju_outgoings_app.fragments.fragmentSignUp;
import com.google.firebase.auth.FirebaseAuth;

import javax.annotation.Nullable;

public class fragmentSettings extends Fragment {


    RelativeLayout singOutLayout;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_settings, parent, false);
        setup();

        singOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), authentication.class);
                startActivity(i);
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

void setup(){
    singOutLayout = (RelativeLayout) view.findViewById(R.id.signOutLayout);

}
}