package com.abdulghffar.gju_outgoings_app.activities;

import static android.content.ContentValues.TAG;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.db;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.user;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.admin.Admin;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentAddPost;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentFeatures;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentHome;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentSearch;
import com.abdulghffar.gju_outgoings_app.fragments.navFragments.fragmentSettings;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;


public class MainActivity extends AppCompatActivity {

    ImageView profileImage;

    static user userData;
    BottomNavigationView nav;
    TextView activityNameField;

    ImageView loadingLogo;

    post currentPost;
    FragmentManager fragmentManager;
    boolean doubleBackClick = false;
    ImageView adminPanelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Call your getData() method to refresh the data
                fragmentHome fragmentHome = new fragmentHome();
                replaceFragment(fragmentHome, "Home");
                // Hide the refresh indicator after the data is refreshed
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        profileImage = findViewById(R.id.accountPic);
        activityNameField = findViewById(R.id.activityName);
        loadingLogo = findViewById(R.id.loadingLogo);
        loadingLogo.setImageResource(R.drawable.loading_logo);

        fragmentManager = getSupportFragmentManager();
        adminPanelButton = findViewById(R.id.adminPanelButton);


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
                            Glide.with(MainActivity.this).load(userData.getProfilePic()).into(profileImage);
                        }
                        if (userData.getRole().equals("Moderator")) {
                            adminPanelButton.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        adminPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Admin.class);
                startActivity(intent);
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
                        fragmentAddPost fragmentAdd = new fragmentAddPost();
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
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    @Override
    public void onBackPressed() {
        if (doubleBackClick) {
            Log.i("MainActivity", "Double back click");
            this.finishAffinity();
            return;
        } else if (fragmentManager.getBackStackEntryCount() > 1) {
            Log.i("MainActivity", "popping backstack");
            fragmentManager.popBackStack();
        } else if (fragmentManager.getBackStackEntryCount() == 1) {
            fragmentHome fragmentHome = new fragmentHome();
            replaceFragment(fragmentHome, "Home");
        }

        this.doubleBackClick = true;

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackClick = false;
            }
        }, 500);

    }

    public void loadingUI(int value) {
        switch (value) {
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