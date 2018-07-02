package com.test.controller;

import com.test.entity.User;
import com.test.repository.UserRepository;
import com.test.service.RegisterService;
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
public class RegisterTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RegisterService registerService;
    @Before
    public void doInit(){
        userRepository.deleteAll();
        userRepository.save(new User("123456","123456"));
        userRepository.save(new User("234567","123456"));
    }

    /**
     * 测试注册失败，用户名已经存在
     */
    @Test
    public void doRegisterTestUserExist(){
        String username = "123456";
        String password = "123456";
        String msg = registerService.doRegister(username,password);
        assertEquals("success","3",msg);
        System.out.println("测试注册失败--------------------");
        System.out.println("用户已存在");
        System.out.println("------------------------------ ");
    }

    /**
     * 测试注册成功
     */
    @Test
    public void doRegisterTestSuccessRegister(){
        String username = "456789";
        String password = "123456";
        String msg = registerService.doRegister(username,password);
        assertEquals("success","0",msg);
        System.out.println("测试注册成功--------------------- ");
        System.out.println("注册成功");
        System.out.println("------------------------------ ");
    }

    /**
     * 测试注册失败，长度不满足要求
     */
    @Test
    public void doRegisterTestLengthFail(){
        String username = "45678";
        String password = "123456";
        String msg = registerService.doRegister(username,password);
        assertEquals("success","2",msg);
        System.out.println("测试注册失败，长度应该在6～16之间--------------------- ");
        System.out.println("测试注册失败，长度应该在6～16之间");
        System.out.println("------------------------------------------------- ");
    }
    @Test
    public void doRegisterTestLengthFail2(){
        String username = "456782930ufanfafagg";
        String password = "123456";
        String msg = registerService.doRegister(username,password);
        assertEquals("success","2",msg);
        System.out.println("测试注册失败，长度应该在6～16之间--------------------- ");
        System.out.println("测试注册失败，长度应该在6～16之间");
        System.out.println("------------------------------------------------- ");
    }
    @Test
    public void doRegisterTestLengthFail3(){
        String username = "456789";
        String password = "123456fbafh894ngff";
        String msg = registerService.doRegister(username,password);
        assertEquals("success","2",msg);
        System.out.println("测试注册失败，长度应该在6～16之间--------------------- ");
        System.out.println("测试注册失败，长度应该在6～16之间");
        System.out.println("------------------------------------------------- ");
    }

    /**
     * 空格
     */
    @Test
    public void doRegisterTestBlankExist(){
        String username = "456 789";
        String password = "123456fbaf";
        String msg = registerService.doRegister(username,password);
        assertEquals("success","1",msg);
        System.out.println("测试注册失败，存在空格------------------------------- ");
        System.out.println("测试注册失败，存在空格");
        System.out.println("------------------------------------------------- ");
    }
    @Test
    public void doRegisterTestBlankExist2(){
        String username = "456789";
        String password = "1234 56fbaf";
        String msg = registerService.doRegister(username,password);
        assertEquals("success","1",msg);
        System.out.println("测试注册失败，存在空格------------------------------- ");
        System.out.println("测试注册失败，存在空格");
        System.out.println("------------------------------------------------- ");
    }

    @After
    public void doAfter(){
        userRepository.deleteAll();
        userRepository.save(new User("123456","123456"));

    }
}