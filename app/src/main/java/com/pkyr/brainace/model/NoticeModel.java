package com.pkyr.brainace.model;

public class NoticeModel {
    private String notice_id;
    private String notice_sender;
    private String notice_message;
    private String notice_date;
    private String notice_timestamp;

    public NoticeModel() {
    }

    public NoticeModel(String notice_id, String notice_sender, String notice_message, String notice_date, String notice_timestamp) {
        this.notice_sender = notice_sender;
        this.notice_message = notice_message;
        this.notice_date = notice_date;
        this.notice_timestamp = notice_timestamp;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getNotice_sender() {
        return notice_sender;
    }

    public void setNotice_sender(String notice_sender) {
        this.notice_sender = notice_sender;
    }

    public String getNotice_message() {
        return notice_message;
    }

    public void setNotice_message(String notice_message) {
        this.notice_message = notice_message;
    }

    public String getNotice_date() {
        return notice_date;
    }

    public void setNotice_date(String notice_date) {
        this.notice_date = notice_date;
    }

    public String getNotice_timestamp() {
        return notice_timestamp;
    }

    public void setNotice_timestamp(String notice_timestamp) {
        this.notice_timestamp = notice_timestamp;
    }
}
