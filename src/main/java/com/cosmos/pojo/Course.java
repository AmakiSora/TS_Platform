package com.cosmos.pojo;

import lombok.Data;

@Data
public class Course {
    private String id;//课程id
    private String name;//课程名
    private String teacherID;//任课教师ID
    private String teacher;//任课教师
    private String college;//所属学院
}
