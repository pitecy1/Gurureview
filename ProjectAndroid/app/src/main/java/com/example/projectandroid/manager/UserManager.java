package com.example.projectandroid.manager;

import android.content.Context;
import android.util.Log;

import com.example.projectandroid.R;
import com.example.projectandroid.model.User;
import com.example.projectandroid.task.WSTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UserManager {
    private static UserManager userManager;
    private Context context;

    public interface UserManagerListener{
        void onComplete(Object response);

        void onError(String err);
    }

    public UserManager(Context context){this.context = context;}

    public static UserManager getUserManager(Context context){
        if(userManager == null){
            userManager = new UserManager(context);
        }
        return userManager;
    }

    public void doUpdateImgUser(Object object , final UserManagerListener listener){
        if(!(object instanceof User)){
            Log.e("step","return");
            return;
        }
        String encode = "";
        User user = (User) object;
        try {
            encode = URLEncoder.encode(  user.getImg_user(),"UTF-8");
            String eName = URLEncoder.encode(user.getName(),"UTF-8");
            user.setImg_user(encode);
            user.setName(eName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(user);
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
        task.execute(context.getString(R.string.updateUrlProfile),json);
    }

    public void doUpdateUser(Object object , final UserManagerListener listener){
        if(!(object instanceof User)){
            Log.e("step","return");
            return;
        }
        String encode = "";
        User user = (User) object;
        try {
            if(user.getImg_user() != null){
                encode = URLEncoder.encode(  user.getImg_user(),"UTF-8");
                user.setImg_user(encode);
            }
            String eName = URLEncoder.encode(user.getName(),"UTF-8");

            user.setName(eName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(user);
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                Gson gson = new GsonBuilder().create();
                User user = gson.fromJson(response,User.class);
                listener.onComplete(user);
            }
            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.updateUser),json);
    }



    public void doConvertPasswordToSHA(String pass , final UserManagerListener listener){
        String ePass = "";
        try {
            ePass = URLEncoder.encode(pass,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(ePass);
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
        task.execute(context.getString(R.string.convertpassword),json);
    }


}
