package com.test.controller;

import com.test.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
@Controller
public class Login {
    @Autowired
    MongoTemplate mongoTemplate;
    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public void doLogin(@RequestParam(value = "username")String username,
                        @RequestParam(value = "password")String password, HttpServletResponse response)
            throws IOException,ServletException {
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("username").is(username),Criteria.where("password").is(password));
        response.setContentType("text/html;charset=utf-8");
        PrintWriter pw = response.getWriter();
        System.out.println(mongoTemplate.count(new Query(criteria),Users.class));
        if(mongoTemplate.count(new Query(criteria),Users.class) > 0){
            pw.print(1);
        }
        else {
            pw.print(-1);
        }
    }
}