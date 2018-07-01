package com.test.controller;

import com.test.service.TemplateFileService;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Controller
public class TemplateFileController {
    @Autowired
    freemarker.template.Configuration configuration;

    @Autowired
    MongoTemplate mongoTemplateModel;

    @Autowired
    TemplateFileService templateFileService;

    private String result = "";

    /**
     * 生成文档
     * @param jsonName
     * @param jsonGet
     * @param templateID
     * @return
     * @throws ServletException
     * @throws IOException
     * @throws TemplateException
     */
    @ResponseBody
    @RequestMapping(value = "/template/{templateID}" ,method = RequestMethod.POST)
    public String getResults(@RequestParam(value = "jsname")String jsonName,
                           @RequestParam(value = "jsonStr")String jsonGet,
                           @PathVariable String templateID)
            throws ServletException, IOException, TemplateException {
        System.out.println(jsonGet);
        System.out.println("Find department with ID: " + templateID);
//        String jsonName=request.getParameter("jsname");
//        String jsonGet=request.getParameter("jsonStr");
        String usefulness = jsonGet.substring(1,jsonGet.length()-1);

        System.out.println(usefulness);

        this.result = templateFileService.getResult(jsonName,usefulness,templateID);

        System.out.println(result);

        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/ServletDownload",method = RequestMethod.POST)
    public void doDownload(@RequestParam(value = "fileName")String fileName, HttpServletResponse response) throws ServletException, IOException {


        response.setHeader("Content-Disposition","attachment;filename=" + fileName + ".md");

        response.setContentType("application/octet-stream");

        response.setContentLength((int) result.length());

        PrintWriter pw = response.getWriter();
        pw.print(result);
        System.out.println(result);

    }

}
