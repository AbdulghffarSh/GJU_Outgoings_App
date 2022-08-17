package com.abdulghffar.gju_outgoings_app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.fragments.fragmentRating;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentCity;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class navBarActivities extends AppCompatActivity {

    ImageView backButton;
    TextView label;
    ArrayList<city> citiesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_activites);

        backButton = (ImageView) findViewById(R.id.backButton);
        label = (TextView) findViewById(R.id.activityLabel);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(navBarActivities.this, MainActivity.class);
                startActivity(intent);
            }
        });

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("fragmentName");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("fragmentName");
        }


        assert newString != null;
        pickFragment(newString);


    }


    public void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    void pickFragment(String fragmentName) {
        switch (fragmentName) {
            case "Rating":
                label.setText("Cities and Universities");
                fragmentRating fragmentRating = new fragmentRating();
                replaceFragment(fragmentRating);
                break;
            case "City":
                label.setText("Cities and Universities");
                fragmentCity fragmentCity = new fragmentCity();
                replaceFragment(fragmentCity);
                break;

        }


    }

    public ArrayList<city> getCitiesData() {
        return citiesData;
    }

    public void setCitiesData(ArrayList<city> citiesData) {
        this.citiesData = citiesData;
    }
}