package com.test.controller;

import com.test.entity.KaoserFile;
import com.test.repository.KaoserFileRepository;
import com.test.service.SaveToMongodbService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SaveToMongodbControllerTest {

    @Autowired
    private KaoserFileRepository kaoserFileRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private SaveToMongodbService saveToMongodbService;
    @Before
    public void doInit(){
        kaoserFileRepository.deleteAll();
        kaoserFileRepository.save(new KaoserFile("1","2","2","1"));
        kaoserFileRepository.save(new KaoserFile("1","2","3","2"));
        kaoserFileRepository.save(new KaoserFile("3","3","6","1"));
        kaoserFileRepository.save(new KaoserFile("2","4","3","1"));
    }

    /**
     * 测试保存失败，相同作者相同项目相同文件名
     */
    @Test
    public void doSaveTestFailBySameProjectAndSameUserAndSameFileName() throws IOException {
        String name = "1";
        String myname = "2";
        String projectname = "1";
        String jsonstr = "4";
        String msg = saveToMongodbService.doSaveToMongodb(projectname,jsonstr,myname,name);
        assertEquals("success","{\"name\":\"fail\"}",msg);
        System.out.println("测试保存失败，相同作者相同项目相同文件名--------------------- ");
        System.out.println("测试保存失败，相同作者相同项目相同文件名");
        System.out.println("------------------------------------------------------ ");
    }

    /**
     * 测试保存成功，相同作者不同项目相同文件名
     */
    @Test
    public void doSaveTestSuccessBySameProjectAndSameUserAndDifferentFileName() throws IOException {
        String name = "2";
        String myname = "2";
        String projectname = "1";
        String jsonstr = "4";
        String msg = saveToMongodbService.doSaveToMongodb(projectname,jsonstr,myname,name);
        assertEquals("success","{\"name\":\"success\"}",msg);
        System.out.println("测试保存成功，相同作者相同项目不同文件名--------------------- ");
        System.out.println("测试保存成功，相同作者相同项目不同文件名");
        System.out.println("------------------------------------------------------ ");
    }

    /**
     * 测试保存成功，相同作者不同项目相同文件名
     */
    @Test
    public void doSaveTestSuccessByDifferentProjectAndSameUser() throws IOException {
        String name = "2";
        String myname = "2";
        String projectname = "2";
        String jsonstr = "4";
        String msg = saveToMongodbService.doSaveToMongodb(projectname,jsonstr,myname,name);
        assertEquals("success","{\"name\":\"success\"}",msg);
        System.out.println("测试保存成功，相同作者不同项目相同文件名--------------------- ");
        System.out.println("测试保存成功，相同作者不同项目相同文件名");
        System.out.println("------------------------------------------------------ ");
    }

    /**
     * 测试保存成功，不同作者
     */
    @Test
    public void doSaveTestSuccessByDifferentUser() throws IOException {
        String name = "2";
        String myname = "3";
        String projectname = "2";
        String jsonstr = "4";
        String msg = saveToMongodbService.doSaveToMongodb(projectname,jsonstr,myname,name);
        assertEquals("success","{\"name\":\"success\"}",msg);
        System.out.println("测试保存成功，不同作者------------------------------------ ");
        System.out.println("测试保存成功，不同作者");
        System.out.println("------------------------------------------------------ ");
    }

    @After
    public void doAfter(){
        kaoserFileRepository.deleteAll();
    }
}