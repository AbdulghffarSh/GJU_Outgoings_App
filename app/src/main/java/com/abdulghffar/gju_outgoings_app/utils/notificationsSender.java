package com.abdulghffar.gju_outgoings_app.utils;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class notificationsSender {

    public final static String app_id = "5dd645a1-2b50-4708-b7ae-a297bc750799";
    private final static String REST_API_KEY = "MmE2YzZlMWUtMDc4OS00MDU1LTg3NWYtOGIxOGMwN2U5YzZm";

    public static void sendNotification(String message,String playerId) {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
// Create the JSON body of the request
        String jsonBody = "{\"app_id\":\""+app_id+"\", \"include_player_ids\":[\""+playerId+"\"],\"contents\":{\"en\":\""+message+"\"}}";
        RequestBody body = RequestBody.create(mediaType, jsonBody);

// Create the request
        Request request = new Request.Builder()
                .url("https://onesignal.com/api/v1/notifications")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic "+REST_API_KEY)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // you code to handle response
            }
        });


    }


    public static void sendNotificationToAllUsers(String message) {

        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
// Create the JSON body of the request
        String jsonBody = "{\"app_id\":\""+app_id+"\",\"included_segments\":[\"All\"],\"contents\":{\"en\":\""+message+"\"}}";
        RequestBody body = RequestBody.create(mediaType, jsonBody);

// Create the request
        Request request = new Request.Builder()
                .url("https://onesignal.com/api/v1/notifications")
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Basic "+REST_API_KEY)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                // you code to handle response
            }
        });}

}

