package com.test.controller;

import com.test.entity.KaoserFile;
import com.test.entity.Users;
import com.test.repository.KaoserFileRepository;
import com.test.service.FindByMynameService;
import net.sf.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ImportFromMongoTest {

    @Autowired
    FindByMynameService findBymyNameService;

    @Autowired
    private KaoserFileRepository kaoserFileRepository;
    @Before
    public void initData(){
        if(kaoserFileRepository.findAll().size()>0){
            kaoserFileRepository.deleteAll();
        }
        String jsonGet = "{\"mxGraphModel \":{\"root \": {\"mxCell \":[{ \"-id\": \"0\" },{\"-id\": \"1\",\"-parent\": \"0\"},{\"-id\": \"2\",\"-value\": \"d\",\"-style\":\"shape=goal;whiteSpace=wrap;html=1;top=0;bottom=0;fillColor=#DAE8FC;strokeColor=#0066CC;fontSize=18\",\"-vertex\": \"1\",\"-parent\": \"1\",\"-flag\": \"goal\",\"mxGeometry\": {\"-x\": \"130\",\"-y\": \"110\",\"-width\": \"180\",\"-height\": \"40\",\"-as\": \"geometry\"}}]}}}";
        kaoserFileRepository.save(new KaoserFile("1",jsonGet,"2","1"));
        kaoserFileRepository.save(new KaoserFile("2",jsonGet,"2","2"));
        kaoserFileRepository.save(new KaoserFile("3",jsonGet,"6","1"));

    }

    /**
     * 测试查找成功
     */
    @Test
    public void findTestSuccess() {
        JSONArray jsonArray = findBymyNameService.find("2");
        assertEquals("success",2,jsonArray.size());
        System.out.println("测试查找成功------------------------------ ");
        System.out.println("测试查找成功");
        System.out.println("----------------------------------------");
    }

    /**
     * 测试查找失败，用户名不存在
     */
    @Test
    public void findTestFail() {
        JSONArray jsonArray = findBymyNameService.find("10");
        assertEquals("success",0,jsonArray.size());
        System.out.println("测试查找失败,用户名不存在------------------------------ ");
        System.out.println("测试查找失败");
        System.out.println("----------------------------------------------------");
    }
}