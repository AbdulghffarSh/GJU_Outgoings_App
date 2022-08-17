package com.abdulghffar.gju_outgoings_app.fragments;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.authentication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;


public class fragmentSignUp extends Fragment {
    //Vars
    String name;
    String email;
    String password;
    String confirmPassword;
    String gender;
    Map<String, Object> userData;

    //Fields
    EditText emailField;
    EditText passwordField;
    EditText confirmPasswordField;
    EditText nameField;

    TextView signInTextView;

    //Buttons
    Button signUpButton;
    ImageButton facebookButton;

    //Firebase
    private FirebaseAuth mAuth;
    FirebaseFirestore db;

    //RadioGroup
    RadioGroup radioGroup;
    RadioButton maleRadioButton;
    RadioButton femaleRadioButton;


    View view;

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
                authentication authentication = (authentication) getActivity();
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
        emailField = (EditText) view.findViewById(R.id.email);
        nameField = (EditText) view.findViewById(R.id.fullName);
        passwordField = (EditText) view.findViewById(R.id.password);
        confirmPasswordField = (EditText) view.findViewById(R.id.confirmPassword);

        signInTextView = (TextView) view.findViewById(R.id.signIn);

        //Buttons
        signUpButton = (Button) view.findViewById(R.id.signUpButton);
        facebookButton = (ImageButton) view.findViewById(R.id.facebookButton);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //RadioGroup
        radioGroup = (RadioGroup) view.findViewById(R.id.genderRadioGroup);
        maleRadioButton = (RadioButton) view.findViewById(R.id.maleRadioButton);
        femaleRadioButton = (RadioButton) view.findViewById(R.id.femaleRadioButton);


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
        RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
        gender = radioButton.getText().toString();

        FirebaseRegistration(email, password);


    }


    private void FirebaseRegistration(String email, String password) {
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
                    }


                });


    }

    private void updateUI(FirebaseUser user) {
        checkUserInfo(user);
        authentication registration = (authentication) getActivity();
        fragmentMoreInfo fragmentMoreInfo = new fragmentMoreInfo();
        assert registration != null;
        Bundle bundle = new Bundle();
        bundle.putSerializable("HashMap", (Serializable) userData);
        fragmentMoreInfo.setArguments(bundle);
        registration.replaceFragment(fragmentMoreInfo);
    }

    private void checkUserInfo(FirebaseUser user) {
        System.out.println(user.getUid());
        db = FirebaseFirestore.getInstance();

        String timeStamp = new java.util.Date().toString();
        userData = new HashMap<>();
        // Create a new user with a first, middle, and last name
        userData.put("Uid", user.getUid());
        userData.put("name", name);
        userData.put("major", null);
        userData.put("profilePic", null);
        userData.put("registrationTimeStamp", timeStamp);
        userData.put("role", "Student");
        userData.put("studentID", null);
        userData.put("gender", gender);
        userData.put("email", user.getEmail());


    }
}

