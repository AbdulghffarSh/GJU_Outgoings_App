package com.abdulghffar.gju_outgoings_app.admin;

import static android.content.ContentValues.TAG;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.db;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.mAuth;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.user;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.admin.fragments.fragmentDashboard;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class Admin extends AppCompatActivity {

    user userData;

    FragmentManager fragmentManager;
    ImageView loadingLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        setup();
        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        userData = document.toObject(user.class);
                        assert userData != null;
                        if (userData.getUid() == null) {
                            userData.setUid(user.getUid());
                        }
                        System.out.println(user.getUid());
                        System.out.println(userData.getUid());
                        assert userData != null;
                        setUser(userData);


                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }

    void setup() {
        fragmentManager = getSupportFragmentManager();
        replaceFragment(new fragmentDashboard());
        loadingLogo = findViewById(R.id.loadingLogo);
        loadingLogo.setImageResource(R.drawable.loading_logo);

    }

    public void replaceFragment(Fragment fragment) {
        user = mAuth.getCurrentUser();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }


    public void toast(String message) {
        Toast toast = Toast.makeText(Admin.this, message, Toast.LENGTH_LONG);
        toast.show();
    }

    void setUser(user userData) {
        this.userData = userData;
    }

    public user getUser() {
        return userData;
    }

    @Override
    public void onBackPressed() {

        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStack();
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            Intent i = new Intent(Admin.this, Admin.class);
            startActivity(i);

        }
    }
    public void loadingUI(int value){
    switch (value){
        case 0: 
            ((AnimationDrawable) loadingLogo.getDrawable()).stop();
            loadingLogo.setVisibility(View.INVISIBLE);
            break;
        case 1: 
            ((AnimationDrawable) loadingLogo.getDrawable()).start();
            loadingLogo.setVisibility(View.VISIBLE);
            break;
    }
}



}