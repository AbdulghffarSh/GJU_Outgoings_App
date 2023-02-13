package com.abdulghffar.gju_outgoings_app.fragments.navFragments;

import static android.content.ContentValues.TAG;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.db;
import static com.abdulghffar.gju_outgoings_app.database.firebaseDb.mAuth;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.activities.navBarActivities;
import com.abdulghffar.gju_outgoings_app.adapters.commentAdapter;
import com.abdulghffar.gju_outgoings_app.adapters.universityAdapter;
import com.abdulghffar.gju_outgoings_app.database.firebaseDb;
import com.abdulghffar.gju_outgoings_app.objects.city;
import com.abdulghffar.gju_outgoings_app.objects.comment;
import com.abdulghffar.gju_outgoings_app.objects.report;
import com.abdulghffar.gju_outgoings_app.objects.user;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.annotation.Nullable;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class fragmentCity extends Fragment {
    city cityData;
    RecyclerView commentsRecyclerView;
    RecyclerView universitiesRecyclerView;

    ArrayList<String> universityNames;
    ArrayList<comment> commentsArraylist;

    commentAdapter commentAdapter;
    universityAdapter universityAdapter;


    TextView cityName;
    TextView cityOverview;

    EditText commentField;
    ImageView cityPic;
    ImageView addCommentButton;

    FirebaseDatabase realTimeDB = firebaseDb.realDb;
    String pageUrl = null;

    navBarActivities navBarActivities;

    View view;
    ImageView wikiButton;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_city, parent, false);

        setup();
        getComments();
        getCityInfo(cityData.getCityName());

        Glide.get(getActivity()).getRegistry().prepend(StorageReference.class, InputStream.class, new FirebaseImageLoader.Factory());

        wikiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageUrl != null) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(pageUrl));
                    startActivity(i);
                }
            }
        });
        universityAdapter.setOnItemClickListener(new universityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");

            }
        });

        commentAdapter.setOnItemClickListener(new commentAdapter.OnItemClickListener() {
            @Override
            public void reportItemClick(int position) {
                checkReportDialog(position);
            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!commentField.getText().toString().matches("")) addComment();
            }

        });


        return view;
    }


    private void setData(city cityData) {
        cityName = view.findViewById(R.id.cityName);
        cityPic = view.findViewById(R.id.img);
        addCommentButton = view.findViewById(R.id.addCommentButton);

        cityName.setText(cityData.getCityName());
        if (cityData.getPics() != null) {
            try {
                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("citiesImages/" + cityData.getCityName() + "/image1.jpg");
                Glide.with(this).load(storageRef).into(cityPic);
            } catch (Exception e) {
                System.out.println(e.toString());

            }

        }
    }


    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
    }

    public void changeItem(int position, String text) {

        navBarActivities.setUniversity(position);
        fragmentUniversity fragmentUniversity = new fragmentUniversity();
        navBarActivities.replaceFragment(fragmentUniversity);


    }

    void getComments() {
        navBarActivities.loadingUI(1);
        realTimeDB = FirebaseDatabase.getInstance("https://gju-outgings-app-24c61-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = realTimeDB.getReference("/Cities/" + cityData.getCityName() + "/Comments/");
        commentsArraylist.clear();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    String Uid = snapshot.child("uid").getValue(String.class);
                    String timeStamp = snapshot.child("timeStamp").getValue(String.class);
                    String comment = snapshot.child("commentText").getValue(String.class);
                    String ref = snapshot.child("reference").getValue(String.class);


                    DocumentReference docRef = db.collection("Users").document(Uid);
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            user user = documentSnapshot.toObject(user.class);
                            comment commentObject = new comment(comment, Uid, timeStamp, user, ref);
                            commentsArraylist.add(commentObject);
                            commentAdapter.notifyDataSetChanged();

                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        navBarActivities.loadingUI(0);

    }

    void addComment() {

        String commentText = commentField.getText().toString();
        String Uid = mAuth.getUid();
        String timeStamp = new java.util.Date().toString();
        comment newComment = new comment(commentText, Uid, timeStamp, null, null);
        String cityName = cityData.getCityName();

        System.out.println("This is the city name " + cityName);

        DatabaseReference mDatabase = realTimeDB.getReference("/Cities/" + cityData.getCityName());
        String ref = "/Cities/" + cityData.getCityName() + "/Comments/" + timeStamp;
        newComment.setReference(ref);
        mDatabase.child("Comments").child(timeStamp).setValue(newComment).toString();
        toast("Comment added");
        commentField.setText("");
        closeKeyboard();
        getComments();

    }

    void setup() {
        wikiButton = view.findViewById(R.id.wikiButton);
        navBarActivities = (navBarActivities) getActivity();
        assert navBarActivities != null;
        cityData = navBarActivities.getCityData();
        setData(cityData);

        universityNames = new ArrayList<>(cityData.getUniversities().keySet());
        commentsArraylist = new ArrayList<>();
        commentsRecyclerView = view.findViewById(R.id.commentsRecyclerView);

        universitiesRecyclerView = view.findViewById(R.id.universitiesRecyclerView);
        universityAdapter = new universityAdapter(universityNames);
        universitiesRecyclerView.setHasFixedSize(true);
        universitiesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        universitiesRecyclerView.setAdapter(universityAdapter);

        commentAdapter = new commentAdapter(commentsArraylist);
        commentsRecyclerView.setHasFixedSize(true);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        commentsRecyclerView.setAdapter(commentAdapter);

        commentField = view.findViewById(R.id.commentField);
        cityOverview = view.findViewById(R.id.cityOverview);

    }

    void toast(String message) {
        Toast toast = Toast.makeText(view.getContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    void reportComment(int position) {
        navBarActivities.loadingUI(1);

        comment selectedComment = commentsArraylist.get(position);
        ArrayList<String> reportedBy = new ArrayList<>();
        reportedBy.add(mAuth.getUid());
        report report = new report(selectedComment.getCommentText(), selectedComment.getReference(), selectedComment.getTimeStamp(), selectedComment.getUid(), reportedBy);
        String myUid = mAuth.getUid();

        DocumentReference docRef = db.collection("Reports").document(selectedComment.getTimeStamp());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                boolean userAlreadyReported = false;
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        report existReport = document.toObject(report.class);
                        assert existReport != null;
                        ArrayList<String> reportedBy = existReport.getUsers();
                        for (String uid : reportedBy) {
                            assert myUid != null;
                            if (uid.contains(myUid)) {
                                toast("You have already reported this comment");
                                userAlreadyReported = true;
                            }
                        }
                        if (!userAlreadyReported) {
                            reportedBy.add(myUid);
                            db.collection("Reports").document(selectedComment.getTimeStamp()).set(existReport).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                    toast("Reported");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);

                                }
                            });
                        }

                    } else {
                        Log.d(TAG, "No such document");
                        db.collection("Reports").document(selectedComment.getTimeStamp()).set(report).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);

                            }
                        });
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        navBarActivities.loadingUI(0);

    }

    void checkReportDialog(int position) {
        ViewGroup subView = (ViewGroup) getLayoutInflater().// inflater view
                inflate(R.layout.update_data_dialog, null, false);
        EditText newData = subView.findViewById(R.id.editText);
        subView.removeView(newData);
        TextView messageField = subView.findViewById(R.id.text);

        messageField.setText("Are you sure you want to report this comment?");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);

        builder.setView(subView);

        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                reportComment(position);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != getActivity().getCurrentFocus())
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getApplicationWindowToken(), 0);
    }


    private void getCityInfo(String city) {
        String url = "https://en.wikipedia.org/w/api.php?action=query&prop=extracts&format=json&titles=" + city;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error making Wikipedia API request", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.e(TAG, "Wikipedia API request failed with code: " + response.code());
                    return;
                }
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    JSONObject query = result.getJSONObject("query");
                    JSONObject pages = query.getJSONObject("pages");
                    String pageId = pages.keys().next();
                    JSONObject page = pages.getJSONObject(pageId);
                    final String extract = page.getString("extract");
                    pageUrl = "https://en.wikipedia.org/?curid=" + pageId;

                    final String withoutHtml = HtmlCompat.fromHtml(extract, HtmlCompat.FROM_HTML_MODE_LEGACY).toString();

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cityOverview.setText(withoutHtml);
                            }
                        });
                    }


                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing Wikipedia API response", e);
                }
            }
        });
    }


}