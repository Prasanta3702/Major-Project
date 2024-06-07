package com.pkyr.brainace.model;

public class UserModel {
    private String id;
    private String name;
    private String email;
    private String pass;
    private String code;
    private String course;
    private String batch;
    private String sem;
    private String sec;

    public UserModel() {
    }

    public UserModel(String id, String name, String email, String pass, String code, String course, String batch, String sem, String sec) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.code = code;
        this.course = course;
        this.batch = batch;
        this.sem = sem;
        this.sec = sec;
    }

    public String getId() {
        return id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getSec() {
        return sec;
    }

    public void setSec(String sec) {
        this.sec = sec;
    }
}
