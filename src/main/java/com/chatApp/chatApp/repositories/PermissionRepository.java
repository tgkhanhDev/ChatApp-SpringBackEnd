package com.chatApp.chatApp.repositories;

import com.chatApp.chatApp.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends MongoRepository<Permission, String> {
    Permission findByName(String name);
    void deleteByName(String name);
    
}
