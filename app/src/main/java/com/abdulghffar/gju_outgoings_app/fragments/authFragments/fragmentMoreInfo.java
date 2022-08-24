package com.abdulghffar.gju_outgoings_app.fragments.authFragments;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.authentication;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class fragmentMoreInfo extends Fragment {
    //Fields
    Button doneButton;
    ImageView profileImage;
    TextView addImage;

    //EditTexts
    EditText studentIDField;
    EditText majorField;

    //Vars
    user userData;
    authentication authentication;

    //Firebase
    FirebaseFirestore db;
    FirebaseStorage storage;


    ProgressBar progressBar;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_more_info, parent, false);

        //Initialize
        authentication = (authentication) getActivity();
        doneButton = view.findViewById(R.id.doneButton);
        studentIDField = view.findViewById(R.id.studentID);
        majorField = view.findViewById(R.id.major);
        progressBar = view.findViewById(R.id.progressBar);
        userData = authentication.getUserData();
        addImage = view.findViewById(R.id.addImage);
        profileImage = view.findViewById(R.id.uPic);


        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });


        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);

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

    void checkData() {
        progressBar.setVisibility(View.VISIBLE);
        String studentID = studentIDField.getText().toString();
        String major = majorField.getText().toString();
        if (studentID.matches("")) {
            toast("Student ID field is empty");
            return;
        }
        if (major.matches("")) {
            toast("Major field is empty");
            return;
        }
        userData.setStudentID(studentID);
        userData.setMajor(major);

        addToFireStore();


    }

    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

    void addToFireStore() {
        db = FirebaseFirestore.getInstance();
        // Add a new document with a generated ID
        db.collection("Users").document(userData.getUid())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        checkUserApproval(FirebaseAuth.getInstance().getCurrentUser());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        progressBar.setVisibility(View.INVISIBLE);

                    }
                });

    }

    private void updateUI(int x) {
        authentication registration = (authentication) getActivity();
        assert registration != null;
        if (x == 1) {
            fragmentWaiting fragmentWaiting = new fragmentWaiting();
            registration.replaceFragment(fragmentWaiting);
        } else {
            fragmentSignIn fragmentSignIn = new fragmentSignIn();
            registration.replaceFragment(fragmentSignIn);
        }
    }

    private void checkUserApproval(FirebaseUser user) {
        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
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
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

    private void uploadImage(Uri photo) {
        progressBar.setVisibility(View.VISIBLE);
        storage = FirebaseStorage.getInstance();
        if (photo != null) {

            // Code for showing progressDialog while uploading
            ProgressBar ProgressBar
                    = new ProgressBar(getActivity());
            ProgressBar.setVisibility(View.VISIBLE);

            // Defining the child of storageReference
            StorageReference ref
                    = storage.getReference()
                    .child(
                            "Users/ProfilePics/"
                                    + userData.getUid());

            // adding listeners on upload
            // or failure of image
            ref.putFile(photo)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    ProgressBar.setVisibility(View.INVISIBLE);
                                    Toast
                                            .makeText(getActivity(),
                                                    "Done",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Uri downloadUrl = uri;
                                            //Set Image Link in user
                                            //changeData("profilePic", downloadUrl.toString());
                                            userData.setProfilePic(downloadUrl.toString());
                                        }

                                    });


                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            ProgressBar.setVisibility(View.INVISIBLE);
                            Toast
                                    .makeText(getActivity(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

        }

    }


    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {
                    Uri photoUri = result.getData().getData();
                    //use photoUri here

                    profileImage.setImageURI(photoUri);
                    uploadImage(photoUri);
                }
            }
    );
}