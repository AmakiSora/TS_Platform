package com.cosmos.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {//教师表
    private String id;//教工号
    private String name;//教师名
    private Integer gender;//性别 0女，1男
    private String college;//学院
    private String phone;//手机号
}
