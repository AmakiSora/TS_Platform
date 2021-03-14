package com.cosmos.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Course {
    private String id;//课程id
    private String name;//课程名
    private String teacherID;//任课教师ID
    private String teacher;//任课教师
    private String college;//所属学院
    private Date startDate;//开课日期
    private Date endDate;//结课日期
    private Integer studentNum;//课程学生人数
    private String introduce;//课程介绍
}
