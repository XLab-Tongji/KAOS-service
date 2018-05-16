package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;

public class JoinMysql {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, ParseException {
        String url = "jdbc:mysql://127.0.0.1:3306/test";
        String username = "root";
        String password = "000000";
        Class.forName("com.mysql.jdbc.Driver");
        Connection connect=DriverManager.getConnection(url, username, password);
        Student student = new Student();
        String str = "{'name':'tiger','age':21,'birthday':'2018-04-08'}";
        student.transform(str);
        //birthday进行了字符串转化，数据库里的birthday字段是varchar类型
        String sql = "insert into student(name,age,birthday) values("+student.getName()+","+student.getAge()+","+student.getBirthday().toString()+")";
        PreparedStatement preparedStatement=connect.prepareStatement(sql);
        preparedStatement.execute();
        preparedStatement.close();
        connect.close();
    }
}
