package com.test.init;

import com.test.entity.KaoserFile;
import com.test.repository.KaoserFileRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class DoInitAndDestory {

    @Autowired
    private KaoserFileRepository kaoserFileRepository;

    public void doInitKaosFile(){

    }
}
