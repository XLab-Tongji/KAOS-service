package com.test.controller;

import com.test.entity.KaoserFile;
import com.test.entity.Users;
import com.test.repository.UsersRepository;
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
public class RegisterTest {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private MongoTemplate mongoTemplate;
    @Before
    public void doInit(){
        usersRepository.deleteAll();
        usersRepository.save(new Users("1","123456"));
        usersRepository.save(new Users("2","123456"));
        usersRepository.save(new Users("3","123456"));

    }

    /**
     * 测试注册失败，用户名已经存在
     */
    @Test
    public void doRegisterTest(){
        String username = "1";
        String password = "123456";
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        assertEquals("success",1,mongoTemplate.count(query,Users.class));
        System.out.println("测试注册失败--------------------");
        System.out.println("用户已存在");
        System.out.println("------------------------------ ");
    }

    /**
     * 测试注册成功
     */
    @Test
    public void doRegisterTest1(){
        String username = "4";
        String password = "123456";
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        assertEquals("success",0,mongoTemplate.count(query,Users.class));
        System.out.println("测试注册成功--------------------- ");
        System.out.println("注册成功");
        System.out.println("------------------------------ ");
    }

    @After
    public void doAfter(){
        usersRepository.deleteAll();
    }
}