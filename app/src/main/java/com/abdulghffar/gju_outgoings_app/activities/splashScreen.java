package com.abdulghffar.gju_outgoings_app.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.ProgressBar;

import com.abdulghffar.gju_outgoings_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;

public class splashScreen extends AppCompatActivity {
    Timer timer = new Timer();
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        timer();


    }

    void timer() {

        ObjectAnimator animation2 = ObjectAnimator.ofInt(null, "progress", 0, 100);
        animation2.setDuration(3000);
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                user = mAuth.getCurrentUser();
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //do something when the countdown is complete
                checkUser();


            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        animation2.start();

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
                            if (document.get("approval").toString().matches("Not yet")) {
                                Intent intent = new Intent(splashScreen.this, authentication.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(splashScreen.this, MainActivity.class);
                                startActivity(intent);
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
}