package com.test.service;

import com.test.entity.KaoserFile;
import com.test.entity.KaoserShape;
import com.test.repository.KaoserFileRepository;
import com.test.repository.KaoserShapeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SaveToMongodbService {
    @Autowired
    private KaoserFileRepository kaoserFileRepository;

    @Autowired
    private KaoserShapeRepository kaoserShapeRepository;

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
        //System.out.println(ResourceUtils.getURL("classpath").getPath());
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

    public String doSaveShapeToMongodb(String id,String name,String style,int width,int height,String attribute)throws IOException {
        String resp;
        KaoserShape myshape=new KaoserShape();
        myshape.setHeight(height);
        myshape.setName(name);
        myshape.setId(id);
        myshape.setStyle(style);
        myshape.setAttribute(attribute);
        myshape.setWidth(width);

        Criteria criatira = new Criteria();
        criatira.andOperator(Criteria.where("id").is(id), Criteria.where("style").is(style),Criteria.where("name").is(name));
        if(mongoTemplate.count(new Query(criatira), KaoserShape.class)>0){
            resp = "{\"name\":\"fail\"}";
        }
        else{
            mongoTemplate.save(myshape);
            resp = "{\"name\":\"success\"}";
        }
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

    public List<KaoserShape> findMyShapeByNameAndId(String id){
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("id").is(id));
        List result = mongoTemplate.find(new Query(criteria),KaoserShape.class);
        return result;
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
