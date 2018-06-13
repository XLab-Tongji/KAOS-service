package com.test.service;

import com.test.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    @Autowired
    MongoTemplate mongoTemplate;

    public String doRegister(String username,String password){
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));

        if(mongoTemplate.count(query,User.class) == 0){
            if(username.contains(" ")||password.contains(" ")){
                return "1";   //字符串中含有空格
            }else{
                if(username.length()<=5||username.length()>=17||password.length()<=5||password.length()>=17){
                    return "2";    //密码长度应该在6～16之间
                }
                else{
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    mongoTemplate.save(user);
                    return "0"; //合法
                }
            }
        }else{
            return "3"; //用户已存在
        }
    }
}
