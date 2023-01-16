package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.adapters.postAdapter;
import com.abdulghffar.gju_outgoings_app.utils.FcmNotificationsSender;

import javax.annotation.Nullable;

public class fragmentSearch extends Fragment {

RecyclerView recyclerView;
postAdapter adapter;
    View view;
    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_search, parent, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        textView = view.findViewById(R.id.textViewSearch);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FcmNotificationsSender notificationsSender = new FcmNotificationsSender();
                notificationsSender.sendNotification();
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