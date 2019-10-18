package com.example.projectandroid.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserModel {
    private User user;

    public UserModel(){
        user = new User();
    }

    public UserModel(String jsonResponse){
        Gson gson = new GsonBuilder().create();
        user = gson.fromJson(jsonResponse , User.class);
    }

    public String toJSONString(){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this.user);
    }

    public User getUser() {
        return user;
    }

    public class User{


        private String username;
        private String password;
        private String img_user;
        private String name;
        private String email;

        public User() {
        }

        public User(String username, String password, String img_user, String name, String email) {
            this.username = username;
            this.password = password;
            this.img_user = img_user;
            this.name = name;
            this.email = email;
        }

        public String getImg_user() {
            return img_user;
        }

        public void setImg_user(String img_user) {
            this.img_user = img_user;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
