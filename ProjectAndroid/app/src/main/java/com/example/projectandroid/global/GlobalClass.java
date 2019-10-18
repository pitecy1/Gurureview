package com.example.projectandroid.global;

import android.app.Application;

import com.example.projectandroid.model.User;

public class GlobalClass extends Application {
    User user = null ;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
