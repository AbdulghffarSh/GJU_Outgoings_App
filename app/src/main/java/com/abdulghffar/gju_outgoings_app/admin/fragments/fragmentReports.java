package com.abdulghffar.gju_outgoings_app.admin.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.admin.Admin;
import com.abdulghffar.gju_outgoings_app.admin.adapters.reportsAdapter;
import com.abdulghffar.gju_outgoings_app.objects.report;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentReports extends Fragment {

    RecyclerView reportsRecyclerView;
    ArrayList<report> reportArrayList;
    reportsAdapter reportsAdapter;
    TextView empty;

    FirebaseFirestore db;

    //Others
    View view;
    Admin Admin;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_admin_reports, parent, false);

        setup();
        getReports();

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
        reportArrayList = new ArrayList<>();
        reportsRecyclerView = view.findViewById(R.id.recyclerView);
        reportsAdapter = new reportsAdapter(reportArrayList);
        reportsRecyclerView.setHasFixedSize(true);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reportsRecyclerView.setAdapter(reportsAdapter);
        empty = (TextView) view.findViewById(R.id.empty);

        db = FirebaseFirestore.getInstance();

    }


    void toast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    void getReports() {
        db.collection("Reports")
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
                                reportArrayList.add(dc.getDocument().toObject(report.class));
                            }

                            reportsAdapter.notifyDataSetChanged();

                        }
                        if (reportArrayList.size() < 1) {
                            empty.setVisibility(View.VISIBLE);
                        } else {
                            empty.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }


}