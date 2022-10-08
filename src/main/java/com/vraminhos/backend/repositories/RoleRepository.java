package com.vraminhos.backend.repositories;

import com.vraminhos.backend.models.ERole;
import com.vraminhos.backend.models.Role;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(ERole name);
}