package com.vraminhos.backend.repositories;

import com.vraminhos.backend.models.Post;
import com.vraminhos.backend.models.User;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {

	Optional<HashSet<Post>> findByUser(User user);

}
