package com.vraminhos.backend.graphql.controllers;

import com.vraminhos.backend.graphql.inputs.CommentInput;
import com.vraminhos.backend.graphql.inputs.PostInput;
import com.vraminhos.backend.models.Comment;
import com.vraminhos.backend.models.ERole;
import com.vraminhos.backend.models.Post;
import com.vraminhos.backend.models.Role;
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
import org.springframework.security.core.Authentication;
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

	@QueryMapping
	public User getUserDetailsById(@Argument String userId) {
		return userRepository.findById(userId).orElseThrow();
	}

	@QueryMapping
	public Iterable<Post> getAllPosts(@Argument Integer amount, @Argument Integer page) {
		if (amount != null) {
			return postRepository.findAll(PageRequest.of(page != null ? page : 0, amount));
		} else {
			return postRepository.findAll();
		}
	}

	@QueryMapping
	public Post getPostById(@Argument String postId) {
		return postRepository.findById(postId).orElseThrow();
	}

	@QueryMapping
	public Iterable<Post> getPostsByUsername(@Argument String username) {
		User user = userRepository.findByUsername(username).orElseThrow();
		return postRepository.findByUser(user).orElseThrow();
	}

	@QueryMapping
	public Iterable<Post> getTopPosts(@Argument int amount) {
		return postRepository.findTopPosts(PageRequest.of(0, amount)).orElseThrow();
	}

	@QueryMapping
	public Iterable<Post> getTopLikedPosts(@Argument int amount) {
		return postRepository.findAllByOrderByNumLikesDesc(PageRequest.of(0, amount)).orElseThrow();
	}

	@MutationMapping
	public Post createPost(@Argument PostInput postInput, Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		User user = userRepository.findById(((UserDetailsImpl) token.getPrincipal()).getId()).orElseThrow();

		Post newPost = new Post(postInput.title(), postInput.message(), postInput.category(), user);
		postRepository.insert(newPost);

		return newPost;
	}

	@MutationMapping
	public Post deletePostById(@Argument String postId, Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		User user = userRepository.findById(((UserDetailsImpl) token.getPrincipal()).getId()).orElseThrow();

		boolean isAdmin = false;
		for (Role role : user.getRoles()) {
			if (role.getName().equals(ERole.ROLE_ADMIN)) {
				isAdmin = true;
				break;
			}
		}

		System.out.println("isAdmin: " + isAdmin);

		Post post = postRepository.findById(postId).orElseThrow();

		if (post.getUser().equals(user)) {
			postRepository.delete(post);
			return post;
		}

		return null;
	}

	@MutationMapping
	public Post likePost(@Argument String postId, Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		User user = userRepository.findById(((UserDetailsImpl) token.getPrincipal()).getId()).orElseThrow();

		Post post = postRepository.findById(postId).orElseThrow();
		post.addLike(user);
		postRepository.save(post);

		return post;
	}

	@MutationMapping
	public Post unlikePost(@Argument String postId, Principal principal) {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
		User user = userRepository.findById(((UserDetailsImpl) token.getPrincipal()).getId()).orElseThrow();

		Post post = postRepository.findById(postId).orElseThrow();
		post.removeLike(user);
		postRepository.save(post);

		return post;
	}

	@MutationMapping
	public Post addComment(@Argument CommentInput commentInput, Authentication authentication) {
		User user = userRepository.findById(((UserDetailsImpl) authentication.getPrincipal()).getId()).orElseThrow();

		Post post = postRepository.findById(commentInput.postId()).orElseThrow();

		Comment comment = new Comment(commentInput.message(), user);
		post.addComment(comment);

		postRepository.save(post);

		return post;
	}

	@MutationMapping
	public Post removeCommentById(@Argument String commentId) {
		Post post = postRepository.findByCommentsId(commentId).orElseThrow();
		post.removeCommentById(commentId);
		postRepository.save(post);

		return post;
	}

}
