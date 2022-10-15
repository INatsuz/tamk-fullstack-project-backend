package com.vraminhos.backend.payload.requests;

import javax.validation.constraints.NotBlank;

public class ForumPostRequest {

	@NotBlank
	private String title;

	@NotBlank
	private String message;

	@NotBlank
	private String category;

	@NotBlank
	private String username;

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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
