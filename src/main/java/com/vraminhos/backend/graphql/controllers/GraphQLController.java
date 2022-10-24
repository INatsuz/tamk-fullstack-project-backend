package com.vraminhos.backend.graphql.controllers;

import com.vraminhos.backend.graphql.inputs.PostInput;
import com.vraminhos.backend.models.Post;
import com.vraminhos.backend.models.User;
import com.vraminhos.backend.repositories.PostRepository;
import com.vraminhos.backend.repositories.UserRepository;
import com.vraminhos.backend.security.services.UserDetailsImpl;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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

	@QueryMapping
	public Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

//	@PreAuthorize("#username.equals(authentication.principal.username)")
	@QueryMapping
	public Iterable<Post> getAllPosts() {
		return postRepository.findAll();
	}

//	@PreAuthorize("#username.equals(authentication.principal.username)")
	@QueryMapping
	public Iterable<Post> getPostsByUsername(@Argument String username) {
		User user = userRepository.findByUsername(username).orElseThrow();

		return postRepository.findByUser(user).orElseThrow();
	}

//	@PreAuthorize("#username.equals(authentication.principal.username)")
	@QueryMapping
	public Iterable<Post> getTopPosts() {
		return postRepository.findTopPosts().orElseThrow();
	}

//	@PreAuthorize("#username.equals(authentication.principal.username)")
	@QueryMapping
	public Iterable<Post> getTop10LikedPosts() {
		return postRepository.findAllByOrderByNumLikesDesc(PageRequest.of(0, 3)).orElseThrow();
	}

	@MutationMapping
	public Post createPost(@Argument PostInput postInput, Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		User user = userRepository.findById(((UserDetailsImpl)token.getPrincipal()).getId()).orElseThrow();

		Post newPost = new Post(postInput.title(), postInput.message(), postInput.category(), user);
		postRepository.insert(newPost);

		return newPost;
	}

	@MutationMapping
	public Post likePost(@Argument String postId, Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		User user = userRepository.findById(((UserDetailsImpl)token.getPrincipal()).getId()).orElseThrow();

		Post post = postRepository.findById(postId).orElseThrow();
		post.addLike(user);
		postRepository.save(post);

		return post;
	}

	@MutationMapping
	public Post unlikePost(@Argument String postId, Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		User user = userRepository.findById(((UserDetailsImpl)token.getPrincipal()).getId()).orElseThrow();

		Post post = postRepository.findById(postId).orElseThrow();
		post.removeLike(user);
		postRepository.save(post);

		return post;
	}

	@MutationMapping
	public Post removeCommentById(@Argument String commentId, Principal principal) {
		Post post = postRepository.findByCommentsId(commentId).orElseThrow();
		post.removeCommentById(commentId);
		postRepository.save(post);

		return post;
	}

}
