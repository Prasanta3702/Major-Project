package com.pkyr.brainace.model;

public class UploadAssignmentModel {
    private String studentName;
    private String studentCode;
    private String assignmentSubject;
    private String assignmentName;
    private String assignmentDate;
    private String assignmentAnswer;

    public UploadAssignmentModel() {
    }

    public UploadAssignmentModel(String studentName, String studentCode, String assignmentSubject, String assignmentName, String assignmentDate, String assignmentAnswer) {
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.assignmentSubject = assignmentSubject;
        this.assignmentName = assignmentName;
        this.assignmentDate = assignmentDate;
        this.assignmentAnswer = assignmentAnswer;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    public String getAssignmentSubject() {
        return assignmentSubject;
    }

    public void setAssignmentSubject(String assignmentSubject) {
        this.assignmentSubject = assignmentSubject;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getAssignmentDate() {
        return assignmentDate;
    }

    public void setAssignmentDate(String assignmentDate) {
        this.assignmentDate = assignmentDate;
    }

    public String getAssignmentAnswer() {
        return assignmentAnswer;
    }

    public void setAssignmentAnswer(String assignmentAnswer) {
        this.assignmentAnswer = assignmentAnswer;
    }
}
