package com.vraminhos.backend.graphql.controllers;

import com.vraminhos.backend.models.Post;
import com.vraminhos.backend.models.User;
import com.vraminhos.backend.repositories.PostRepository;
import com.vraminhos.backend.repositories.UserRepository;
import com.vraminhos.backend.security.services.UserDetailsImpl;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@Controller
@CrossOrigin
public class GraphQLController {
	UserRepository userRepository;
	PostRepository postRepository;

	@Autowired
	public GraphQLController(UserRepository userRepository, PostRepository postRepository) {
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@QueryMapping
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

//	@PreAuthorize("#username.equals(authentication.principal.username)")
	@PreAuthorize("hasRole('ROLE_USER')")
	@QueryMapping
	public Iterable<Post> getAllPosts() {
		return postRepository.findAll();
	}

//	@PreAuthorize("#username.equals(authentication.principal.username)")
	@PreAuthorize("hasRole('ROLE_USER')")
	@QueryMapping
	public Iterable<Post> getPostsByUsername(@Argument String username) {
		User user = userRepository.findByUsername(username).orElseThrow();

		return postRepository.findByUser(user).orElseThrow();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@MutationMapping
	public Post createPost(@Argument PostInput postInput, Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;

		User user = userRepository.findById(((UserDetailsImpl)token.getPrincipal()).getId()).orElseThrow();
		Post newPost = new Post(postInput.title, postInput.message, postInput.category, user);
		postRepository.insert(newPost);

		return newPost;
	}

	record PostInput(String title, String message, String category) { }

}
