package com.abdulghffar.gju_outgoings_app.admin.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.MainActivity;
import com.abdulghffar.gju_outgoings_app.activities.authentication;
import com.abdulghffar.gju_outgoings_app.admin.Admin;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentAdd;
import com.google.firebase.auth.FirebaseAuth;

import javax.annotation.Nullable;

public class fragmentDashboard extends Fragment {

    ImageView signOutButton;
    Button changeLayout;
    Button addPost;

    //Others
    View view;
    Admin Admin;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_admin_dashboard, parent, false);

        setup();

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutDialog();
            }
        });
        changeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MainActivity.class);
                startActivity(i);
            }
        });
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Admin = (Admin) getActivity();
                assert Admin != null;
                Admin.replaceFragment(new fragmentAdd());
            }
        });

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    void setup() {
        signOutButton = (ImageView) view.findViewById(R.id.signOutButton);
        changeLayout = (Button) view.findViewById(R.id.changeViewButton);
        addPost = (Button) view.findViewById(R.id.addPostButton);
    }

    public void signOutDialog() {
        ViewGroup subView = (ViewGroup) getLayoutInflater().// inflater view
                inflate(R.layout.update_data_dialog, null, false);
        EditText newData = subView.findViewById(R.id.editText);
        subView.removeView(newData);
        TextView messageField = subView.findViewById(R.id.text);

        messageField.setText("Are you sure you want to sign out?");

        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(getActivity(), R.style.AlertDialogCustom);

        builder.setView(subView);

        builder.setCancelable(false);
        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent i = new Intent(getActivity(), authentication.class);
                                startActivity(i);
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

    void toast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }


}