package com.pkyr.brainace;

public class Subject {
    private String subjectName;
    private String subjectCode;
    private String subjectTeacher;

    public Subject() {
        super();
    }

    public Subject(String subjectName, String subjectCode, String subjectTeacher) {
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.subjectTeacher = subjectTeacher;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }
}
