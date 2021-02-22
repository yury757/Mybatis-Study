## 一、Mybatis遇到的问题大部分有以下五类：

1、配置文件没有注册

2、绑定接口错误

3、方法名不对

4、返回类型不对

5、Maven导出资源问题

## 二、Mybatis实现方式

- 写一个实体类和对应的查询接口
- 本来我们应该手写实现这个查询接口的类，并在对应的方法里面写sql语句、使用SqlSession执行SQL语句，再把结果集强转成我们自己的实体类。
- Mybatis则不需要我们手写这个实现类，而是弄了一个mapper的xml文件，里面定义了**某个接口的某个方法的实现**，我们只需要在xml中定义这个方法的SQL语句、参数类型、参数集、结果类型、结果集等标签。
- 再将对应的mapper注册到Mybatis的配置文件中。
- **然后项目启动时，Mybatis框架去配置文件的注册中心中把注册过的类提前实现好，生成.class字节码文件（猜测）**，我们只需要通过`getMapper(UserDao.class)`方法（这个方法里面肯定封装了newInstance或类似的方法）就可以拿到对应类的实例，然后直接调用相应的方法就行。而且会自动帮我们把结果集封装到mapper定义的结果类型中。

## 三、Mybatis中的三个核心类：

### （1）SqlSessionFactoryBuilder

这个类是**用于创建SqlSessionFactory对象的**，SqlSessionFactory对象一旦创建就不再需要SqlSessionFactoryBuilder了。

```java
// 使用mybatis第一步，获取SqlSessionFactory对象
static{
    String resource = "mybatis-config.xml";
    InputStream inputStream = null;
    try {
        inputStream = Resources.getResourceAsStream(resource);
    } catch (IOException e) {
        e.printStackTrace();
    }
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
}
```

### （2）SqlSessionFactory

SqlSessionFactory一旦被创建，应该在程序运行期间一直存在，因为**它是创建SqlSession对象的工厂**。默认为单例模式。

### （3）SqlSession

SqlSession是用于访问数据库的一个会话。

- SqlSession实例**不是线程安全的**，因此避免被共享，最佳的使用域是请求或非静态方法作用域。
- 使用完一个SqlSession后**一定一定一定**要关闭它，为避免关闭资源时异常，最好使用以下方式使用SqlSession

```java
// 获取SqlSession对象的方法
public static SqlSession getSqlSession(){
    return sqlSessionFactory.openSession();
}

// 重载方法，选择是否自动提交
public static SqlSession getSqlSession(boolean autoCommit){
    return sqlSessionFactory.openSession(autoCommit);
}
```

```java
try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
    UserDao userDao = sqlSession.getMapper(UserDao.class);
    List<User> userList = userDao.getUserList();
    for (User user : userList) {
        System.out.println(user.toString());
    }
}
```

## 四、Mapper标签属性注意事项

- `id`：对应接口的方法名
- `resultType`：结果集类型，要写全限定类名，或别名
- `parameterType`：参数类型
- 当接口方法只有一个参数时，`#{}`中有以下几种填法
    - 若传入参数类型是一个实体类或其他类，`#{}`可直接填入相应属性名
    - 若传入参数类型是`Map`接口类（可以用别名`map`代表`Map`），`#{}`可直接填入相应的键值
    - 若传入参数是`String`、`int`等其他类型，`#{}`填任意值数字或字母的组合都行，建议使用`param1`
- 当接口方法只有多个参数时，`parameterType`可不填，`#{}`按接口方法的参数顺序填入`#{param1}`、`#{param2}`。或者在接口处使用`@param`注解，给参数起一个别名。

