package com.capstone.deptmanager.model;

/**
 * Created by daehee on 2017. 11. 11..
 */

public class NotiBean {

    private int no;
    private String title;
    private String msg;
    private String date;

    public NotiBean() {} // An empty constructor

    public NotiBean(int no, String title, String msg, String date) {
        this.no = no;
        this.title = title;
        this.msg = msg;
        this.date = date;
    } // end of Constructor

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

} // end of class
