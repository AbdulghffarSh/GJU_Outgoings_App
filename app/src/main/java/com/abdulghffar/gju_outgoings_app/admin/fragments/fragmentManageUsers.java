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
import com.abdulghffar.gju_outgoings_app.adapters.userAdapter;
import com.abdulghffar.gju_outgoings_app.admin.Admin;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.abdulghffar.gju_outgoings_app.utils.FcmNotificationsSender;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class fragmentManageUsers extends Fragment {

    RecyclerView reportsRecyclerView;
    ArrayList<user> usersArrayList;
    ArrayList<user> newUsersArrayList;
    userAdapter userAdapter;
    TextView empty, newUsersCount;

    FirebaseFirestore db;

    //Others
    View view;
    Admin Admin;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_manage_users, parent, false);

        setup();
        getUsers();

        userAdapter.setOnItemClickListener(new userAdapter.OnItemClickListener() {
            @Override
            public void setAsModeratorButton(int position) {
                user currentItem = newUsersArrayList.get(position);
                db.collection("Users")
                        .document(currentItem.getUid())
                        .update("role", "Moderator");

                if (currentItem.getFcmToken() != null) {
                    sendNotification("Account", "You have been promoted to moderator", currentItem.getFcmToken());
                }

            }

            @Override
            public void acceptButton(int position) {
                user currentItem = newUsersArrayList.get(position);
                db.collection("Users")
                        .document(currentItem.getUid())
                        .update("role", "Approved");

                if (currentItem.getFcmToken() != null) {
                    sendNotification("Account", "You have been accepted as a user", currentItem.getFcmToken());
                }

            }

            @Override
            public void rejectButton(int position) {
                user currentItem = newUsersArrayList.get(position);
                db.collection("Users")
                        .document(currentItem.getUid())
                        .update("role", "Denied");

                if (currentItem.getFcmToken() != null) {
                    sendNotification("Account", "You have been rejected as a user", currentItem.getFcmToken());
                }
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
        Admin = (Admin) getActivity();
        newUsersArrayList = new ArrayList<>();
        reportsRecyclerView = view.findViewById(R.id.recyclerView);
        userAdapter = new userAdapter(newUsersArrayList);
        reportsRecyclerView.setHasFixedSize(true);
        reportsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        reportsRecyclerView.setAdapter(userAdapter);
        empty = (TextView) view.findViewById(R.id.empty);
        newUsersCount = view.findViewById(R.id.newUsersCount);

        db = FirebaseFirestore.getInstance();

    }


    void toast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    void getUsers() {
        db.collection("Users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
                    @Override
                    public void onEvent(@androidx.annotation.Nullable QuerySnapshot value, @androidx.annotation.Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        assert value != null;
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED && dc.getDocument().toObject(user.class).getApproval().equals("Not yet")) {
                                newUsersArrayList.add(dc.getDocument().toObject(user.class));

                            }

                            userAdapter.notifyDataSetChanged();

                        }
                        newUsersCount.setText(newUsersArrayList.size() + "");
                    }
                });

    }

    void sendNotification(String title, String body, String fcmToken) {
        FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(fcmToken, title, body, getContext(), getActivity());
        fcmNotificationsSender.SendNotifications();
    }


}