```xml
<!-- 有两个类型相同的参数的查询 -->
<select id="getTwoUserById" parameterType="int" resultType="org.xxxx.pojo.User">
    select * from user where id = #{param1} or id = #{param2}
</select>

<!-- 有两个类型不同的参数的查询2 -->
<select id="getTwoUserById2" parameterType="Object" resultType="org.xxxx.pojo.User">
    select * from user where id = #{param1} or name = #{param2}
</select>

<!-- 有两个类型不同的参数的查询3 -->
<select id="getTwoUserById3" resultType="org.xxxx.pojo.User">
    select * from user where id = #{param1} or id = #{param2.id}
</select>

<!-- 有两个类型不同的参数的查询4 -->
<select id="getTwoUserById4" resultType="org.xxxx.pojo.User">
    select * from user where id = #{id} or id = #{user.id}
</select>
```

```java
/**
 * 有两个类型不同的参数的查询4，使用@Param注解
 */
public List<User> getTwoUserById4(@Param("id") int id,@Param("user") User user);
```

- 、模糊查询有两种方式
    - 在mapper中这样用来拼接`%`：`like "%"#{param1}"%"`
    - mapper中仍然使用`like #{param1}`，而在调用方式时手动在传入参数两边加上`%`

推荐使用第一种，因为在参数里面加`%`可能面临被转义的风险。

```xml
<select id="getUserLike1" parameterType="string" resultType="org.xxxx.pojo.User">
    select * from user where name like "%"#{param1}"%"
</select>

<select id="getUserLike2" parameterType="string" resultType="org.xxxx.pojo.User">
    select * from user where name like #{param1}
</select>
```

- `resultMap`：结果集映射，将从数据库中取出来的字段和类中的属性做一个映射关系，为解决数据库字段名和类属性名不一致的问题。`column`为数据库字段名，`property`为类的属性名。

```xml
<resultMap id="UserMap" type="user">
    <result column="id" property="id"/>
    <result column="name" property="name"/>
    <result column="password" property="pwd"/>
</resultMap>
```



## 五、mybatis-config.xml配置解析

### （1）properties标签

可以引入其他某个`.properties`文件，作为参数值在本配置文件中使用。

```xml
<properties resource="db.properties"/>
```

也可以可以加入`property`标签加入自定义参数。

对于有重复的参数，参数调用顺序是，先生成`property`标签中的参数，再读取引入的配置文件中的参数，对于有重复的参数会被覆盖掉，理解成一个`HashMap`即可。

### （2）settings标签

有以下属性：https://mybatis.org/mybatis-3/zh/configuration.html#settings

主要用的有：

- cacheEnabled：缓存
- useGeneratedKeys：自动生成主键
- mapUnderscoreToCamelCase：数据库字段名转java属性名时自动重命名
- logImpl：日志实现类

官网也给了一个建议的设置如下：

```xml
<settings>
  <setting name="cacheEnabled" value="true"/>
  <setting name="lazyLoadingEnabled" value="true"/>
  <setting name="multipleResultSetsEnabled" value="true"/>
  <setting name="useColumnLabel" value="true"/>
  <setting name="useGeneratedKeys" value="false"/>
  <setting name="autoMappingBehavior" value="PARTIAL"/>
  <setting name="autoMappingUnknownColumnBehavior" value="WARNING"/>
  <setting name="defaultExecutorType" value="SIMPLE"/>
  <setting name="defaultStatementTimeout" value="25"/>
  <setting name="defaultFetchSize" value="100"/>
  <setting name="safeRowBoundsEnabled" value="false"/>
  <setting name="mapUnderscoreToCamelCase" value="false"/>
  <setting name="localCacheScope" value="SESSION"/>
  <setting name="jdbcTypeForNull" value="OTHER"/>
  <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
</settings>
```

### （3）typeAliases标签

为类型设置别名，这样避免了写全限定类型或全限定接口名。

当为一整个包的类设置别名时，若类型带有`@Alias`注解时，别名为注解值；否则别名为对应类的类型，首字母小写。

```xml
<typeAliases>
    <!-- <typeAlias type="org.yuyr757.pojo.User" alias="UserAlias"/> -->
    <package name="org.xxxxx.pojo"/>
</typeAliases>
```

