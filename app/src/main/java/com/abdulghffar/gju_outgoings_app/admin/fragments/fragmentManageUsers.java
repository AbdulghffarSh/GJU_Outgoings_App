package com.abdulghffar.gju_outgoings_app.admin.fragments;

import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.db;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.abdulghffar.gju_outgoings_app.adapters.userAdapter;
import com.abdulghffar.gju_outgoings_app.admin.Admin;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.abdulghffar.gju_outgoings_app.utils.notificationsSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
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


                db.collection("Users").document(currentItem.getUid()).update("role", "Moderator", "approval", "Approved").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Success", "Done");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failed", e.toString());

                    }
                });


                try {
                    notificationsSender.sendNotificationToAllUsers("You have been promoted to the role of moderator.", "\"" + currentItem.getPlayerId() + "\"");
                } catch (Exception e) {
                    Log.i("Error with sending notification to the user ", e.toString());
                }


                newUsersArrayList.remove(position);
                userAdapter.notifyDataSetChanged();


            }

            @Override
            public void acceptButton(int position) {

                user currentItem = newUsersArrayList.get(position);

                db.collection("Users").document(currentItem.getUid()).update("approval", "Approved").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Success", "Done");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failed", e.toString());

                    }
                });
                try {
                    notificationsSender.sendNotificationToAllUsers("Your request for registration has been approved", "\"" + currentItem.getPlayerId() + "\"");
                } catch (Exception e) {
                    Log.i("Error with sending notification to the user ", e.toString());
                }

                newUsersArrayList.remove(position);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void rejectButton(int position) {
                user currentItem = newUsersArrayList.get(position);

                System.out.println(currentItem);

                db.collection("Users").document(currentItem.getUid()).update("approval", "Denied").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Success", "Done");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Failed", e.toString());

                    }
                });
                try {
                    notificationsSender.sendNotificationToAllUsers("Your registration request was rejected, please contact the moderator", "\"" + currentItem.getPlayerId() + "\"");
                } catch (Exception e) {
                    Log.i("Error with sending notification to the user ", e.toString());
                }


                newUsersArrayList.remove(position);
                userAdapter.notifyDataSetChanged();
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


    }


    void toast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    void getUsers() {
        db.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
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


}