package com.chatApp.chatApp.repositories;

import com.chatApp.chatApp.model.InvalidatedToken;
import com.chatApp.chatApp.model.Permission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends MongoRepository<InvalidatedToken, String> {
}
