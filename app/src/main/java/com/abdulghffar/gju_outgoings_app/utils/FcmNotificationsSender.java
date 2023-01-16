package com.abdulghffar.gju_outgoings_app.utils;


import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FcmNotificationsSender {


    final static String app_id = "5dd645a1-2b50-4708-b7ae-a297bc750799";
    final static String REST_API_KEY ="MmE2YzZlMWUtMDc4OS00MDU1LTg3NWYtOGIxOGMwN2U5YzZm";



    public void sendNotification(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{\"app_id\":\""+app_id+"\",\"contents\":{\"en\":\"English or Any Language Message\"}");
                    Request request = new Request.Builder()
                            .url("https://onesignal.com/api/v1/notifications")
                            .post(body)
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Authorization", "Basic "+REST_API_KEY)
                            .build();
                    Response response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

}
