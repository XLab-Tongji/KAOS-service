package com.test.service;

import com.test.entity.KaoserFile;
import com.test.repository.KaoserFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

@Service
public class SaveToMongodbService {
    @Autowired
    private KaoserFileRepository kaoserFileRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    public String doSaveToMongodb(String jsonName,String jsonGet,String myName,String title)throws IOException {
        System.out.println(jsonName);
        System.out.println(jsonGet);

        KaoserFile jsoninfo=new KaoserFile();
        jsoninfo.setName(jsonName);
        jsoninfo.setJsonStr(jsonGet);
        jsoninfo.setMyname(myName);
        jsoninfo.setProjectname(title);

        String resp="";
        System.out.println(ResourceUtils.getURL("classpath").getPath());
        Criteria criatira = new Criteria();
        criatira.andOperator(Criteria.where("myname").is(myName), Criteria.where("projectname").is(title),Criteria.where("name").is(jsonName));
        //mongoTemplate.find(new Query(criatira), KaoserFile.class);
        if(mongoTemplate.count(new Query(criatira), KaoserFile.class)>0){
            resp = "{\"name\":\"fail\"}";
        }
        else{
            kaoserFileRepository.save(jsoninfo);
            resp = "{\"name\":\"success\"}";
        }
        System.out.println(resp);
        return resp;
    }
}
