package com.test.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "KaoserShape")
public class KaoserShape {

    public String getMyid() {
        return myid;
    }

    public void setMyid(String myid) {
        this.myid = myid;
    }

    @Id
    private String myid;
    private String id;
    private String name;
    private String style;
    private int width;
    private int height;
    private String attribute;
    private String projectName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String title) {
        this.attribute = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public  String getProjectName(){return projectName;}

    public void setProjectName(String projectName){this.projectName = projectName;}
}
