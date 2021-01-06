package com.cosmos.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {//学生表
    private Integer id;
    private String name;
    private Integer gender;
    private String classes;
    private String college;
    private String phone;

}
