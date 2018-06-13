package com.test.controller;

import com.test.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class Register {

    @Autowired
    private RegisterService registerService;
    @ResponseBody
    @RequestMapping(value = "/register" ,method = RequestMethod.POST)
    public String doRegister(@RequestParam(value = "username")String username,
                           @RequestParam(value = "password")String password) {
        return registerService.doRegister(username,password);
    }
}
