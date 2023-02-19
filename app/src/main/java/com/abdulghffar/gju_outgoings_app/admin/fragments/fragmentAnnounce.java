package com.abdulghffar.gju_outgoings_app.admin.fragments;

import static com.abdulghffar.gju_outgoings_app.utils.notificationsSender.sendNotificationToAllUsers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.admin.Admin;

import javax.annotation.Nullable;

public class fragmentAnnounce extends Fragment {
    View view;
    ImageView announceButton;
    EditText announceMessage;
    Admin admin;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_announce, parent, false);

        setUp();

        announceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (announceMessage.length() < 3) {
                    closeKeyboard();
                    if (admin != null) {
                        admin.toast("The message is too short");
                    }
                } else {
                    sendNotificationToAllUsers(announceMessage.getText().toString());
                    closeKeyboard();
                    if (admin != null) {
                        admin.toast("Done");
                    }
                }

            }
        });
        return view;
    }

    void setUp() {
        announceButton = view.findViewById(R.id.announceButton);
        announceMessage = view.findViewById(R.id.announceMessage);
        admin = (Admin) getActivity();

    }

    void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != getActivity().getCurrentFocus())
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), 0);
    }

}