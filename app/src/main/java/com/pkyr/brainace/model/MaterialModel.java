package com.pkyr.brainace.model;

public class MaterialModel {
    private String id;
    private String subjectName;
    private String desc;
    private String document;
    private String date;

    public MaterialModel() {}

    public MaterialModel(String id, String subjectName, String desc, String document, String date) {
        this.subjectName = subjectName;
        this.desc = desc;
        this.date = date;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getDocument() {
        return document;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
