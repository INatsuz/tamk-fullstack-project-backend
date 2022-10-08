package com.vraminhos.backend.controllers;

import com.vraminhos.backend.models.Post;
import com.vraminhos.backend.models.User;
import com.vraminhos.backend.repositories.PostRepository;
import com.vraminhos.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

@Controller
public class GraphQLController {

	UserRepository userRepository;
	PostRepository postRepository;

	@Autowired
	public GraphQLController(UserRepository userRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@PreAuthorize("#username.equals(authentication.principal.username)")
	@QueryMapping
	public Iterable<Post> postsByUsername(@Argument String username) {
		User user = userRepository.findByUsername(username).orElseThrow();

		return postRepository.findByUser(user).orElseThrow();
	}

}
