package com.test.service;

import com.test.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    @Autowired
    MongoTemplate mongoTemplate;
    public String doLogin(String username,String password){
        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("username").is(username),Criteria.where("password").is(password));
        //response.setContentType("text/html;charset=utf-8");
        //PrintWriter pw = response.getWriter();
        System.out.println(mongoTemplate.count(new Query(criteria),User.class));
        if(mongoTemplate.count(new Query(criteria),User.class) > 0){
            return "1";
        }
        else {
            return "-1";
        }
    }
}
