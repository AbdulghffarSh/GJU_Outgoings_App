package com.abdulghffar.gju_outgoings_app.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.fragments.authFragments.fragmentSignIn;
import com.abdulghffar.gju_outgoings_app.objects.user;

public class authentication extends AppCompatActivity {

    static user userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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


}


