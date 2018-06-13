package com.test.controller;

import com.test.entity.User;
import com.test.repository.UserRepository;
import com.test.service.LoginService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LoginTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private LoginService loginService;
    @Before
    public void doInit(){
        userRepository.deleteAll();
        userRepository.save(new User("1","123456"));
        userRepository.save(new User("2","123456"));
        userRepository.save(new User("3","123456"));

    }

    /**
     * 测试登录失败,用户名不存在
     */
    @Test
    public void doLoginTest(){
        String username = "4";
        String password = "123456";
        String loginMsg = loginService.doLogin(username,password);
        assertEquals("success","-1",loginMsg);
        System.out.println("测试登录失败,用户名不存在-----------------------");
        System.out.println("测试登录失败,用户名不存在");
        System.out.println("--------------------------------------------");
    }

    /**
     * 测试登录失败,密码错误
     */
    @Test
    public void doLoginTest1(){
        String username = "3";
        String password = "1234567";
        String loginMsg = loginService.doLogin(username,password);
        assertEquals("success","-1",loginMsg);
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
        String loginMsg = loginService.doLogin(username,password);
        assertEquals("success","1",loginMsg);
        System.out.println("测试登录成功------------------------------ ");
        System.out.println("测试登录成功");
        System.out.println("----------------------------------------");
    }

    @After
    public void doAfter(){
        userRepository.deleteAll();
    }
}