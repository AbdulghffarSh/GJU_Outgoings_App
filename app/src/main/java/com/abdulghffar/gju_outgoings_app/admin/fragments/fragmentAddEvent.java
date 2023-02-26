package com.abdulghffar.gju_outgoings_app.admin.fragments;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.abdulghffar.gju_outgoings_app.R;
import com.abdulghffar.gju_outgoings_app.admin.Admin;
import com.abdulghffar.gju_outgoings_app.objects.event;
import com.abdulghffar.gju_outgoings_app.utils.notificationsSender;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.annotation.Nullable;

public class fragmentAddEvent extends Fragment {

    TextView fromDateField, toDateField, fromTimeField, toTimeField;
    EditText eventTitle, eventNotes;
    private SimpleDateFormat dateFormatter, timeFormatter;
    Button SendButton;
    String FilterToDate, FilterFromDate;

    Calendar fromTimeStamp, toTimeStamp;

    //Others
    View view;
    Admin Admin;
    FirebaseFirestore db;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        view = inflater.inflate(R.layout.activity_fragment_add_events, parent, false);

        setup();


        pickTime();

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FilterToDate = toDateField.getText().toString().trim();
                FilterFromDate = fromDateField.getText().toString().trim();

                if (eventTitle.getText().toString().matches("")) {
                    toast("Event title field is empty");
                    return;
                } else if (fromDateField.getText().toString().matches("")) {
                    toast("From date field is empty");
                    return;
                } else if (fromTimeField.getText().toString().matches("")) {
                    toast("From time field is empty");
                    return;
                } else if (toDateField.getText().toString().matches("")) {
                    toast("To date field is empty");
                    return;
                } else if (toTimeField.getText().toString().matches("")) {
                    toast("To time field is empty");
                    return;
                }
                addEvent();

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
        Admin = (Admin) getActivity();
        fromDateField = (TextView) view.findViewById(R.id.fromDate);
        toDateField = (TextView) view.findViewById(R.id.toDate);

        fromTimeField = (TextView) view.findViewById(R.id.fromTime);
        toTimeField = (TextView) view.findViewById(R.id.toTime);
        eventTitle = (EditText) view.findViewById(R.id.eventTitle);
        eventNotes = (EditText) view.findViewById(R.id.eventNotes);

        SendButton = (Button) view.findViewById(R.id.SendDateBtn);
        toTimeStamp = Calendar.getInstance(Locale.getDefault());
        fromTimeStamp = Calendar.getInstance(Locale.getDefault());
        db = FirebaseFirestore.getInstance();

    }


    void toast(String message) {
        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    void addEvent() {

        Intent calendarIntentVar = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, fromTimeStamp.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, toTimeStamp.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, eventTitle.getText().toString())
                .putExtra(CalendarContract.Events.DESCRIPTION, eventNotes.getText().toString());

        event event = new event(fromTimeStamp.getTime(), toTimeStamp.getTime(), eventTitle.getText().toString(), eventNotes.getText().toString());
        addToFirebase(event);
        try {
            startActivity(calendarIntentVar);
        } catch (Exception ErrVar) {
            toast("Install Calender App");
        }
    }

    void pickTime() {
        toDateField.setInputType(InputType.TYPE_NULL);
        fromDateField.setInputType(InputType.TYPE_NULL);
        toDateField.requestFocus();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("hh:mm aa", Locale.US);

        toDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the minimum date for the date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //todo
                                toTimeStamp.set(year, month, dayOfMonth);
                                toDateField.setText(dateFormatter.format(toTimeStamp.getTime()));
                            }
                        }, toTimeStamp.get(Calendar.YEAR), toTimeStamp.get(Calendar.MONTH), toTimeStamp.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // set the minimum date to today
                datePickerDialog.show();

            }
        });


        toTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {                                //todo
                                toTimeStamp.set(Calendar.HOUR_OF_DAY, hour);
                                toTimeStamp.set(Calendar.MINUTE, minute);
                                toTimeField.setText(timeFormatter.format(toTimeStamp.getTime()));
                            }
                        }, fromTimeStamp.get(Calendar.HOUR_OF_DAY), fromTimeStamp.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });

        fromDateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the minimum date for the date picker dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //todo
                                fromTimeStamp.set(year, month, dayOfMonth);
                                fromDateField.setText(dateFormatter.format(toTimeStamp.getTime()));
                            }
                        }, fromTimeStamp.get(Calendar.YEAR), toTimeStamp.get(Calendar.MONTH), fromTimeStamp.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000); // set the minimum date to today
                datePickerDialog.show();
            }
        });

        fromTimeField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onTimeSet(TimePicker timePicker, int hour, int minute) {                                //todo
                                fromTimeStamp.set(Calendar.HOUR_OF_DAY, hour);
                                fromTimeStamp.set(Calendar.MINUTE, minute);
                                fromTimeField.setText(timeFormatter.format(fromTimeStamp.getTime()));
                            }
                        }, fromTimeStamp.get(Calendar.HOUR_OF_DAY), fromTimeStamp.get(Calendar.MINUTE), false);
                timePickerDialog.show();
            }
        });


    }

    void addToFirebase(event event) {

        Admin.loadingUI(1);
        db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("Events").document();
        ref.set(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        //add to RealTimeDB for comments
                        toast("Event added");
                        sendNotification("The moderator shared an event");

                        Admin.loadingUI(0);


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Admin.loadingUI(0);

                    }
                });


    }

    void sendNotification(String body) {
        notificationsSender.sendNotificationToAllUsers(body);
    }

}