package com.cosmos.dao;

import com.cosmos.pojo.College;
import com.cosmos.pojo.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
@Repository
public class StaffDao {
    //模拟数据库
    private static Map<Integer,Staff> staffs = null;

    @Autowired
    private CollegeDao collegeDao;

    static {
        staffs = new HashMap<Integer, Staff>();//创建一个职工表
        staffs.put(1001,new Staff(1001,0,"AA","18077889900",new College(001,"马克思主义学院")));
        staffs.put(1002,new Staff(1002,1,"BB","18077889911",new College(002,"电子信息学院")));
        staffs.put(1003,new Staff(1003,0,"CC","18077889922",new College(003,"外国语学院")));
        staffs.put(1004,new Staff(1004,1,"DD","18077889933",new College(004,"电气学院")));
        staffs.put(1005,new Staff(1005,0,"EE","18077889944",new College(005,"商学院")));

    }
    //增加职工
    private static Integer initId = 1006;//主键自增
    public  void save(Staff staff){
        if (staff.getId()==null){
            staff.setId(initId++);
        }
        staff.setCollege(collegeDao.getCollege(staff.getCollege().getId()));
        staffs.put(staff.getId(),staff);
    }
    //查询全部职工信息
    public Collection<Staff> getAll(){
        return staffs.values();
        }
    //通过id查找职工
    public Staff getStaffById(Integer id){
        return staffs.get(id);
    }
    //删除职工byid
    public void delete(Integer id){
        staffs.remove(id);
    }
}
