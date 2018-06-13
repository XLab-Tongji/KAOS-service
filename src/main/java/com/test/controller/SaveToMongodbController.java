package com.test.controller;

import com.test.entity.KaoserFile;
import com.test.repository.KaoserFileRepository;
import com.test.service.SaveToMongodbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.*;

@Controller
public class SaveToMongodbController {
    @Autowired
    private KaoserFileRepository kaoserFileRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private SaveToMongodbService saveToMongodbService;

    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam(value = "jsname")String jsonName,
                                     @RequestParam(value = "jsonStr")String jsonGet,
                                     @RequestParam(value = "myname")String myName,
                                     @RequestParam(value = "mytitle")String title)
            throws IOException{

        return saveToMongodbService.doSaveToMongodb(jsonName,jsonGet,myName,title);
    }

    public KaoserFile findByName(String name){
        return kaoserFileRepository.findByName(name);
    }
}
