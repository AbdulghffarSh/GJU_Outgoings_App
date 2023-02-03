package com.abdulghffar.gju_outgoings_app.activities;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.fragments.authFragments.fragmentSignIn;
import com.abdulghffar.gju_outgoings_app.objects.user;

public class authentication extends AppCompatActivity {

    static user userData;
    ImageView loadingLogo;

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
        loadingLogo = findViewById(R.id.loadingLogo);
        loadingLogo.setImageResource(R.drawable.loading_logo);

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


