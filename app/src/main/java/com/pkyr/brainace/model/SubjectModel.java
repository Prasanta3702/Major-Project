package com.pkyr.brainace.model;

public class SubjectModel {
    private String subjectCode;
    private String subjectName;
    private String subjectTeacher;

    public SubjectModel() {
    }

    public SubjectModel(String subjectCode, String subjectName, String subjectTeacher) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectTeacher = subjectTeacher;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }
}
