package com.test.service;

import com.test.entity.KaoserFile;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FindByMynameService {
    @Autowired
    MongoTemplate mongoTemplate;
    @Transactional
    public JSONArray find(String myname){
        Query query = new Query(Criteria.where("myname").is(myname));
        List<KaoserFile> kaoserFiles = mongoTemplate.find(query,KaoserFile.class);
        JSONArray jsonArray = JSONArray.fromObject(kaoserFiles);
        return jsonArray;
    }
}
