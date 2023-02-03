package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.mAuth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.authentication;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;

import javax.annotation.Nullable;

public class fragmentSettings extends Fragment {


    RelativeLayout singOutLayout;
    RelativeLayout profileSettingsLayout;
    RelativeLayout contactUsLayout;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_settings, parent, false);
        setup();

        singOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutDialog();

            }
        });
        contactUsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), navBarActivities.class);
                String fragmentName = "contactUs";
                intent.putExtra("fragmentName", fragmentName);
                startActivity(intent);
            }
        });
        profileSettingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), navBarActivities.class);
                String fragmentName = "accountSettings";
                intent.putExtra("fragmentName", fragmentName);
                startActivity(intent);
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

    void setup() {
        singOutLayout = view.findViewById(R.id.signOutLayout);
        profileSettingsLayout = view.findViewById(R.id.profileSettingsLayout);
        contactUsLayout = view.findViewById(R.id.contactUsLayout);

    }


    public void signOutDialog() {
        ViewGroup subView = (ViewGroup) getLayoutInflater().// inflater view
                inflate(R.layout.update_data_dialog, null, false);
        EditText newData = subView.findViewById(R.id.editText);
        subView.removeView(newData);
        TextView messageField = subView.findViewById(R.id.text);

        messageField.setText("Are you sure you want to sign out?");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);

        builder.setView(subView);

        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), authentication.class);
                startActivity(intent);
            }
        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

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


    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }
}