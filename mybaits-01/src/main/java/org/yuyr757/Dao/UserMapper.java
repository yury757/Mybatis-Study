package org.yuyr757.Dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.yuyr757.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * 查询全部用户
     */
    public List<User> getUserList();

    /**
     * 根据id查询用户
     */
    public User getUserById(int id);

    /**
     * 新增用户
     */
    public void addUser(User user);

    /**
     * 更新用户信息
     */
    public void updateUser(User user);

    /**
     * 根据id删除用户
     */
    public void deleteUser(int id);

    /* ------------------------以下是多个参数的例子--------------------------*/
    /**
     * 有两个类型相同的参数的查询
     */
    public List<User> getTwoUserById(int id1, int id2);

    /**
     * 有两个类型不同的参数的查询
     */
    public List<User> getTwoUserById2(int id, String name);

    /**
     * 有两个类型不同的参数的查询3
     */
    public List<User> getTwoUserById3(int id, User user);

    /**
     * 有两个类型不同的参数的查询4，使用@Param注解
     */
    public List<User> getTwoUserById4(@Param("id") int id,@Param("user") User user);

    /* -----------------------以下使用Map映射表来存储数据----------------------------------*/
    /* ------------在一些业务特别复杂的情况下可以这么用，业务简单最好还是用实体类------------------------*/
    public List<Map<String, Object>> getUserListUsingMap();
    public void addUserUsingMap(Map<String, Object> map);

    /* -----------------------模糊查询----------------------------------*/
    public List<User> getUserLike1(String name);
    public List<User> getUserLike2(String name);

    /* -----------------------分页查询----------------------------------*/
    public List<User> getUserLimit(Map<String, Integer> map);

    /* -----------------------使用注解开发----------------------------------*/

    /**
     * 对于这种很简单的sql，可以不用写mapper，直接写一个Select注解，里面传入sql值即可
     * 注意点：
     * 1、数据库字段名和类属性名要相同
     * 2、returnType为接口的返回类型
     * 3、parameterType为接口的参数类型
     * 实现方式：反射、动态代理
     */
    @Select("select * from user where id = #{param1}")
    public List<User> getUserByIdUsingAnnotation(int id);

    @Select("select * from user where id = #{param1} or name = #{param2}")
    public List<User> getUserByIdUsingAnnotation2(int id, String name);

    @Insert("insert into user(id, name, pwd) values (#{id}, #{name}, #{pwd})")
    public void addUserUsingAnnotation(User user);

    /* -----------------------使用缓存----------------------------------*/
    public User getUserByIdUsingCache(int id);

}
