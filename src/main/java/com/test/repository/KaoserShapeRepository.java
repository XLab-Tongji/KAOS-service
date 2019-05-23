package com.test.repository;

import com.test.entity.KaoserShape;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KaoserShapeRepository extends MongoRepository<KaoserShape,String>{

    KaoserShape findBystyle(String s);
}
