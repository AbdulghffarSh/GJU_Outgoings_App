package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;
import com.abdulghffar.gju_outgoings_app.adapters.eventAdapter;
import com.abdulghffar.gju_outgoings_app.objects.event;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentEvents extends Fragment {

    RecyclerView recyclerView;
    com.abdulghffar.gju_outgoings_app.adapters.eventAdapter eventAdapter;
    ArrayList<event> eventsArrayList;
    FirebaseFirestore db;
    navBarActivities navBarActivities;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_events, parent, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = FirebaseFirestore.getInstance();
        eventsArrayList = new ArrayList<event>();
        eventAdapter = new eventAdapter(eventsArrayList);
        navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;

        recyclerView.setAdapter(eventAdapter);

        eventAdapter.setOnItemClickListener(new eventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openDialog(position);
            }
        });

        EventChangeListener();

        return view;
    }

    private void EventChangeListener() {
        navBarActivities.progressBarStatus(true);
        db.collection("Events").orderBy("startDate", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        assert value != null;
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                eventsArrayList.add(dc.getDocument().toObject(event.class));
                            }

//                            navBarActivities.seteventsArrayList(eventsArrayList);
                            eventAdapter.notifyDataSetChanged();
                            navBarActivities.progressBarStatus(false);
                        }

                    }
                });


    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }


    void openDialog(int position) {
        ViewGroup subView = (ViewGroup) getLayoutInflater().// inflater view
                inflate(R.layout.event_dialog, null, false);
        TextView eventTitle = subView.findViewById(R.id.eventTitle);
        TextView startTime = subView.findViewById(R.id.startTime);
        TextView endTime = subView.findViewById(R.id.endTime);
        TextView description = subView.findViewById(R.id.eventDescription);


        eventTitle.setText(eventsArrayList.get(position).getTitle());
        startTime.setText(eventsArrayList.get(position).getStartDate().toString());
        endTime.setText(eventsArrayList.get(position).getEndDate().toString());
        description.setText(eventsArrayList.get(position).getDescription());


        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(getActivity(), R.style.AlertDialogCustom);

        builder.setView(subView);

        builder.setCancelable(false);
        builder
                .setPositiveButton(
                        "Add",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                addEvent(eventsArrayList.get(position));

                            }
                        });

        builder
                .setNegativeButton(
                        "Dismiss",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    void addEvent(event currentEvent) {

        Intent calendarIntentVar = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, currentEvent.getStartDate())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, currentEvent.getEndDate())
                .putExtra(CalendarContract.Events.TITLE, currentEvent.getTitle())
                .putExtra(CalendarContract.Events.DESCRIPTION, currentEvent.getDescription());

        try {
            startActivity(calendarIntentVar);
        } catch (Exception ErrVar) {
            toast("Install Calender App");
        }

    }

    void toast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }

}