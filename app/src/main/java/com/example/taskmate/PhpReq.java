package com.example.taskmate;

import android.app.Activity;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.*;

public class PhpReq {
    public void sendRequest(Activity a, Request req, RequestHandler rh){

        OkHttpClient client = new OkHttpClient();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()){
                    final String responseData = response.body().string();
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rh.processResponse(responseData);
                        }
                    });
                }else{
                    System.out.println("Request Failed");
                }
            }
        });

    }

}
