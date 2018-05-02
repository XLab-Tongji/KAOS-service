package com.test.service;

import freemarker.template.TemplateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;

import static org.junit.Assert.*;

public class DownloadServiceTest {
    @Autowired
    DownloadService downloadService;

    @Before
    public void doInit(){

    }

    @Test
    public void getResult() throws IOException,TemplateException {
        String jsonName = "test";
        String jsonGet = "{\"mxGraphModel\":{\"root\": {\"mxCell\":[{ \"-id\": \"0\" },{\"-id\": \"1\",\"-parent\": \"0\"},{\"-id\": \"2\",\"-value\": \"d\",\"-style\":\"shape=goal;whiteSpace=wrap;html=1;top=0;bottom=0;fillColor=#DAE8FC;strokeColor=#0066CC;fontSize=18\",\"-vertex\": \"1\",\"-parent\": \"1\",\"-flag\": \"goal\",\"mxGeometry\": {\"-x\": \"130\",\"-y\": \"110\",\"-width\": \"180\",\"-height\": \"40\",\"-as\": \"geometry\"}}]}}}";
        String templateID = "md";
        //String result = downloadService.getResult(jsonName,jsonGet,templateID);
        System.out.println("------------------------------ ");
        //System.out.println(result);
        System.out.println("------------------------------ ");
    }
}