package com.vraminhos.backend.models;

import java.util.Date;
import javax.validation.constraints.NotBlank;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.format.annotation.DateTimeFormat;

public class Comment {

	@Id
	private String Id;

	@NotBlank
	private String message;

	@NotBlank
	@DBRef(lazy = true)
	private User user;

	@CreatedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@NotBlank
	private Date createdAt;

	public Comment() {
		this.Id = new ObjectId().toString();
		this.createdAt = new Date();
	}

	public Comment(String message, User user) {
		this.Id = new ObjectId().toString();
		this.createdAt = new Date();
		this.message = message;
		this.user = user;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
