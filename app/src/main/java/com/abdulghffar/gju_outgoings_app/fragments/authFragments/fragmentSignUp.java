package com.abdulghffar.gju_outgoings_app.fragments.authFragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.authentication;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.annotation.Nullable;


public class fragmentSignUp extends Fragment {
    //Vars
    String name;
    String email;
    String password;
    String confirmPassword;
    String gender;

    //Fields
    EditText emailField;
    EditText passwordField;
    EditText confirmPasswordField;
    EditText nameField;

    TextView signInTextView;

    //Buttons
    Button signUpButton;


    //Firebase
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    //RadioGroup
    RadioGroup radioGroup;
    RadioButton maleRadioButton;
    RadioButton femaleRadioButton;

    //others
    View view;
    authentication authentication;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_sign_up, parent, false);
        setup();
        // Check if user is signed in (non-null) and update UI accordingly.
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValues();
            }
        });
        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentSignIn fragmentSignIn = new fragmentSignIn();
                assert authentication != null;
                authentication.replaceFragment(fragmentSignIn);
            }
        });


        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }


    void setup() {
        //Fields
        emailField = view.findViewById(R.id.email);
        nameField = view.findViewById(R.id.fullName);
        passwordField = view.findViewById(R.id.password);
        confirmPasswordField = view.findViewById(R.id.confirmPassword);

        signInTextView = view.findViewById(R.id.signIn);

        //Buttons
        signUpButton = view.findViewById(R.id.signUpButton);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //RadioGroup
        radioGroup = view.findViewById(R.id.genderRadioGroup);
        maleRadioButton = view.findViewById(R.id.maleRadioButton);
        femaleRadioButton = view.findViewById(R.id.femaleRadioButton);

        authentication = (authentication) getActivity();


    }

    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }


    //Get User data from edittext fields
    void getValues() {
        //Data from fields
        name = nameField.getText().toString();
        email = emailField.getText().toString();
        password = passwordField.getText().toString();
        confirmPassword = confirmPasswordField.getText().toString();


        //Check if the fields are filled in appropriate way
        //check name
        if (name.matches("")) {
            toast("Please enter your name");
            return;
        }
        //Check email
        if (email.matches("")) {
            toast("Please enter your email");
            return;
        }
        if ((!email.contains("@")) || (!email.contains(".com"))) {
            toast("Invalid email format");
            return;
        }
        //Check password
        if (password.matches("")) {
            toast("Please enter your password");
            return;
        }
        if (password.length() < 7) {
            toast("Your password is too short");
            return;
        }
        if (confirmPassword.matches("")) {
            toast("Please confirm your password");
            return;
        }
        if (!confirmPassword.equals(password)) {
            toast("Both passwords does not math");
            return;
        }
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            toast("Please pick your gender");
            return;
        }
        int selectedId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = view.findViewById(selectedId);
        gender = radioButton.getText().toString();

        FirebaseRegistration(email, password);


    }


    private void FirebaseRegistration(String email, String password) {
        authentication.loadingUI(1);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            toast("You signed up successfully");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                            assert user != null;
                            user.updateProfile(profileUpdates);
                            updateUI(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            toast("Authentication failed" + task.getException());
                        }
                        authentication.loadingUI(0);
                    }


                });


    }

    private void updateUI(FirebaseUser user) {
        checkUserInfo(user);
        fragmentMoreInfo fragmentMoreInfo = new fragmentMoreInfo();
        assert authentication != null;
        authentication.replaceFragment(fragmentMoreInfo);
    }

    private void checkUserInfo(FirebaseUser user) {
        System.out.println(user.getUid());
        db = FirebaseFirestore.getInstance();

        String timeStamp = new java.util.Date().toString();
        user userData = new user(user.getUid(), "Not yet", user.getEmail(), gender, null, name, null, timeStamp, "Student", null, null);

        assert authentication != null;
        authentication.setUserData(userData);

    }



}

