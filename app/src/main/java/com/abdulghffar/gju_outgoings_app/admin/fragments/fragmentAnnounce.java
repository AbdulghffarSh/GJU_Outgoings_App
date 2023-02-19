package com.abdulghffar.gju_outgoings_app.admin.fragments;

import static com.abdulghffar.gju_outgoings_app.utils.notificationsSender.sendNotificationToAllUsers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;

import javax.annotation.Nullable;

public class fragmentAnnounce extends Fragment {
    View view;
    ImageView announceButton;
    EditText announceMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_announce, parent, false);

        setUp();

        announceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotificationToAllUsers(announceMessage.getText().toString());
            }
        });
        return view;
    }

    void setUp() {
        announceButton = view.findViewById(R.id.announceButton);
        announceMessage = view.findViewById(R.id.announceMessage);
    }
}