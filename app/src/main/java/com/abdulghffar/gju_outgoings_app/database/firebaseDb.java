package com.abdulghffar.gju_outgoings_app.database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class firebaseDb {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static FirebaseDatabase realDb = FirebaseDatabase.getInstance();



}
