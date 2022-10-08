package com.abdulghffar.gju_outgoings_app.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.admin.Admin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class splashScreen extends AppCompatActivity {
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        checkUser();

    }


    private void checkUser() {
        db = FirebaseFirestore.getInstance();
        if (user != null) {
            DocumentReference docRef = db.collection("Users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            if (document.get("role").toString().matches("Moderator")) {
                                Intent intent = new Intent(splashScreen.this, Admin.class);
                                startActivity(intent);
                            } else {

                                if (document.get("approval").toString().matches("Not yet")) {
                                    Intent intent = new Intent(splashScreen.this, authentication.class);
                                    startActivity(intent);
                                } else if (document.get("approval").toString().matches("Approved")) {
                                    Intent intent = new Intent(splashScreen.this, MainActivity.class);
                                    startActivity(intent);
                                } else if (document.get("approval").toString().matches("Blocked")) {
                                    FirebaseAuth.getInstance().signOut();
                                    toast("You have been blocked, please contact the moderator");
                                } else if (document.get("approval").toString().matches("Denied")) {
                                    FirebaseAuth.getInstance().signOut();
                                    toast("Your registration request was rejected, please contact the moderator");
                                } else {
                                    toast("You have a problem with your account, please contact the moderator");
                                    Intent intent = new Intent(splashScreen.this, authentication.class);
                                    startActivity(intent);
                                }
                            }

                        } else {
                            Log.d(TAG, "No such document");

                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });


        } else {
            Intent intent = new Intent(splashScreen.this, authentication.class);
            startActivity(intent);
        }

    }

    void toast(String message) {
        Toast toast = Toast.makeText(splashScreen.this, message, Toast.LENGTH_LONG);
        toast.show();
    }
}