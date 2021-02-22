package org.yuyr757.Dao;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import org.yuyr757.pojo.User;
import org.yuyr757.utils.MybatisUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMapperTest {
    @Test
    public void test(){
        // 获取SqlSession
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            // 使用mapper的方式一(推荐使用)：
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.getUserList();
            for (User user : userList) {
                System.out.println(user.toString());
            }

            // 使用mapper的方式二：
//            List<User> userList = sqlSession.selectList("org.yuyr757.Dao.UserDao.getUserList");
//            for (User user : userList) {
//                System.out.println(user.toString());
//            }

            // 关闭SqlSession
            // 若使用了try(SqlSession sqlSession = MybatisUtils.getSqlSession()){}
            // 则不用写关闭资源的代码，因为它实现了AutoClosable接口，会自动关闭
            // sqlSession.close();
        }
    }

    @Test
    public void testGetUserById(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getUserById(2);
            System.out.println(user.toString());
        }
    }

    @Test
    public void testAddUser(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User newuser = new User(3, "小明", "asdf");
            userMapper.addUser(newuser);
            sqlSession.commit();
            System.out.println("插入成功" + newuser.toString());
            userMapper.deleteUser(3);
            sqlSession.commit();
        }
    }

    @Test
    public void testUpdateUser(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getUserById(2);
            user.setName("张三y");
            userMapper.updateUser(user);
            sqlSession.commit();
            System.out.println("更新成功");
        }
    }

    @Test
    public void testDeleteUser(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.addUser(new User(3, "sohbho", "sfdgfn"));
            userMapper.deleteUser(3);
            sqlSession.commit();
            System.out.println("删除成功");
        }
    }

    /* ------------------------以下是多个参数的例子--------------------------*/
    @Test
    public void testGetTwoUserById(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.getTwoUserById(3, 4);
            for (User user : userList) {
                System.out.println(user);
            }
        }
    }

    @Test
    public void testGetTwoUserById2(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.getTwoUserById2(2, "yuyr757");
            for (User user : userList) {
                System.out.println(user);
            }
        }
    }

    @Test
    public void testGetTwoUserById3(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getUserById(1);
            List<User> userList = userMapper.getTwoUserById3(2, user);
            for (User item : userList) {
                System.out.println(item);
            }
        }
    }

    @Test
    public void testGetTwoUserById4() {
        try (SqlSession sqlSession = MybatisUtils.getSqlSession()) {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getUserById(1);
            List<User> userList = userMapper.getTwoUserById4(2, user);
            for (User item : userList) {
                System.out.println(item);
            }
        }
    }

    /* ------------------------以下使用Map的例子--------------------------*/
    @Test
    public void testAddUserUsingMap(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            Map<String, Object> map = new HashMap<>();
            map.put("ID", "3");
            map.put("NAME", "小明");
            map.put("PWD", "！@#￥%……&*（）——+《》？：“：{");
            userMapper.addUserUsingMap(map);
            sqlSession.commit();
            User user = userMapper.getUserById(3);
            System.out.println("插入成功：" + user.toString());
            userMapper.deleteUser(3);
            sqlSession.commit();
        }
    }

    @Test
    public void testGetUserListUsingMap(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<Map<String, Object>> userList = userMapper.getUserListUsingMap();
            for (Map<String, Object> stringObjectMap : userList) {
                System.out.println(stringObjectMap.toString());
            }
        }
    }

    /* ------------------------模糊查询有两种方式--------------------------*/
    @Test
    public void testGetUserLike1(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.getUserLike1("y");
            for (User user : userList) {
                System.out.println(user.toString());
            }
        }
    }

    @Test
    public void testGetUserLike2(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.getUserLike2("%y%");
            for (User user : userList) {
                System.out.println(user.toString());
            }
        }
    }

    /* ------------------------分页--------------------------*/
    @Test
    public void testGetUserLimit(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            Map<String, Integer> map = new HashMap<>();
            map.put("startIndex", 0);
            map.put("endIndex", 10);
            List<User> userList = userMapper.getUserLimit(map);
            for (User user : userList) {
                System.out.println(user.toString());
            }
        }
    }

    /* -----------------------使用注解开发----------------------------------*/
    @Test
    public void testGetUserByIdUsingAnnotation(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.getUserByIdUsingAnnotation(10);
            for (User user : userList) {
                System.out.println(user.toString());
            }
        }
    }

    @Test
    public void testGetUserByIdUsingAnnotation2(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = userMapper.getUserByIdUsingAnnotation2(10, "yuyr757");
            for (User user : userList) {
                System.out.println(user.toString());
            }
        }
    }

    @Test
    public void testAddUserUsingAnnotation(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true)){
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            userMapper.addUserUsingAnnotation(new User(1000, "1000name", "1000pwd"));
            User user = userMapper.getUserById(1000);
            System.out.println("新增成功：" + user.toString());
            userMapper.deleteUser(1000);
        }
    }

    /* -----------------------使用缓存----------------------------------*/

    /**
     * 本地缓存
     */
    @Test
    public void testGetUserByIdUsingCache(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true))
        {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getUserByIdUsingCache(2);
            System.out.println(user.toString());
            System.out.println("==============================================");
            // sql只执行了一次
            User user2 = userMapper.getUserByIdUsingCache(2);
            System.out.println(user.toString());
            System.out.println(user == user2);
        }
    }

    /**
     * 二级缓存
     * 当sqlsession关闭时，会把本地缓存扔到二级缓存中
     */
    @Test
    public void testGetUserByIdUsingCache2(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession(true);
            SqlSession sqlSession2 = MybatisUtils.getSqlSession(true))
        {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            User user = userMapper.getUserByIdUsingCache(2);
            System.out.println(user.toString());
            sqlSession.close();
            System.out.println("==============================================");
            UserMapper userMapper2 = sqlSession2.getMapper(UserMapper.class);
            User user2 = userMapper2.getUserByIdUsingCache(2);
            System.out.println(user.toString());
            System.out.println(user == user2);
        }
    }
}
