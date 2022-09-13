package com.abdulghffar.gju_outgoings_app.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentAdd;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentFeatures;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentHome;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentSearch;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentSettings;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity {

    ImageView profileImage;
    FirebaseFirestore db;
    FirebaseUser user;
    user userData;
    BottomNavigationView nav;
    TextView activityNameField;
    ProgressBar progressBar;

    post currentPost;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        profileImage = findViewById(R.id.accountPic);
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        activityNameField = findViewById(R.id.activityName);
        progressBar = findViewById(R.id.progressBar);

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

                        //Using Picasso
                        if (userData.getProfilePic() != null) {
                            Picasso.get().load(userData.getProfilePic()).into(profileImage);
                        }
                        getToken();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        nav = findViewById(R.id.botNavBar);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        fragmentHome fragmentHome = new fragmentHome();
                        replaceFragment(fragmentHome, "Home");
                        break;
                    case R.id.search:
                        fragmentSearch fragmentSearch = new fragmentSearch();
                        replaceFragment(fragmentSearch, "Search");
                        break;
                    case R.id.add:
                        fragmentAdd fragmentAdd = new fragmentAdd();
                        replaceFragment(fragmentAdd, "Post");
                        break;
                    case R.id.features:
                        fragmentFeatures fragmentFeatures = new fragmentFeatures();
                        replaceFragment(fragmentFeatures, "Features");
                        break;
                    case R.id.settings:
                        fragmentSettings fragmentSettings = new fragmentSettings();
                        replaceFragment(fragmentSettings, "Settings");
                        break;


                }
                return true;
            }
        });


        fragmentHome fragmentHome = new fragmentHome();
        replaceFragment(fragmentHome, "Home");


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, navBarActivities.class);
                String fragmentName = "accountSettings";
                intent.putExtra("fragmentName", fragmentName);
                startActivity(intent);
            }
        });

    }

    public void replaceFragment(Fragment fragment, String activityName) {
        activityNameField.setText(activityName);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        DocumentReference documentReference = db.collection("Users").document(user.getUid());
        documentReference.update("fcmToken", token)
                .addOnSuccessListener(unused -> toast("Token updated successfully")).addOnFailureListener(e -> toast("Unable to update token"));


    }

    void toast(String message) {
        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    void setUser(user userData) {
        this.userData = userData;
    }

    public user getUser() {
        return userData;
    }

    public void setCurrentPost(post currentPost) {
        this.currentPost = currentPost;
    }

    public post getCurrentPost() {
        return currentPost;
    }

    public void progressBarStatus(boolean status) {
        if (status) progressBar.setVisibility(View.VISIBLE);
        else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }


}