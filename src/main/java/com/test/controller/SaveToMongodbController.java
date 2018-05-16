package com.test.controller;

import com.test.entity.KaoserFile;
import com.test.repository.KaoserFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class SaveToMongodbController {
    @Autowired
    private KaoserFileRepository kaoserFileRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    @ResponseBody
    public void save(HttpServletRequest request,
                                     HttpServletResponse response , Model model)
            throws ServletException, IOException{
        String jsonName=request.getParameter("jsname");
        String jsonGet=request.getParameter("jsonStr");
        String myName=request.getParameter("myname");
        String title=request.getParameter("mytitle");

        System.out.println(jsonName);
        System.out.println(jsonGet);
        KaoserFile jsoninfo=new KaoserFile();
        jsoninfo.setName(jsonName);
        jsoninfo.setJsonStr(jsonGet);
        jsoninfo.setMyname(myName);
        jsoninfo.setProjectname(title);
        response.setContentType("application/json;charset=utf-8");

        String resp="";
        System.out.println(ResourceUtils.getURL("classpath").getPath());
        Criteria criatira = new Criteria();
        criatira.andOperator(Criteria.where("myname").is(myName), Criteria.where("projectname").is(title));
        //mongoTemplate.find(new Query(criatira), KaoserFile.class);
        if(mongoTemplate.count(new Query(criatira), KaoserFile.class)>0){
            resp = "{\"name\":\"fail\"}";
        }
        else{
            kaoserFileRepository.save(jsoninfo);
            resp = "{\"name\":\"success\"}";
        }
        try {
                response.getWriter().print(resp);
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        System.out.println(resp);
    }

    public KaoserFile findByName(String name){
        return kaoserFileRepository.findByName(name);
    }
}
