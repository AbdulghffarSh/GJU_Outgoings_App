package com.abdulghffar.gju_outgoings_app.activities;

import static android.content.ContentValues.TAG;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.db;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.mAuth;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.user;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.onesignal.OneSignal;

public class splashScreen extends AppCompatActivity {

    ProgressBar progressBar;

    private static final String ONESIGNAL_APP_ID = "5dd645a1-2b50-4708-b7ae-a297bc750799";
    static String playerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        notificationsHandler();
        progressBar = findViewById(R.id.progressBar);
        user = mAuth.getCurrentUser();
        checkUser();

    }


    private void checkUser() {
        if (user != null) {
            DocumentReference docRef = db.collection("Users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            updatePlayerId();

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
                                    mAuth.signOut();
                                    toast("You have been blocked, please contact the moderator");
                                } else if (document.get("approval").toString().matches("Denied")) {
                                    mAuth.signOut();
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



    private void notificationsHandler() {
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        // promptForPushNotifications will show the native Android notification permission prompt.
        // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
        OneSignal.promptForPushNotifications();
        playerId = OneSignal.getDeviceState().getUserId();
    }

    public static String getPlayerId() {
        return playerId;
    }
    private void updatePlayerId() {
        DocumentReference documentReference = db.collection("Users").document(user.getUid());
        documentReference.update("playerId", splashScreen.getPlayerId())
                .addOnSuccessListener(unused -> toast("playerId updated successfully")).addOnFailureListener(e -> toast("Unable to update playerId"));


    }
}