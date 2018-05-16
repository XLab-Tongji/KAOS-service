package com.test;

import net.sf.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Student{
    private String name;
    private int age;
    private Date birthday;

    public Student() {
        birthday=new Date();
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", birthday=" + birthday +
                '}';
    }
    public void transform(String string) throws ParseException {
        JSONObject jsonObject= JSONObject.fromObject(string);
        this.name = jsonObject.getString("name");
        this.age = Integer.parseInt(jsonObject.getString("age"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.birthday = simpleDateFormat.parse(jsonObject.getString("birthday"));
        System.out.println(this.toString());
    }
    public static void main(String[]args) throws ParseException {
        String str = "{'name':'tiger','age':21,'birthday':'2018-04-08'}";
        Student student = new Student();
        student.transform(str);
    }
}
