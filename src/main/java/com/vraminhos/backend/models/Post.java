package com.vraminhos.backend.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "posts")
public class Post {

	@Id
	private String id;

	@NotBlank
	private String title;

	@NotBlank
	private String message;

	@CreatedDate
	@NotBlank
	private Date date;

	@NotBlank
	@DBRef(lazy = true)
	private User user;

	@DBRef(lazy = true)
	private Set<Post> replies = new HashSet<>();

	public Post() {
	}

	public Post(String title, String message, User user) {
		this.title = title;
		this.message = message;
		this.user = user;
	}

	public Post(String title, String message, Date date, User user, Set<Post> replies) {
		this.title = title;
		this.message = message;
		this.date = date;
		this.user = user;
		this.replies = replies;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Post> getReplies() {
		return replies;
	}

	public void setReplies(Set<Post> replies) {
		this.replies = replies;
	}
}
