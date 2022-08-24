package com.abdulghffar.gju_outgoings_app.fragments.authFragments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.MainActivity;
import com.abdulghffar.gju_outgoings_app.activities.authentication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;

public class fragmentSignIn extends Fragment {

    //Fields
    TextView signUpTextView;
    EditText emailField;
    EditText passwordField;
    //Buttons
    Button signInButton;

    //Firebase
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    //Others
    View view;
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_sign_in, parent, false);

        setup();

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check email
                if (emailField.getText().toString().matches("")) {
                    toast("Please enter your email");
                    return;
                }
                if ((!emailField.getText().toString().contains("@")) || (!emailField.getText().toString().contains(".com"))) {
                    toast("Invalid email format");
                    return;
                }
                //Check password
                if (passwordField.getText().toString().matches("")) {
                    toast("Please enter your password");
                    return;
                }
                if (getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                signIn();
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authentication registration = (authentication) getActivity();
                fragmentSignUp fragmentSignUp = new fragmentSignUp();
                assert registration != null;
                registration.replaceFragment(fragmentSignUp);
            }
        });

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
    }

    void setup() {
        //Fields
        signUpTextView = view.findViewById(R.id.signUp);
        emailField = view.findViewById(R.id.emailField);
        passwordField = view.findViewById(R.id.passwordField);

        //Buttons
        signInButton = view.findViewById(R.id.signInButton);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkUserApproval(currentUser);
        }

        //Others
        progressBar = view.findViewById(R.id.progressBar);

    }

    void signIn() {
        progressBar.setVisibility(View.VISIBLE);
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                checkUserApproval(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            toast("Sign in Failed:\n" + task.getException());
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    void updateUI(int x) {
        if (x == 0) {
            Intent i = new Intent(getActivity(), MainActivity.class);
            startActivity(i);
        } else {
            authentication authentication = (authentication) getActivity();
            fragmentWaiting fragmentWaiting = new fragmentWaiting();
            assert authentication != null;
            authentication.replaceFragment(fragmentWaiting);
        }
    }


    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void checkUserApproval(FirebaseUser user) {
        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                progressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        if (document.get("approval").toString().matches("Not yet")) {
                            updateUI(1);
                        } else {
                            updateUI(0);
                        }

                    } else {
                        Log.d(TAG, "No such document");

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

}