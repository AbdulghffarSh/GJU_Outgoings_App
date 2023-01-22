package com.abdulghffar.gju_outgoings_app.activities;

import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.mAuth;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.abdulghffar.gju_outgoings_app.R;

public class splashScreen extends AppCompatActivity {

    ProgressBar progressBar;

    private static final String ONESIGNAL_APP_ID = "5dd645a1-2b50-4708-b7ae-a297bc750799";
    static String playerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar = findViewById(R.id.progressBar);
        user = mAuth.getCurrentUser();
        Intent intent = new Intent(splashScreen.this, authentication.class);
        startActivity(intent);
    }











}