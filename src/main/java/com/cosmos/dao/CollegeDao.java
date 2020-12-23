package com.cosmos.dao;

import com.cosmos.pojo.College;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Repository
public class CollegeDao {
    private static Map<Integer,College> colleges = null;

    static {
        colleges = new HashMap<Integer,College>();//建表
        colleges.put(001,new College(001,"马克思主义学院"));
        colleges.put(002,new College(002,"电子信息学院"));
        colleges.put(003,new College(003,"外国语学院"));
        colleges.put(004,new College(004,"电气学院"));
        colleges.put(005,new College(005,"商学院"));

    }
    public Collection<College> getColleges(){//获取学院信息
        return colleges.values();
    }
    public College getCollege(Integer id){//通过id获得学院
        return colleges.get(id);
    }

}
