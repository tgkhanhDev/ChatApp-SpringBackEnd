package com.chatApp.chatApp.repositories;

import com.chatApp.chatApp.dto.Response.RoleResponse;
import com.chatApp.chatApp.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByName(String name);
    void deleteByName(String name);
    boolean existsByName(String name);
}
