package com.test.controller;

import com.test.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class Login {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private LoginService loginService;

    @ResponseBody
    @RequestMapping(value = "/login" ,method = RequestMethod.POST)
    public String doLogin(@RequestParam(value = "username")String username,
                                      @RequestParam(value = "password")String password) {
        return loginService.doLogin(username,password);
    }
}