Mybatis有一些默认别名，如下：https://mybatis.org/mybatis-3/zh/configuration.html#typeAliases

### （4）mappers映射器

官网有四种写法，使用完全限定资源定位符（URL）不推荐使用。

```xml
<mappers>
    <!-- <mapper resource="org/xxxx/Dao/UserMapper.xml"/>-->
    <!-- <mapper class="org.xxxx.Dao.UserMapper"/>-->
    <package name="org.xxxx.Dao"/>
</mappers>
```

最推荐使用第四种，将包内的映射器接口实现全部注册为映射器。使用条件：

- 接口和mapper必须放在同一个包下，建议包名为Dao，<font color="Red">同一个包下是指编译后同一个包下，可以在resources目录下也新建一个`org.xxxx.Dao`目录，这样接口和mapper配置就会编译到同一个包下了。</font>
- 接口和mapper两个文件名必须相同（文件类型后缀不管）
- 使用这种方式必须在`pom.xml`中把`src/java/main`下的xml文件作为配置文件添加到`build.resources.resource`中

```
java.org.xxxx.Dao
	UserMapper.java（接口）
    Department.java
resources.org.xxxx.Dao
    UserMapper.xml（mapper）
    Department.xml
```

## 六、分页

### 1、在mapper的sql语句中把startIndex和endIndex作为参数传入进去

### 2、分页插件PageHelper：https://pagehelper.github.io/

## N、注意事项

- insert、update、delete要手动提交事务：

```java
sqlSession.commit();
```

## 七、使用注解开发

实现方式：反射、动态代理

```java
/**
 * 对于这种很简单的sql，可以不用写mapper，直接写一个Select注解，里面传入sql值即可
 * 注意点：
 * 1、数据库字段名和类属性名要相同
 * 2、returnType为接口的返回类型
 * 3、parameterType为接口的参数类型
 */
@Select("select * from user where id = #{param1}")
public List<User> getUserByIdUsingAnnotation(int id);

@Select("select * from user where id = #{param1} or name = #{param2}")
public List<User> getUserByIdUsingAnnotation2(int id, String name);

@Insert("insert into user(id, name, pwd) values (#{id}, #{name}, #{pwd})")
public void addUserUsingAnnotation(User user);
```

## 八、连表查询1

在数据库设计时，为降低数据的冗余，一般都会做到三范式。比如学生老师信息表可能会做成以下这种方式：

比如一个学生表如下：

| ID   | NAME | TEACHER_ID |
| ---- | ---- | ---------- |
| 1    | 小明 | 1          |
| 2    | 小五 | 1          |
| 3    | 小华 | 3          |
| 4    | 小石 | 2          |
| 5    | 李笑 | 3          |
| 6    | 孙武 | 2          |
| 7    | 黄铭 | 2          |

一个老师表如下：

| ID   | NAME   |
| ---- | ------ |
| 1    | 李老师 |
| 2    | 黄老师 |
| 3    | 钱老师 |

因此我们的java对象应该是这样的：

```java
public class Student {
    private int id;
    private String name;
    private Teacher teacher; // 引用了一个老师
}

public class Teacher {
    private int id;
    private String name;
}
```

这样我们在配置mapper时有两种方法：

### 1、通过子查询方式

```xml
<select id="getStudent" resultMap="studentTeacher">
    select * from student
</select>

<resultMap id="studentTeacher" type="student">
    <result property="id" column="id"/>
    <result property="name" column="name"/>
    <!-- 对象使用association，集合使用collection -->
    <association property="teacher" column="teacher_id" javaType="teacher" select="getTeacher"/>
</resultMap>

<select id="getTeacher" resultType="teacher">
    select * from teacher where id = #{id}
</select>
```

其中`association`标签的属性解释：

- `property`：属性名
- `column`：该属性要用数据库中的某个字段名去关联查询
- `javaType`：该属性的类型
- `select`：从数据库拿到这个类的数据的select语句

