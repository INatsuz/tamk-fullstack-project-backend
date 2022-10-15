package com.vraminhos.backend.controllers;

import com.vraminhos.backend.models.Comment;
import com.vraminhos.backend.models.Post;
import com.vraminhos.backend.models.User;
import com.vraminhos.backend.payload.requests.CommentRequest;
import com.vraminhos.backend.payload.requests.ForumPostRequest;
import com.vraminhos.backend.repositories.PostRepository;
import com.vraminhos.backend.repositories.RoleRepository;
import com.vraminhos.backend.repositories.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIController {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PostRepository postRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	public APIController(RoleRepository roleRepository, UserRepository userRepository, PostRepository postRepository) {
		this.roleRepository = roleRepository;
		this.userRepository = userRepository;
		this.postRepository = postRepository;
	}

	@GetMapping("/messages")
	@RolesAllowed({"ROLE_USER"})
	public String getMessagesByINatsuz() {
		User user = userRepository.findByUsername("inatsuz").orElseThrow();
		HashSet<Post> posts = postRepository.findByUser(user).orElseThrow();
		List<String> messages = new ArrayList<>();
		posts.forEach(post -> messages.add(post.getMessage()));

		return messages.toString();
	}

	@GetMapping("/addPost")
	public String addPost(@Valid @RequestBody ForumPostRequest postRequest) {
		User user = userRepository.findByUsername(postRequest.getUsername()).orElseThrow();

		Post post = new Post(postRequest.getTitle(), postRequest.getMessage(), postRequest.getCategory(), user);
		postRepository.insert(post);

		return "Success";
	}

	@GetMapping("/addComment")
	public String addComment(@Valid @RequestBody CommentRequest commentRequest) {
		User user = userRepository.findByUsername(commentRequest.getUsername()).orElseThrow();
		Post post = postRepository.findById(commentRequest.getPostId()).orElseThrow();

		Comment comment = new Comment(commentRequest.getMessage(), user);
		post.getComments().add(comment);
		postRepository.save(post);

		return "Success";
	}

	@GetMapping("/add-user")
	public String addUser() {
		User user = new User("vasco", "vasconunoraminhos@gmail.com", passwordEncoder.encode("1999"));

		userRepository.insert(user);

		return "Success";
	}

}
