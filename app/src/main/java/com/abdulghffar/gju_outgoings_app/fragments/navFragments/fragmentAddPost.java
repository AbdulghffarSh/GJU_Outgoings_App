package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import static android.content.ContentValues.TAG;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.db;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.mAuth;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.user;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
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
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.MainActivity;
import com.abdulghffar.gju_outgoings_app.admin.Admin;
import com.abdulghffar.gju_outgoings_app.database.firebaseDb;
import com.abdulghffar.gju_outgoings_app.objects.post;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.abdulghffar.gju_outgoings_app.utils.notificationsSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;

import javax.annotation.Nullable;

public class fragmentAddPost extends Fragment {

    user userData;
    MainActivity MainActivity;
    //Buttons
    //Tags
    Button accommodationsTag;
    Button eventsTag;
    Button vacanciesTag;
    Button visaTag;
    Button moneyAndBanksTag;
    Button lookingForHelpTag;
    Button otherTag;
    Button submit;

    EditText titleField;
    EditText bodyField;


    ImageView attachImage;
    ProgressBar progressBar;
    FirebaseStorage storage;

    FirebaseDatabase realTimeDB = firebaseDb.realDb;



    Uri currentImage;


    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_add, parent, false);

        setup();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishPost();
            }
        });
        accommodationsTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(accommodationsTag);


            }
        });
        eventsTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(eventsTag);

            }
        });
        vacanciesTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(vacanciesTag);

            }
        });
        visaTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(visaTag);

            }
        });
        moneyAndBanksTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(moneyAndBanksTag);

            }
        });
        lookingForHelpTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(lookingForHelpTag);

            }
        });
        otherTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectButton(otherTag);

            }
        });

        attachImage.setOnClickListener(new View.OnClickListener() {
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
    }

    void setup() {
        //Tags
        accommodationsTag = view.findViewById(R.id.accommodationsTag);
        eventsTag = view.findViewById(R.id.eventsTag);
        vacanciesTag = view.findViewById(R.id.vacanciesTag);
        visaTag = view.findViewById(R.id.visaTag);
        moneyAndBanksTag = view.findViewById(R.id.moneyAndBanksTag);
        lookingForHelpTag = view.findViewById(R.id.lookingForHelpTag);
        otherTag = view.findViewById(R.id.otherTag);

        submit = view.findViewById(R.id.postButton);

        attachImage = view.findViewById(R.id.attachImage);
        progressBar = view.findViewById(R.id.progressBar);
        user = mAuth.getCurrentUser();

        try {
            MainActivity = (MainActivity) getActivity();
            assert MainActivity != null;
            userData = MainActivity.getUser();
        } catch (Exception e) {
            Admin Admin = (Admin) getActivity();
            assert Admin != null;
            userData = Admin.getUser();
        }


        titleField = view.findViewById(R.id.postTitle);
        bodyField = view.findViewById(R.id.postBody);
    }

    void selectButton(Button button) {
        accommodationsTag.setSelected(false);
        eventsTag.setSelected(false);
        vacanciesTag.setSelected(false);
        visaTag.setSelected(false);
        moneyAndBanksTag.setSelected(false);
        lookingForHelpTag.setSelected(false);
        otherTag.setSelected(false);

        button.setSelected(true);
    }


    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK
                        && result.getData() != null) {
                    Uri photoUri = result.getData().getData();
                    //use photoUri here


                    AssetFileDescriptor fileDescriptor = null;
                    try {
                        fileDescriptor = getActivity().getContentResolver().openAssetFileDescriptor(photoUri, "r");
                        long fileSize = fileDescriptor.getLength();
                        if (fileSize >= 10000000) {
                            toast("Image exceeds the maximum size 10MB");
                            return;
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                    attachImage.setImageURI(photoUri);
                    currentImage = photoUri;
                }
            }
    );


    private void publishPost() {
        String collection;
        if (userData.getRole().equals("Student")) {
            collection = "Posts";
        } else {
            collection = "PinnedPosts";
        }
        String timeStamp = new java.util.Date().toString();
        String tag = getSelectedTag();
        if (tag == null) {
            toast("Choose a category for your post");
            return;
        }
        if (titleField.getText().toString().length() < 5) {
            toast("Add post title");
            return;
        }
        if (bodyField.getText().toString().length() < 10) {
            toast("Add more words to the post");
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        storage = FirebaseStorage.getInstance();
        if (currentImage != null) {

            // Code for showing progressDialog while uploading
            ProgressBar ProgressBar
                    = new ProgressBar(getActivity());
            ProgressBar.setVisibility(View.VISIBLE);

            // Defining the child of storageReference
            StorageReference ref
                    = storage.getReference()
                    .child(
                            "Users/" + collection + "/"
                                    + user.getUid() + timeStamp);

            // adding listeners on upload
            // or failure of image
            ref.putFile(currentImage)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
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
                                            post post = new post(null, userData, downloadUrl.toString(), tag, titleField.getText().toString(), bodyField.getText().toString(), timeStamp);
                                            addToFirebase(post);

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


        } else {
            post post = new post(null, userData, null, tag, titleField.getText().toString(), bodyField.getText().toString(), timeStamp);
            addToFirebase(post);
        }

    }

    String getSelectedTag() {
        if (accommodationsTag.isSelected()) return accommodationsTag.getText().toString();
        if (eventsTag.isSelected()) return eventsTag.getText().toString();
        if (vacanciesTag.isSelected()) return vacanciesTag.getText().toString();
        if (visaTag.isSelected()) return visaTag.getText().toString();
        if (moneyAndBanksTag.isSelected()) return moneyAndBanksTag.getText().toString();
        if (lookingForHelpTag.isSelected()) return lookingForHelpTag.getText().toString();
        if (otherTag.isSelected()) return otherTag.getText().toString();

        return null;
    }

    void addToFirebase(post post) {
        String collection;
        if (userData.getRole().equals("Student")) {
            collection = "Posts";
        } else {
            collection = "PinnedPosts";
        }
        DocumentReference ref = db.collection(collection).document();
        post.setPostID(ref.getId());
        ref.set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        progressBar.setVisibility(View.INVISIBLE);

                        //add to RealTimeDB for comments

                        String ref = "/Posts/" + "/Users/" + user.getUid() + "/UserPosts/" + post.getPostID();
                        DatabaseReference mDatabase = realTimeDB.getReference(ref);
                        mDatabase.child("Comments").setValue("");

                        toast("Post added");
                        fragmentHome fragmentHome = new fragmentHome();
                        MainActivity.replaceFragment(fragmentHome, "Home");

                        if (userData.getRole().equals("Moderator")) {
                            sendNotification("The moderator shared a post");
                        }
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

    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    void sendNotification(String body) {
        notificationsSender.sendNotificationToAllUsers(body,"\"All\"");
    }
}