这种方式实际上就是把查询出来的关联字段去重，去重后再去数据库里面查相应的数据，再封装到对象中。

如打开日志后可以发现这种方式实际上查了四次数据库。

```
2021-02-22 17:40:11[ DEBUG ]Opening JDBC Connection
2021-02-22 17:40:12[ DEBUG ]Created connection 202125197.
2021-02-22 17:40:12[ DEBUG ]==>  Preparing: select * from student 
2021-02-22 17:40:12[ DEBUG ]==> Parameters: 
2021-02-22 17:40:12[ DEBUG ]====>  Preparing: select * from teacher where id = ? 
2021-02-22 17:40:12[ DEBUG ]====> Parameters: 1(Integer)
2021-02-22 17:40:12[ DEBUG ]<====      Total: 1
2021-02-22 17:40:12[ DEBUG ]====>  Preparing: select * from teacher where id = ? 
2021-02-22 17:40:12[ DEBUG ]====> Parameters: 3(Integer)
2021-02-22 17:40:12[ DEBUG ]<====      Total: 1
2021-02-22 17:40:12[ DEBUG ]====>  Preparing: select * from teacher where id = ? 
2021-02-22 17:40:12[ DEBUG ]====> Parameters: 2(Integer)
2021-02-22 17:40:12[ DEBUG ]<====      Total: 1
2021-02-22 17:40:12[ DEBUG ]<==      Total: 7
Student{id=1, name='小明', teacher=Teacher{id=1, name='李老师'}}
Student{id=2, name='小五', teacher=Teacher{id=1, name='李老师'}}
Student{id=3, name='小华', teacher=Teacher{id=3, name='钱老师'}}
Student{id=4, name='小石', teacher=Teacher{id=2, name='黄老师'}}
Student{id=5, name='李笑', teacher=Teacher{id=3, name='钱老师'}}
Student{id=6, name='孙武', teacher=Teacher{id=2, name='黄老师'}}
Student{id=7, name='黄铭', teacher=Teacher{id=2, name='黄老师'}}
2021-02-22 17:40:12[ DEBUG ]Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@c0c2f8d]
2021-02-22 17:40:12[ DEBUG ]Returned connection 202125197 to pool.
```

### 2、通过连表查询方式

```xml
<select id="getStudent2" resultMap="studentTeacher2">
    select s.id, s.name, s.teacher_id, t.name as teacher_name
    from student s left join teacher t on s.teacher_id = t.id
</select>

<resultMap id="studentTeacher2" type="student">
    <result property="id" column="id"/>
    <result property="name" column="name"/>
    <association property="teacher" javaType="teacher">
        <result property="id" column="teacher_id"/>
        <result property="name" column="teacher_name"/>
    </association>
</resultMap>
```

同样有一个`association`标签，而下面还有封装这个`teacher`类的子标签，子标签定义了初始化这个类所需要的字段映射。

- `property`：属性名
- `javaType`：该属性的类型

这种方式只需要查一次数据库：

```
2021-02-22 17:41:58[ DEBUG ]Opening JDBC Connection
2021-02-22 17:41:59[ DEBUG ]Created connection 202125197.
2021-02-22 17:41:59[ DEBUG ]==>  Preparing: select s.id, s.name, s.teacher_id, t.name as teacher_name from student s left join teacher t on s.teacher_id = t.id 
2021-02-22 17:41:59[ DEBUG ]==> Parameters: 
2021-02-22 17:41:59[ DEBUG ]<==      Total: 7
Student{id=1, name='小明', teacher=Teacher{id=1, name='李老师'}}
Student{id=2, name='小五', teacher=Teacher{id=1, name='李老师'}}
Student{id=4, name='小石', teacher=Teacher{id=2, name='黄老师'}}
Student{id=6, name='孙武', teacher=Teacher{id=2, name='黄老师'}}
Student{id=7, name='黄铭', teacher=Teacher{id=2, name='黄老师'}}
Student{id=3, name='小华', teacher=Teacher{id=3, name='钱老师'}}
Student{id=5, name='李笑', teacher=Teacher{id=3, name='钱老师'}}
2021-02-22 17:41:59[ DEBUG ]Closing JDBC Connection [com.mysql.cj.jdbc.ConnectionImpl@c0c2f8d]
2021-02-22 17:41:59[ DEBUG ]Returned connection 202125197 to pool.
```

