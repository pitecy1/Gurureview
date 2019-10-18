package com.example.projectandroid.manager;

import android.content.Context;

import com.example.projectandroid.MainActivity;
import com.example.projectandroid.R;
import com.example.projectandroid.model.UserModel;
import com.example.projectandroid.task.WSTask;

public class LoginManager {
    private static LoginManager loginManager;
    private Context context;

    public interface LoginManagerListener{
        void onComplete(Object response);

        void onError(String err);
    }

    public LoginManager(Context context){this.context = context;}

    public static LoginManager getLoginManager(Context context){
        if(loginManager == null) {
            loginManager = new LoginManager(context);
        }
        return loginManager;
    }

    public void doLogin(Object object , final LoginManagerListener listener){
        if(!(object instanceof UserModel)){
            return;
        }
        UserModel userModel = (UserModel) object;
        WSTask task = new WSTask(this.context, new WSTask.WSTaskListener() {
            @Override
            public void onComplete(String response) {
                UserModel userModel = new UserModel(response);
                listener.onComplete(userModel);
            }

            @Override
            public void onError(String err) {
                listener.onError(err);
            }
        });
        task.execute(context.getString(R.string.login_url),userModel.toJSONString());
    }

    public void doRegister(Object object , final LoginManagerListener listener){
        if(!(object instanceof UserModel)){
            return;
        }
        final UserModel userModel = (UserModel) object;
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
        task.execute(context.getString(R.string.register_url),userModel.toJSONString());
    }

}
