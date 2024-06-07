package com.pkyr.brainace.model;

public class AssignmentModel {
    private String assignment_name;
    private String assignment_subject;
    private String assignment_teacher;
    private String assignment_date;
    private String assignment_last_date;
    private String assignment_question;

    public AssignmentModel() {
    }

    public AssignmentModel(String assignment_name, String assignment_subject, String assignment_teacher, String assignment_date, String assignment_last_date, String assignment_question) {
        this.assignment_name = assignment_name;
        this.assignment_subject = assignment_subject;
        this.assignment_teacher = assignment_teacher;
        this.assignment_date = assignment_date;
        this.assignment_last_date = assignment_last_date;
        this.assignment_question = assignment_question;
    }

    public String getAssignment_name() {
        return assignment_name;
    }

    public void setAssignment_name(String assignment_name) {
        this.assignment_name = assignment_name;
    }

    public String getAssignment_subject() {
        return assignment_subject;
    }

    public void setAssignment_subject(String assignment_subject) {
        this.assignment_subject = assignment_subject;
    }

    public String getAssignment_teacher() {
        return assignment_teacher;
    }

    public void setAssignment_teacher(String assignment_teacher) {
        this.assignment_teacher = assignment_teacher;
    }

    public String getAssignment_date() {
        return assignment_date;
    }

    public void setAssignment_date(String assignment_date) {
        this.assignment_date = assignment_date;
    }

    public String getAssignment_last_date() {
        return assignment_last_date;
    }

    public void setAssignment_last_date(String assignment_last_date) {
        this.assignment_last_date = assignment_last_date;
    }

    public String getAssignment_question() {
        return assignment_question;
    }

    public void setAssignment_question(String assignment_question) {
        this.assignment_question = assignment_question;
    }
}
