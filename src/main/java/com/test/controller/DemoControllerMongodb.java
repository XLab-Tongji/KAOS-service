package com.test.controller;

import com.test.entity.DemoInfo;
import com.test.repository.DemoInfoRepository;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class DemoControllerMongodb {
    @Autowired
    private DemoInfoRepository demoInfoRepository;

    @GetMapping(value = "hello")
    public String hello(){
        System.out.println("ios");
        return "hello world!";
    }

//    @RequestMapping("save")
//    public String save(){
//        DemoInfo demoInfo = new DemoInfo();
//        demoInfo.setName("张三");
//        demoInfo.setAge(20);
//        demoInfoRepository.save(demoInfo);
//
//        demoInfo = new DemoInfo();
//        demoInfo.setName("李四");
//        demoInfo.setAge(30);
//        demoInfoRepository.save(demoInfo);
//
//        return "Insert Success.";
//    }

    @RequestMapping("delete")
    public String delete(){
        demoInfoRepository.delete(demoInfoRepository.findByName("李四").getId());
        return "Delete Success.";
    }

    @RequestMapping("find")
    public List<DemoInfo> find(){
        return demoInfoRepository.findAll();
    }

    @RequestMapping("findByName")
    public DemoInfo findByName(String name){
        return demoInfoRepository.findByName(name);
    }

    @RequestMapping("hoo")
    public JSONObject helo(){
        System.out.println("hello");
        String string = "{'hello':'hello'}";
        return JSONObject.fromObject(string);
    }
}
