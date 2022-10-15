package com.vraminhos.backend.payload.requests;

import javax.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;

public class CommentRequest {

	@NotBlank
	private String message;

	@NotBlank
	private String username;

	@NotBlank
	private String postId;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}
}
