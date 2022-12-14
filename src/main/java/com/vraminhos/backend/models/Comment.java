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
	private String id;

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
		this.id = new ObjectId().toString();
		this.createdAt = new Date();
	}

	public Comment(String id) {
		this.id = id;
	}

	public Comment(String message, User user) {
		this.id = new ObjectId().toString();
		this.createdAt = new Date();
		this.message = message;
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Comment comment) {
			return id.equals(comment.getId());
		}
		return false;
	}
}
