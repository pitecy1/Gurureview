package com.example.projectandroid.model;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Review {

	private int id_review ;
	
	public int getId_review() {
		return id_review;
	}

	public void setId_review(int id_review) {
		this.id_review = id_review;
	}

	private String description;

	private int viewer;

	private String url_img;

	private User user;
	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	private Category category;
	
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getViewer() {
		return viewer;
	}

	public void setViewer(int viewer) {
		this.viewer = viewer;
	}

	public String getUrl_img() {
		try{
			this.url_img = URLEncoder.encode(url_img,"UTF-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		return url_img;
	}

	public void setUrl_img(String url_img) {
		try{
			this.url_img = URLEncoder.encode(url_img,"UTF-8");
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	
}
