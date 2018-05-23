package com.test.controller;

import com.test.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
public class Register {
    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = "/register" ,method = RequestMethod.POST)
    public void doRegister(@RequestParam(value = "username")String username,
                           @RequestParam(value = "password")String password,
                           HttpServletResponse response)
            throws IOException,ServletException {
        System.out.println(username);
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        response.setContentType("text/html;charset=utf-8");

        PrintWriter pw = response.getWriter();
        if(mongoTemplate.count(query,Users.class) == 0){
            Users user = new Users();
            user.setUsername(username);
            user.setPassword(password);
            mongoTemplate.save(user);
            pw.print("ok");

        }
    }
}
