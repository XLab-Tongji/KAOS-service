package com.test.repository;
import static org.assertj.core.api.Assertions.assertThat;
import com.test.entity.KaoserFile;
import com.test.init.DoInitAndDestory;
import com.test.service.SaveToMongodbService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class KaoserFileRepositoryTest {
    @Autowired
    private KaoserFileRepository kaoserFileRepository;
    @Autowired
    private SaveToMongodbService saveToMongodbService;
    @Before
    public void doInit(){
        kaoserFileRepository.deleteAll();
        kaoserFileRepository.save(new KaoserFile("1","2","2","1"));
        kaoserFileRepository.save(new KaoserFile("1","2","3","2"));
        kaoserFileRepository.save(new KaoserFile("3","3","33","1"));
        kaoserFileRepository.save(new KaoserFile("2","4","3","1"));
    }

    /**
     * 测试模糊查询
     */
    @Test
    public void testFindByMynameLike(){
        List<KaoserFile> kaoserFiles = kaoserFileRepository.findByMynameLike("3");
        assertThat(kaoserFiles.size()).isEqualTo(3);
        System.out.println("------------------------------ ");
        for(KaoserFile kaoserFile:kaoserFiles){
            System.out.println(kaoserFile.toString());
        }
        System.out.println("------------------------------ ");

    }

    /**
     * 测试按照文件名查询
     */
    @Test
    public void testFindByName1(){
        KaoserFile kaoserFile = kaoserFileRepository.findByName("1");
        //assertThat(kaoserFile).isEqualTo(1);
        assertEquals("success","1",kaoserFile.getName());
        System.out.println("------------------------------ ");
        System.out.println(kaoserFile.toString());
        System.out.println("------------------------------ ");
    }

    /**
     * 按文件名精确查询
     */
    @Test
    public void testFindByName2(){
        KaoserFile kaoserFile = kaoserFileRepository.findByName("2");
        assertEquals("success","1",kaoserFile.getName());
        System.out.println("------------------------------ ");
        System.out.println(kaoserFile.toString());
        System.out.println("------------------------------ ");
    }

    @After
    public void doAfter(){
        kaoserFileRepository.deleteAll();
        saveToMongodbService.addNewFile("123456","new-project","newFile");

    }
}