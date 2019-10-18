package com.example.projectandroid.model;



public class PhotoReview {

	private int id_photoreview;

	private int photo_review_no;

	private String photo_review_content;

	private String photo_review_url;

	private Review review;
	
	
	public int getid_photoreview() {
		return id_photoreview;
	}
	public void setid_photoreview(int id_photoreview) {
		this.id_photoreview = id_photoreview;
	}
	public int getPhoto_review_no() {
		return photo_review_no;
	}
	public void setPhoto_review_no(int photo_review_no) {
		this.photo_review_no = photo_review_no;
	}
	public String getPhoto_review_content() {
		return photo_review_content;
	}
	public void setPhoto_review_content(String photo_review_content) {
		this.photo_review_content = photo_review_content;
	}
	public Review getReview() {
		return review;
	}
	public void setReview(Review review) {
		this.review = review;
	}
	public int getId_photoreview() {
		return id_photoreview;
	}
	public void setId_photoreview(int id_photoreview) {
		this.id_photoreview = id_photoreview;
	}
	public String getPhoto_review_url() {
		return photo_review_url;
	}
	public void setPhoto_review_url(String photo_review_url) {
		this.photo_review_url = photo_review_url;
	}
	
	
}
