package com.abdulghffar.gju_outgoings_app.activities;

import static android.content.ContentValues.TAG;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.currentPlayerId;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.db;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.mAuth;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.updatePlayerId;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.admin.Admin;
import com.abdulghffar.gju_outgoings_app.database.firebaseDb;
import com.abdulghffar.gju_outgoings_app.fragments.authFragments.fragmentSignIn;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.abdulghffar.gju_outgoings_app.utils.notificationsSender;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.onesignal.OneSignal;

public class authentication extends AppCompatActivity {

    static user userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        setup();
        super.onStart();


    }


    void setup() {
        //FragmentChang
        fragmentSignIn fragmentSignIn = new fragmentSignIn();
        replaceFragment(fragmentSignIn);

    }


    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    public user getUserData() {
        return userData;
    }

    public void setUserData(user userData) {
        this.userData = userData;
    }

    private void checkUser() {

        if (firebaseDb.user != null) {
            getPlayerId();
            updatePlayerId();
            DocumentReference docRef = db.collection("Users").document(firebaseDb.user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());


                            if (document.get("role").toString().matches("Moderator")) {
                                Intent intent = new Intent(authentication.this, Admin.class);
                                startActivity(intent);
                            } else {
                                if (document.get("approval").toString().matches("Approved")) {
                                    Intent intent = new Intent(authentication.this, MainActivity.class);
                                    startActivity(intent);
                                } else if (document.get("approval").toString().matches("Blocked")) {
                                    mAuth.signOut();
                                    toast("You have been blocked, please contact the moderator");
                                } else if (document.get("approval").toString().matches("Denied")) {
                                    mAuth.signOut();
                                    toast("Your registration request was rejected, please contact the moderator");
                                } else {
                                    toast("You have a problem with your account, please contact the moderator");
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


        }

    }

    void toast(String message) {
        Toast toast = Toast.makeText(authentication.this, message, Toast.LENGTH_LONG);
        toast.show();
    }
    public String getPlayerId() {
        if(currentPlayerId==null){
            // Enable verbose OneSignal logging to debug issues if needed.
            OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

            // OneSignal Initialization
            OneSignal.initWithContext(authentication.this);
            OneSignal.setAppId(notificationsSender.app_id);

            // promptForPushNotifications will show the native Android notification permission prompt.
            // We recommend removing the following code and instead using an In-App Message to prompt for notification permission (See step 7)
            OneSignal.promptForPushNotifications();
            currentPlayerId = OneSignal.getDeviceState().getUserId();
        }
            return currentPlayerId;
}

}


