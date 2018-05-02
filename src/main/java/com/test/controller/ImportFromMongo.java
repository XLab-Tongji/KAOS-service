package com.test.controller;

import com.test.service.FindByMynameService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
public class ImportFromMongo {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    FindByMynameService findBymyNameService;
    @RequestMapping(value = "importFromMongo",method = RequestMethod.GET)
//    public void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException{
//
//        List<KaoserFile> kaoserFiles = find("wd");
//        System.out.println(kaoserFiles);
//
//    }
    public void find(HttpServletRequest request, HttpServletResponse response)
                throws ServletException, IOException{
        String myName = request.getParameter("myname");

        JSONArray jsonArray = findBymyNameService.find(myName);

        response.setContentType("application/json;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache");

        PrintWriter pw = response.getWriter();
        pw.print(jsonArray);
        System.out.println(jsonArray);
    }
    @RequestMapping("he")
    public String hello(){
        return "hello world!";
    }

}
