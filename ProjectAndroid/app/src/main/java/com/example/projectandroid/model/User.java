package com.example.projectandroid.model;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class User {

	private String username;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private String password;

	private String img_user;

	private String name;

	private String email;

	public String getPassword() {
		return password;
	}

	public String getImg_user() {
		return img_user;
	}

	public void setImg_user(String img_user) {
		this.img_user = img_user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User(String username, String password, String name, String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	public User() {
	}

	@Override
	public String toString() {
		return "User{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", img_user='" + img_user + '\'' +
				", name='" + name + '\'' +
				", email='" + email + '\'' +
				'}';
	}
}
