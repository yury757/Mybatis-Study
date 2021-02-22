package org.yuyr757.Dao;

import org.yuyr757.pojo.Student;

import java.util.List;

public interface StudentMapper {

    /**
     * 获取所有的学生信息，包括其老师信息
     */
    public List<Student> getStudent();
    public List<Student> getStudent2();
}
