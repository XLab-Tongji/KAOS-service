package com.test.repository;

import com.test.entity.KaoserFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KaoserFileRepository extends MongoRepository<KaoserFile,String>{

    KaoserFile findByName(String s);
    List<KaoserFile> findByMynameLike(String myName);
}
