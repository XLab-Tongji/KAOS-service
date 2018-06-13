package com.test.controller;

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
public class LoginTest {
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
     * 测试登录失败,用户名不存在
     */
    @Test
    public void doLoginTest(){
        String username = "4";
        String password = "123456";
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("username").is(username),Criteria.where("password").is(password));
        assertEquals("success",0,mongoTemplate.count(new Query(criteria),Users.class));
        System.out.println("测试登录失败,用户名不存在--------------------");
        System.out.println("测试登录失败,用户名不存在");
        System.out.println("---------------------------------------- ");
    }

    /**
     * 测试登录失败,密码错误
     */
    @Test
    public void doLoginTest1(){
        String username = "3";
        String password = "1234567";
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("username").is(username),Criteria.where("password").is(password));
        assertEquals("success",0,mongoTemplate.count(new Query(criteria),Users.class));
        System.out.println("测试登录失败,密码错误-----------------------");
        System.out.println("测试登录失败,密码错误");
        System.out.println("----------------------------------------");
    }

    /**
     * 测试登录成功
     */
    @Test
    public void doRegisterTest3(){
        String username = "3";
        String password = "123456";
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("username").is(username),Criteria.where("password").is(password));
        assertEquals("success",1,mongoTemplate.count(new Query(criteria),Users.class));
        System.out.println("测试登录成功------------------------------ ");
        System.out.println("测试登录成功");
        System.out.println("----------------------------------------");
    }

    @After
    public void doAfter(){
        usersRepository.deleteAll();
    }
}