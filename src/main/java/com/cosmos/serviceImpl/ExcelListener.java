package com.cosmos.serviceImpl;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cosmos.pojo.Course;
import com.cosmos.pojo.Staff;
import com.cosmos.pojo.Student;
import com.cosmos.service.AdminService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ExcelListener extends AnalysisEventListener<Object> {//表格处理
    private final AdminService adminService;
    private final String role ;
//  有个很重要的点 StudentListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
    @Override
    public void invoke(Object obj, AnalysisContext context) {
        System.out.println(obj);
        switch (role) {
            case "student":
                Student student = (Student) obj;
                adminService.addStudent(student);
                break;
            case "staff":
                Staff staff = (Staff) obj;
                adminService.addTeacher(staff);
                break;
            case "course":
                Course course = (Course) obj;
                adminService.addCourse(course);
                break;
            case "studentCourse":
                Map map = (Map) obj;
                if(map.get(0) != null&&map.get(1) != null) {
                    adminService.addStudentCourse(map.get(0).toString(),map.get(1).toString());
                }
                break;
        }
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {//所有数据解析完成了,都会来调用
        System.out.println("完事");
    }

    public ExcelListener(AdminService adminService, String role) {//easyExcel深坑，必须要传过来，@Autowired不管用
        this.adminService = adminService;
        this.role = role;
    }
}
