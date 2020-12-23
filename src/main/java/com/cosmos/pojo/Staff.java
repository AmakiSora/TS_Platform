package com.cosmos.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {//教师表
    private Integer id;//教工号
    private Integer gender;//性别 0女，1男
    private String name;//教师名
    private String phone;//手机号
    private College college;//学院




}
