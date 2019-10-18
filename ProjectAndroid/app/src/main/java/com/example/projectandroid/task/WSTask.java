package com.example.projectandroid.task;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.example.projectandroid.R;
import com.example.projectandroid.model.ResponseModel;

/**
 * Created by titipong.k on 4/22/2017 AD.
 */

public class WSTask extends AsyncTask<String, String, String> {
    public interface WSTaskListener {
        void onComplete(String response);

        void onError(String err);
    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();
    private WSTaskListener listener;
    private Context context;

    public WSTask(Context context, WSTaskListener listener) {
        super();
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected String doInBackground(String... params) {
        String stringJson = "{}" ;
        try{
            if(params[1]!=null){
                stringJson = params[1];
            }
        }catch (ArrayIndexOutOfBoundsException a ){
            a.printStackTrace();
        }
        RequestBody body = RequestBody.create(JSON, stringJson);
        Request request = new Request.Builder()
                .url(context.getString(R.string.root_url).concat(params[0]))
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();

            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (listener == null)
            return;
        if (s == null) {
            listener.onError(context.getString(R.string.global));
            return;
        }
        Gson gson = new GsonBuilder().create();
        ResponseModel model = gson.fromJson(s, ResponseModel.class);
        if (!model.isSuccess()) {
            if(model.getResult() == null){
                listener.onError(context.getString(R.string.global));
                return;
            }
            listener.onError(model.getResult().toString());
            return;
        }
        listener.onComplete(model.getResult().toString());
    }
}
