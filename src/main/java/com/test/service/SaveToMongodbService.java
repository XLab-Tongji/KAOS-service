package com.test.service;

import com.test.entity.KaoserFile;
import com.test.repository.KaoserFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaveToMongodbService {
    @Autowired
    private KaoserFileRepository kaoserFileRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    public String doSaveToMongodb(String title,String jsonGet,String myName,String projectName)throws IOException {
        System.out.println(title);
        System.out.println(jsonGet);

        KaoserFile jsoninfo=new KaoserFile();
        jsoninfo.setName(title);
        jsoninfo.setJsonStr(jsonGet);
        jsoninfo.setMyname(myName);
        jsoninfo.setProjectname(projectName);

        String resp="";
        System.out.println(ResourceUtils.getURL("classpath").getPath());
        Criteria criatira = new Criteria();
        criatira.andOperator(Criteria.where("myname").is(myName), Criteria.where("projectname").is(title),Criteria.where("name").is(title));
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

    public String updateToMongo(String id,String name,String jsonStr){
        Query query=new Query(Criteria.where("_id").is(id));
        Update update = Update.update("name", name).set("jsonStr",jsonStr);
        mongoTemplate.updateMulti(query, update, KaoserFile.class);
        String resp = "";
        resp = "{\"name\":\"success\"}";
        return resp;
    }

    public KaoserFile addNewFile(String username,
                                         String projectName,
                                         String title){
        KaoserFile jsoninfo=new KaoserFile();
        jsoninfo.setName(title);
        jsoninfo.setJsonStr("");
        jsoninfo.setMyname(username);
        jsoninfo.setProjectname(projectName);
        mongoTemplate.save(jsoninfo);

        return jsoninfo;
    }
}
