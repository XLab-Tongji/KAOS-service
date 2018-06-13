package com.test.controller;

import com.test.entity.KaoserFile;
import com.test.entity.Users;
import com.test.init.DoInitAndDestory;
import com.test.repository.KaoserFileRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SaveToMongodbControllerTest {

    @Autowired
    private KaoserFileRepository kaoserFileRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    //private DoInitAndDestory doInitAndDestory = null;
    @Before
    public void doInit(){
        kaoserFileRepository.deleteAll();
        kaoserFileRepository.save(new KaoserFile("1","2","2","1"));
        kaoserFileRepository.save(new KaoserFile("1","2","3","2"));
        kaoserFileRepository.save(new KaoserFile("3","3","6","1"));
        kaoserFileRepository.save(new KaoserFile("2","4","3","1"));
    }

    /**
     * 测试保存成功，相同作者不同项目相同文件名
     */
    @Test
    public void doSaveTestFailBySameProjectAndSameUserAndSameFileName(){
        String name = "1";
        String myname = "2";
        String projectname = "1";
        String jsonstr = "4";
        Criteria criatira = new Criteria();
        criatira.andOperator(Criteria.where("myname").is(myname), Criteria.where("projectname").is(projectname),Criteria.where("name").is(name));
        assertEquals("success",1,mongoTemplate.count(new Query(criatira), KaoserFile.class));
        kaoserFileRepository.save(new KaoserFile(name,jsonstr,myname,projectname));
        System.out.println("测试保存失败，相同作者相同项目相同文件名--------------------- ");
        System.out.println("测试保存失败，相同作者相同项目相同文件名");
        System.out.println("------------------------------------------------------ ");
    }

    /**
     * 测试保存成功，相同作者不同项目相同文件名
     */
    @Test
    public void doSaveTestSuccessBySameProjectAndSameUserAndDifferentFileName(){
        String name = "2";
        String myname = "2";
        String projectname = "1";
        String jsonstr = "4";
        Criteria criatira = new Criteria();
        criatira.andOperator(Criteria.where("myname").is(myname), Criteria.where("projectname").is(projectname),Criteria.where("name").is(name));
        assertEquals("success",0,mongoTemplate.count(new Query(criatira), KaoserFile.class));
        kaoserFileRepository.save(new KaoserFile(name,jsonstr,myname,projectname));
        System.out.println("测试保存成功，相同作者相同项目不同文件名--------------------- ");
        System.out.println("测试保存成功，相同作者相同项目不同文件名");
        System.out.println("------------------------------------------------------ ");
    }

    /**
     * 测试保存成功，相同作者不同项目相同文件名
     */
    @Test
    public void doSaveTestSuccessByDifferentProjectAndSameUser(){
        String name = "2";
        String myname = "2";
        String projectname = "2";
        String jsonstr = "4";
        Criteria criatira = new Criteria();
        criatira.andOperator(Criteria.where("myname").is(myname), Criteria.where("projectname").is(projectname),Criteria.where("name").is(name));
        assertEquals("success",0,mongoTemplate.count(new Query(criatira), KaoserFile.class));
        kaoserFileRepository.save(new KaoserFile(name,jsonstr,myname,projectname));
        System.out.println("测试保存成功，相同作者不同项目相同文件名--------------------- ");
        System.out.println("测试保存成功，相同作者不同项目相同文件名");
        System.out.println("------------------------------------------------------ ");
    }

    /**
     * 测试保存成功，不同作者
     */
    @Test
    public void doSaveTestSuccessByDifferentUser(){
        String name = "2";
        String myname = "3";
        String projectname = "2";
        String jsonstr = "4";
        Criteria criatira = new Criteria();
        criatira.andOperator(Criteria.where("myname").is(myname), Criteria.where("projectname").is(projectname),Criteria.where("name").is(name));
        assertEquals("success",0,mongoTemplate.count(new Query(criatira), KaoserFile.class));
        kaoserFileRepository.save(new KaoserFile(name,jsonstr,myname,projectname));
        System.out.println("测试保存成功，不同作者------------------------------------ ");
        System.out.println("测试保存成功，不同作者");
        System.out.println("------------------------------------------------------ ");
    }

    @After
    public void doAfter(){
        kaoserFileRepository.deleteAll();
    }
}