package com.abdulghffar.gju_outgoings_app.activities;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.database.firebaseDb;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentAccount;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentCity;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentContactUs;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentEvents;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentRating;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.abdulghffar.gju_outgoings_app.objects.university;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class navBarActivities extends AppCompatActivity {

    ImageView backButton;
    TextView label;
    ArrayList<city> citiesArrayList;
    city cityData;
    HashMap<String, university> cityUniversities;

    university universityData;
    ArrayList<user> userArrayList;
    ProgressBar progressBar;

    FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar_activites);

        backButton = findViewById(R.id.backButton);
        label = findViewById(R.id.activityLabel);
        progressBar = findViewById(R.id.progressBar);
        fragmentManager = getSupportFragmentManager();


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

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void pickFragment(String fragmentName) {
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
            case "Events":
                label.setText("Events");
                fragmentEvents fragmentEvents = new fragmentEvents();
                replaceFragment(fragmentEvents);
                break;


        }


    }

    public void setCitiesArrayList(ArrayList<city> citiesArrayList) {
        this.citiesArrayList = citiesArrayList;
    }

    public ArrayList<city> getCitiesArrayList() {
        return citiesArrayList;
    }


    public void setCityData(city cityData) {
        this.cityData = cityData;
        getUniversities();
    }

    public city getCityData() {
        return cityData;
    }

    public void getUniversities() {
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
//   cityUniversities public void seteventsArrayList(HashMap<event> events) {
//        this.events = events;
//    }
//

    public void setUniversity(int position) {
        ArrayList<university> universities = new ArrayList<>(cityUniversities.values());
        universityData = universities.get(position);

    }

    public university getUniversity() {
        ArrayList<university> universities = new ArrayList<>(cityUniversities.values());
        return universityData;
    }


    public void setUserArrayList(ArrayList<user> userArrayList) {
        this.userArrayList = userArrayList;
    }

    public ArrayList<user> getUserArrayList() {
        return userArrayList;
    }

    public void progressBarStatus(boolean status) {
        if (status) progressBar.setVisibility(View.VISIBLE);
        else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onBackPressed() {

        if (fragmentManager.getBackStackEntryCount() > 1) {
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStack();
        } else if (fragmentManager.getBackStackEntryCount() == 1) {
            super.onBackPressed();
            super.onBackPressed();

        }


    }

}