<font color="Red">具体使用哪种方式视情况而定，简单的连表可以使用第二种。当连表查询的sql特别复杂，以致于难以在sql层面去优化时，可以使用第一种，主查询把其他需要连的表的主键查询来，子查询再用主键去查，可能会提高效率。</font>

## 九、连表查询2

对于以上的学生老师表，我们的java类还可能是这样的：

```java
public class Student2 {
    private int id;
    private String name;
    private int teacherId;
}

public class Teacher2 {
    private int id;
    private String name;
    private List<Student> students; // 老师这里有多个学生对象的引用
}
```

同样有子查询和连表查询两种方式：

### 1、通过子查询方式

```xml
<!-- 子查询 -->
<select id="getTeacherById2" resultMap="teacher2Student2">
    select t.id, t.name from teacher t where t.id = #{id}
</select>

<resultMap id="teacher2Student2" type="teacher2">
    <result property="id" column="id"/>
    <result property="name" column="name"/>
    <collection property="students" column="id" javaType="ArrayList" ofType="student2" select="getStudent"/>
</resultMap>

<select id="getStudent" resultMap="student2Map">
    select * from student where teacher_id = #{id}
</select>

<resultMap id="student2Map" type="student2">
    <result property="teacherId" column="teacher_id"/>
</resultMap>
```

### 2、通过连表查询方式

```xml
<!-- 连表查询 -->
<select id="getTeacherById" resultMap="teacher2Student">
    select t.id, t.name, s.id as student_id, s.name as student_name
    from teacher t left join student s on t.id = s.teacher_id
    where t.id = #{id}
</select>

<resultMap id="teacher2Student" type="teacher2">
    <result property="id" column="id"/>
    <result property="name" column="name"/>
    <!-- 这里要用ofType，即集合的元素类型 -->
    <collection property="students" ofType="student2">
        <result property="id" column="student_id"/>
        <result property="name" column="student_name"/>
        <result property="teacherId" column="id"/>
    </collection>
</resultMap>
```

## 十、缓存

### 1、本地缓存。

作用域为SqlSession，默认开启。

在一个session中查两次相同的sql，只会执行一次sql，第二次拿到的对象，和第一次拿到的对象的<font color="Red">地址都是一样的。</font>**本地缓存将会在做出修改、事务提交或回滚，以及关闭session时清空。默认情况下，本地缓存数据的生命周期等同于整个session的周期。**

### 2、二级缓存。

作用域为mapper的namespace，<font color="Red">当sqlsession作出修改、事务提交、回滚或关闭时，会把本地缓存扔到二级缓存中。即一级缓存失效时，会把其缓存的数据扔到二级缓存中。</font>

需要在mapper中加入`<cache/>`就可以为这个mapper开启二级缓存。

```xml
<cache
       eviction="FIFO"
       flushInterval="60000"
       size="512"
       readOnly="true"/>
```

在`mybatis-config.xml`配置中，设置`cacheEnabled`为true可以为所有mapper开启二级缓存。

缓存清除策略：

- `LRU` – 最近最少使用：移除最长时间不被使用的对象。
- `FIFO` – 先进先出：按对象进入缓存的顺序来移除它们。
- `SOFT` – 软引用：基于垃圾回收器状态和软引用规则移除对象。
- `WEAK` – 弱引用：更积极地基于垃圾收集器状态和弱引用规则移除对象。

### 3、缓存顺序

**二级缓存 => 本地缓存 => 数据库**

















