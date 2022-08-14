package com.abdulghffar.gju_outgoings_app.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.fragments.fragmentSignIn;

public class authentication extends AppCompatActivity {


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


    void reload() {

    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }
}