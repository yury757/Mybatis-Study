package org.yuyr757.Dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.yuyr757.pojo.Student;
import org.yuyr757.pojo.Teacher;
import org.yuyr757.pojo.Teacher2;
import org.yuyr757.utils.MybatisUtils;

import java.util.List;

public class Teacher2MapperTest {
    @Test
    public void testGetTeacherById(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true)){
            Teacher2Mapper teacher2Mapper = sqlSession.getMapper(Teacher2Mapper.class);
            Teacher2 teacher2 = teacher2Mapper.getTeacherById(2);
            System.out.println(teacher2);
        }
    }

    @Test
    public void testGetTeacherById2(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true)){
            Teacher2Mapper teacher2Mapper = sqlSession.getMapper(Teacher2Mapper.class);
            Teacher2 teacher2 = teacher2Mapper.getTeacherById2(2);
            System.out.println(teacher2);
        }
    }
}
