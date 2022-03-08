package com.example.juwelierbehrendt.ExternalLibs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EasyNW {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public class nwResponse {
        boolean succeeded=false;
        String response="nothing";
        byte[] bytes;
        public nwResponse(boolean succeeded,String response)
        {
            this.succeeded=succeeded;
            this.response=response;
        }
        public nwResponse(boolean succeeded,byte[] bytes)
        {
            this.succeeded=succeeded;
            this.bytes=bytes;
            this.response = String.valueOf(bytes);
        }
        public boolean getSucceeded()
        {
            return succeeded;
        }
        public byte[] getBytes()
        {
            return bytes;
        }
        public String getRespone()
        {
            return response;
        }
    }

    String url;
    boolean succeeded=false;
    boolean answerRecieved=false;
    String stringResponse="";
    byte[] responseBytes;
    String email, pw;
    public EasyNW(String url) {
        this.url = url;
    }
    public EasyNW(String url, String email, String pw) {
        this.url = url;
        this.email = email;
        this.pw = pw;
    }
    //Use new HashMap<String, String>()
    public RequestBody map2RequestBody(Map<String, String> keysForJSON) {
        //JSON Datei erstellen
        JSONObject data = new JSONObject();
        try {
            Set<String> set = keysForJSON.keySet();
            for (int i = 0; i < keysForJSON.size(); i++) {
                data.put(set.toArray()[i].toString(), keysForJSON.get(set.toArray()[i].toString()));
            }
        } catch (JSONException e) {
            Log.i("RESPONSE", "JSON creation failed!");
        }
        return RequestBody.create(JSON, data.toString().getBytes());
    }
    public nwResponse sendNwRequest(String type, String action, HashMap<String, String> body, HashMap<String, String> header)
    {

        RequestBody bodyformated = map2RequestBody(body);

        Request.Builder builder = new Request.Builder();
        builder.url(url + action);

        switch(type){
            case "POST":
                builder.post(bodyformated);
                break;
            case "PATCH":
                builder.patch(bodyformated);
                break;
            case "DELETE":
                builder.delete(bodyformated);
                break;
            case "GET":
                builder.get();
                break;
        }
        //Header setzen
        Set<String> set = header.keySet();
        for (int i = 0; i < header.size(); i++) {
            builder.addHeader(set.toArray()[i].toString(), header.get(set.toArray()[i].toString()));
        }

        Request request = builder.build();
        return sendNetworkRequest(request);
    }
    public nwResponse sendNetworkRequest(Request request) {

        final OkHttpClient client = new OkHttpClient();
        answerRecieved=false;
        succeeded = false;
        client.newCall(request).enqueue(new Callback() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(/*@NotNull*/ Call call, /*@NotNull*/ IOException e) {
                Log.i("RESPODE(failed):", Objects.requireNonNull(e.getMessage()));
                answerRecieved=true;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(/*@NotNull*/ Call call, /*@NotNull*/ Response response) throws IOException {
                Log.i("RESPODE:", response.message());
                //responseInputStream = response.body().byteStream();
                //stringResponse = Objects.requireNonNull(response.body()).string();
                responseBytes = response.body().bytes();
                answerRecieved=true;
                if (response.message().equals("OK") || response.message().equals("true")) {
                    succeeded = true;
                }else if(response.message().equals("Bad Request"))
                {
                    succeeded = false;
                }
            }
        });
        while(!answerRecieved) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new EasyNW.nwResponse(succeeded, responseBytes);
    }
}

