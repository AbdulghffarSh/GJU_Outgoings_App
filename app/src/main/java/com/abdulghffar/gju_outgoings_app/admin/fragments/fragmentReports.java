package com.abdulghffar.gju_outgoings_app.admin.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.admin.Admin;

import javax.annotation.Nullable;

public class fragmentReports extends Fragment {


    //Others
    View view;
    Admin Admin;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_admin_reports, parent, false);

        setup();

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
        Admin = (Admin) getActivity();


    }


    void toast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }


}