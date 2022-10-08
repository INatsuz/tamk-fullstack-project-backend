package com.vraminhos.backend.controllers;

import com.vraminhos.backend.models.Post;
import com.vraminhos.backend.models.User;
import com.vraminhos.backend.repositories.PostRepository;
import com.vraminhos.backend.repositories.RoleRepository;
import com.vraminhos.backend.repositories.UserRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
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
		List<String> messages = new ArrayList<String>();
		posts.forEach(post -> messages.add(post.getMessage()));

		return messages.toString();
	}

	@GetMapping("/add-message")
	@RolesAllowed({"ROLE_USER"})
	public String addMessage() {
		User user = userRepository.findByUsername("inatsuz").orElseThrow();

		Post post = new Post("Post Title Here", "Post Message goes in here. What are you waiting for? Start writing...", user);
		postRepository.insert(post);

		return "Success";
	}

	@GetMapping("/add-user")
	public String addUser() {
		User user = new User("vasco", "vasconunoraminhos@gmail.com", passwordEncoder.encode("1999"));

		userRepository.insert(user);

		return "Success";
	}

}
