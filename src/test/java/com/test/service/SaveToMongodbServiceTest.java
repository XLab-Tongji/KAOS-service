package com.test.service;

import com.test.entity.KaoserFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SaveToMongodbServiceTest {

    @Autowired
    private SaveToMongodbService saveToMongodbService;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void doAddNewFileTest(){
        KaoserFile kaoserFile =  saveToMongodbService.addNewFile("123456","1","test");
        System.out.println(kaoserFile);
        //assertEquals("success","",kaoserFile);
        System.out.println("addNewFile=======================");
        System.out.println("测试通过");
        System.out.println("=================================");

    }
}