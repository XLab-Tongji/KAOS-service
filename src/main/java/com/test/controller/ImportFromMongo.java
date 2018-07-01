package com.test.controller;

import com.test.entity.KaoserFile;
import com.test.service.FindByMynameService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@RestController
public class ImportFromMongo {
    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    FindByMynameService findBymyNameService;

    /**
     * 从数据看导入
     * @param myName
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @RequestMapping(value = "importFromMongo",method = RequestMethod.GET)
    public void find(@RequestParam(value = "myname")String myName, HttpServletResponse response)
                throws ServletException, IOException{

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

    /**
     * 通过id查找文档
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("findFileById")
    public Map<String,Object> findKaosFile(@RequestParam(value = "id")String id){
        return findBymyNameService.findKaosFileById(id);
    }
}