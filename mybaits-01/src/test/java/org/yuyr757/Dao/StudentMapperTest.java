package org.yuyr757.Dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.yuyr757.pojo.Student;
import org.yuyr757.pojo.Teacher;
import org.yuyr757.utils.MybatisUtils;

import java.util.List;

public class StudentMapperTest {
    @Test
    public void testGetTeacher(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true)){
            StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
            List<Student> studentList = studentMapper.getStudent();
            for (Student student : studentList) {
                System.out.println(student.toString());
            }
        }
    }

    @Test
    public void testGetTeacher2(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true)){
            StudentMapper studentMapper = sqlSession.getMapper(StudentMapper.class);
            List<Student> studentList = studentMapper.getStudent2();
            for (Student student : studentList) {
                System.out.println(student.toString());
            }
        }
    }
}
