package com.chatApp.chatApp.repositories;

import com.chatApp.chatApp.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
    Optional<Permission> findByName(String name);
    void deleteByName(String name);
    boolean existsByName(String name);
}
