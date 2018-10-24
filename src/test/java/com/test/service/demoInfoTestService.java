package com.test.service;

import com.test.entity.DemoInfo;
import com.test.repository.DemoInfoRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class demoInfoTestService {
    @Autowired
    private DemoInfoRepository demo;

    @Test
    public void test() throws  Exception{
        demo.save(new DemoInfo("1","aaa",1));
        demo.save(new DemoInfo("2","bbb",2));
            System.out.println(demo.findByName("aaa"));
    }
}
