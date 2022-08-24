package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
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
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class fragmentAccount extends Fragment {

    FirebaseUser user;
    FirebaseFirestore db;
    user userData;
    FirebaseStorage storage;

    ImageView profileImage;
    ImageView editMajor;
    ImageView editStatus;

    TextView fullName;
    TextView sID;
    TextView email;
    TextView major;
    TextView status;
    TextView changeImage;

    ProgressBar progressBar;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_account, parent, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();


        setup();

        editMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                updateUserData("major", "Enter the new major");
                getProfileData();
            }
        });

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                launcher.launch(intent);


            }
        });


        getProfileData();

        return view;
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    public void getProfileData() {


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
                        //Using Picasso
                        if (userData.getProfilePic() != null) {

                            Picasso.get().load(userData.getProfilePic()).into(profileImage);


                        }
                        fullName.setText(userData.getName().toString());
                        sID.setText(userData.getStudentID().toString());
                        email.setText(userData.getEmail().toString());
                        major.setText(userData.getMajor().toString());
                        status.setText("None");

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    void setup() {
        profileImage = view.findViewById(R.id.uPic);
        fullName = view.findViewById(R.id.uFullName);
        sID = view.findViewById(R.id.sId);
        email = view.findViewById(R.id.uEmail);
        major = view.findViewById(R.id.uMajor);
        status = view.findViewById(R.id.uStatus);
        changeImage = view.findViewById(R.id.changeImage);
        editMajor = view.findViewById(R.id.editMajor);
        editStatus = view.findViewById(R.id.editStatus);
        progressBar = view.findViewById(R.id.progressBar);
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
                                    + user.getUid());

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
                                            changeData("profilePic", downloadUrl.toString());
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


    void changeData(String field, String newData) {
        progressBar.setVisibility(View.VISIBLE);
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(user.getUid());
        docRef.update(field, newData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                System.out.println("Updated");
                progressBar.setVisibility(View.INVISIBLE);
                getProfileData();

            }
        });

    }


    public void updateUserData(String field, String message) {
        ViewGroup subView = (ViewGroup) getLayoutInflater().// inflater view
                inflate(R.layout.update_data_dialog, null, false);
        EditText newData = subView.findViewById(R.id.editText);
        TextView messageField = subView.findViewById(R.id.text);

        messageField.setText(message);
        // Create the object of
        // AlertDialog Builder class
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(getActivity(), R.style.AlertDialogCustom);

        builder.setView(subView);

        // Set the message show for the Alert time

        // Set Alert Title
//        builder.setTitle("Update user " + field);

        // Set Cancelable false
        // for when the user clicks on the outside
        // the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Save",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                changeData(field, newData.getText().toString());
                            }
                        });

        // Set the Negative button with No name
        // OnClickListener method is use
        // of DialogInterface interface.
        builder
                .setNegativeButton(
                        "Cancel",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();

    }

}


