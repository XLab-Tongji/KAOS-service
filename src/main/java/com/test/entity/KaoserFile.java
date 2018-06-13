package com.test.entity;

import org.springframework.data.annotation.Id;

public class KaoserFile {
    @Id
    private String id;

    /**
     * 文件名
     */
    private String name;
    private String jsonStr;
    /**
     * 作者名
     */
    private String myname;
    /**
     * 项目名
     */
    private String projectname;

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getMyname() {
        return myname;
    }

    public KaoserFile() {
    }

    public KaoserFile(String name, String jsonStr, String myname,String projectname) {
        this.name = name;
        this.jsonStr = jsonStr;
        this.myname = myname;
        this.projectname = projectname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }

    @Override
    public String toString() {
        return "KaoserFile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", jsonStr='" + jsonStr + '\'' +
                ", myname='" + myname + '\'' +
                '}';
    }
}
