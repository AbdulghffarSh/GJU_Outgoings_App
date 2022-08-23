package com.abdulghffar.gju_outgoings_app.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
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
import android.widget.SearchView;
import android.widget.TextView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.fragments.fragmentRating;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentAccount;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentCity;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentContactUs;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.abdulghffar.gju_outgoings_app.objects.university;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

public class navBarActivities extends AppCompatActivity {

    ImageView backButton;
    TextView label;
    ArrayList<city> citiesArrayList;
    city cityData;
    HashMap<String, university> cityUniversities;
    FirebaseFirestore db;
    university universityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_activites);

        backButton = findViewById(R.id.backButton);
        label = findViewById(R.id.activityLabel);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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
            case "accountSettings":
                label.setText("Account Settings");
                fragmentAccount fragmentAccount = new fragmentAccount();
                replaceFragment(fragmentAccount);
                break;
            case "contactUs":
                label.setText("Contact Us");
                fragmentContactUs fragmentContactUs = new fragmentContactUs();
                replaceFragment(fragmentContactUs);
                break;




        }


    }

    public void setCitiesArrayList(ArrayList<city> citiesArrayList) {
        this.citiesArrayList = citiesArrayList;
    }

    public ArrayList<city> getCitiesArrayList() {
        return citiesArrayList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void setCityData(city cityData) {
        this.cityData = cityData;
        getUniversities();
    }

    public city getCityData() {
        return cityData;
    }

    public void getUniversities() {
        db = FirebaseFirestore.getInstance();
        cityUniversities = new HashMap<>();
        for (DocumentReference ref : cityData.getUniversities().values()) {
            System.out.println("This is the ref " + ref.toString());
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            university university = document.toObject(university.class);
                            assert university != null;
                            cityUniversities.put(university.getUniversityName(), university);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                    setCityUniversities(cityUniversities);
                }
            });
        }


    }

    public void setCityUniversities(HashMap<String, university> cityUniversities) {
        this.cityUniversities = cityUniversities;
    }

    public void setUniversity(int position) {
        ArrayList<university> universities = new ArrayList<>(cityUniversities.values());
        universityData = universities.get(position);

    }

    public university getUniversity() {
        ArrayList<university> universities = new ArrayList<>(cityUniversities.values());
        return universityData;
    }


}