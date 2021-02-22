package org.yuyr757.Dao;

import org.apache.ibatis.session.SqlSession;
import org.yuyr757.pojo.User;
import org.yuyr757.utils.MybatisUtils;

import java.util.List;

/**
 * 本来我们使用接口的实现类来写sql语句，查询sql，并封装到实体类中
 * mybatis把这些代码都省略掉了，弄了一个mapper的xml
 * 在mapper.xml中填写相应配置就可以实现以下的实现类
 */
//public class UserMapperImpl implements UserMapper {
//    public List<User> getUserList() {
//        String sql = "select * from user";
//        SqlSession sqlSession = MybatisUtils.getSqlSession();
//        List<Object> objects = sqlSession.selectList(sql);
//        // 再把List<Object> objects封装成List<User>
//        return null;
//    }
//}
