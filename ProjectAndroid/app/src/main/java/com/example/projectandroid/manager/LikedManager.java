package com.example.projectandroid.manager;

import android.content.Context;
import android.util.Log;

import com.example.projectandroid.R;
import com.example.projectandroid.model.Review;
import com.example.projectandroid.task.WSTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class LikedManager {

    private static LikedManager likedManager;
    private Context context;

    public interface LikedManagerListener{
        void onComplete(Object response);

        void onError(String err);
    }

    public LikedManager(Context context){this.context = context;}

    public static LikedManager getLikedManager(Context context){
        if(likedManager == null) {
            likedManager = new LikedManager(context);
        }
        return likedManager;
    }

    public void doSelectCountLiked(int id_review , String username , final LikedManagerListener listener){
        Gson gson = new GsonBuilder().create();

        String rnu = id_review+"%"+username;

        String JSONRNU = gson.toJson(rnu);
        Log.e("user",JSONRNU);
        WSTask task = new WSTask(this.context , new WSTask.WSTaskListener(){
            @Override
            public void onComplete(String response) {
                Log.e("response like",response.toString());
                listener.onComplete(response);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);

            }
        } );
        task.execute(context.getString(R.string.selectCountLiked),JSONRNU);
    }

    public void doCheckLiked(int id_review , String username , final LikedManagerListener listener ){
        Gson gson = new GsonBuilder().create();

        String rnu = id_review+"%"+username;

        String JSON = gson.toJson(rnu);
        Log.e("JSON",JSON);

        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Log.e("ckecker ddddd " , response.toString());

                listener.onComplete(response);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.checkLiked),JSON);
    }

    public void doGetCount(int id , final LikedManagerListener listener){
        Gson gson = new GsonBuilder().create();
        String JSON = gson.toJson(id);
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                listener.onComplete(response);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.getCount),JSON);
    }

    public void doLiked(int id_review , String username , final LikedManagerListener listener ){
        Gson gson = new GsonBuilder().create();

        String rnu = id_review+"%"+username;

        String JSON = gson.toJson(rnu);
        Log.e("JSON",JSON);

        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                listener.onComplete(response);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.dolike),JSON);
    }

    public void doUnlike(int id_review , String username , final LikedManagerListener listener ){
        Gson gson = new GsonBuilder().create();

        String rnu = id_review+"%"+username;

        String JSON = gson.toJson(rnu);
        Log.e("JSON",JSON);

        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                listener.onComplete(response);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.doUnlike),JSON);
    }



}
