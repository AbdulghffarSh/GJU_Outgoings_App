package com.abdulghffar.gju_outgoings_app.database;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class firebaseDb {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static FirebaseDatabase realDb = FirebaseDatabase.getInstance();
    public static String currentPlayerId;




    public static void updatePlayerId() {

        DocumentReference documentReference = db.collection("Users").document(user.getUid());
        documentReference.update("playerId", currentPlayerId)
                .addOnSuccessListener(unused -> Log.d("PlayerId","playerId updated successfully")).addOnFailureListener(e -> Log.d("PlayerId","Unable to update playerId"));


    }


}
