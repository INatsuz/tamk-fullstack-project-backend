package com.vraminhos.backend.repositories;

import com.vraminhos.backend.models.Post;
import com.vraminhos.backend.models.User;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {

	Optional<HashSet<Post>> findByUser(User user);

	Optional<List<Post>> findAllByOrderByNumLikesDesc(Pageable pageable);

	Optional<Post> findByCommentsId(String id);

	@Aggregation(pipeline = {"{$set: {totalInteraction: {$add: ['$numLikes', '$numComments']}}}", "{$sort: {'totalInteraction': -1}}", "{$project: {'totalInteraction': 0}}"})
	Optional<List<Post>> findTopPosts(Pageable pageable);

}
