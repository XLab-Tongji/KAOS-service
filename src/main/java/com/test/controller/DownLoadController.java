package com.test.controller;

import com.test.service.DownloadService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class DownLoadController {
    @Autowired
    freemarker.template.Configuration configuration;

    @Autowired
    MongoTemplate mongoTemplateModel;

    @Autowired
    DownloadService downloadService;


    @RequestMapping(value = "/template/{templateID}" ,method = RequestMethod.POST)
    public void getResults(@RequestParam(value = "jsname")String jsonName,
                           @RequestParam(value = "jsonStr")String jsonGet,
                           HttpServletResponse response,@PathVariable String templateID)
            throws ServletException, IOException, TemplateException {
        System.out.println("Find department with ID: " + templateID);
//        String jsonName=request.getParameter("jsname");
//        String jsonGet=request.getParameter("jsonStr");
        String usefulness = jsonGet.substring(1,jsonGet.length()-1);

        System.out.println(usefulness);

        String results = downloadService.getResult(jsonName,usefulness,templateID);

        response.setContentType("text/html;charset=utf-8");

        System.out.println(results);

        response.getWriter().print(results);
        response.getWriter().flush();
        response.getWriter().close();
    }
}
