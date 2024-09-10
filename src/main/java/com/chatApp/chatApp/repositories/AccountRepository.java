package com.chatApp.chatApp.repositories;

import com.chatApp.chatApp.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends MongoRepository<Account, String> {
    boolean existsByUsername(String username);
    void deleteById(String id);
    Optional<Account> findByUsername(String username);
}
