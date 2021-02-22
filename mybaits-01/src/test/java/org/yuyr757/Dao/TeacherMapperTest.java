package org.yuyr757.Dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.yuyr757.pojo.Teacher;
import org.yuyr757.utils.MybatisUtils;

public class TeacherMapperTest {
    @Test
    public void testGetTeacher(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true)){
            TeacherMapper teacherMapper = sqlSession.getMapper(TeacherMapper.class);
            Teacher teacher = teacherMapper.getTeacher(2);
            System.out.println(teacher.toString());
        }
    }
}
