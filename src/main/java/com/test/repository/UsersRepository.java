package com.test.repository;

import com.test.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users,String> {
    Users findByUsername(String name);
}
