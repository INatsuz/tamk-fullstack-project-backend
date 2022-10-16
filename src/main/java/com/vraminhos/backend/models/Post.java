package com.vraminhos.backend.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "posts")
public class Post {

	@Id
	private String id;

	@NotBlank
	private String title;

	@NotBlank
	private String message;

	@NotBlank
	private String category;

	@CreatedDate
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	@NotBlank
	private Date createdAt;

	@NotBlank
	@DBRef(lazy = true)
	private User user;

	private int numComments = 0;

	private Set<Comment> comments = new HashSet<>();

	private int numLikes = 0;

	@DBRef(lazy = true)
	private Set<User> likes = new HashSet<>();

	public Post() {
	}

	public Post(String title, String message, String category, User user) {
		this.title = title;
		this.message = message;
		this.category = category;
		this.user = user;
	}

	public Post(String title, String message, String category, Date createdAt, User user, Set<Comment> comments) {
		this.title = title;
		this.message = message;
		this.createdAt = createdAt;
		this.user = user;
		this.comments = comments;
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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getNumComments() {
		return numComments;
	}

	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public void addComment(Comment comment) {
		comments.add(comment);
		numComments = comments.size();
	}

	public void removeCommentById(String id) {
		comments.remove(new Comment(id));
		numComments = comments.size();
	}

	public int getNumLikes() {
		return numLikes;
	}

	public void setNumLikes(int numLikes) {
		this.numLikes = numLikes;
	}

	public Set<User> getLikes() {
		return likes;
	}

	public void setLikes(Set<User> likes) {
		this.likes = likes;
	}

	public void addLike(User user) {
		likes.add(user);
		numLikes = likes.size();
	}

	public void removeLike(User user) {
		likes.remove(user);
		numLikes = likes.size();
	}

}
