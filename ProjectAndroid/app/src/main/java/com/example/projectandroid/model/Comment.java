package com.example.projectandroid.model;

import java.util.Date;

public class Comment {

	private int id_comment;

	private String content_commemt;

	private String commemt_date;

	private User user ;

	private Review review;

	public int getId_comment() {
		return id_comment;
	}

	public void setId_comment(int id_comment) {
		this.id_comment = id_comment;
	}

	public String getContent_commemt() {
		return content_commemt;
	}

	public void setContent_commemt(String content_commemt) {
		this.content_commemt = content_commemt;
	}

	public String getCommemt_date() {
		return commemt_date;
	}

	public void setCommemt_date(String commemt_date) {
		this.commemt_date = commemt_date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	@Override
	public String toString() {
		return "Comment{" +
				"id_comment=" + id_comment +
				", content_commemt='" + content_commemt + '\'' +
				", commemt_date='" + commemt_date + '\'' +
				", user=" + user +
				", review=" + review +
				'}';
	}
}
