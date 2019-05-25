package com.test.controller;

import com.test.entity.KaoserFile;
import com.test.entity.KaoserShape;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SaveToMongodbController {
    @Autowired
    private KaoserFileRepository kaoserFileRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    private SaveToMongodbService saveToMongodbService;

    /**
     * 新增或者更新文档
     * @param id
     * @param title
     * @param jsonGet
     * @param myName
     * @param projectName
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/save" ,method = RequestMethod.POST)
    @ResponseBody
    public String save(@RequestParam(value = "id")String id,
            @RequestParam(value = "title")String title,
                       @RequestParam(value = "jsonStr")String jsonGet,
                       @RequestParam(value = "myname")String myName,
                       @RequestParam(value = "projectName")String projectName)
            throws IOException{
        if(id.equals("")){
            return saveToMongodbService.doSaveToMongodb(title,jsonGet,myName,projectName);
        }else{
            return saveToMongodbService.updateToMongo(id,title,jsonGet);
        }
    }

    @RequestMapping(value = "/savemyshape" ,method = RequestMethod.POST)
    @ResponseBody
    public String savemyshape(@RequestParam(value = "id")String id,
                              @RequestParam(value = "name")String name,
                              @RequestParam(value = "style")String style,
                              @RequestParam(value = "width")int width,
                              @RequestParam(value = "height")int height,
                              @RequestParam(value = "attribute")String attribute)
            throws IOException{
        return saveToMongodbService.doSaveShapeToMongodb(id,name,style,width,height,attribute);
    }

    @RequestMapping(value = "/loadmyshape" ,method = RequestMethod.GET)
    @ResponseBody
    public List<KaoserShape> loadmyshape(@RequestParam(value = "id")String id)
            throws IOException{
        return saveToMongodbService.findMyShapeByNameAndId(id);
    }

    public KaoserFile findByName(String name){
        return kaoserFileRepository.findByName(name);
    }

    /**
     * 新建文档
     * @param username
     * @param projectName
     * @param title
     * @return
     */
    @ResponseBody
    @RequestMapping("addNewFile")
    public Map<String,Object> addNewFile(@RequestParam("username")String username,
                                         @RequestParam("projectName")String projectName,
                                         @RequestParam("title")String title){
        Map<String,Object> map = new HashMap<>();
        KaoserFile newFile =  saveToMongodbService.addNewFile(username,projectName,title);
        map.put("kaosfileId",newFile.getId());
        map.put("msg","success");
        return map;
    }
}
