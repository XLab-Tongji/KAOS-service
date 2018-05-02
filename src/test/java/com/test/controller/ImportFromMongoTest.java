package com.test.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.Assert.*;

public class ImportFromMongoTest {
    @Autowired
    MongoTemplate mongoTemplate;
    @Before
    public void initData(){

    }
    @Test
    public void find() {

    }
}