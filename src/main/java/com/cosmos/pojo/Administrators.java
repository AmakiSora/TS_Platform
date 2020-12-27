package com.cosmos.pojo;

public class Administrators {
    private Integer userid;

    private String username;

    public Administrators(Integer userid, String username) {
        this.userid = userid;
        this.username = username;
    }

    public Administrators() {
        super();
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }
}