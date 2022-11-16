## 一 课程介绍

1. IDEA插件安装
2. 回顾Mybatis
3. Mybatis映射器
4. Mybatis sql高级特性-高级查询
5. Mybatis sql高级特性-批量操作
6. 关联映射
7. 缓存



## 二 IDEA插件安装

开门福利：插件安装  free mybatis plugins---鸟

![image-20210525095447871](MyBatis-笔记-DAY02.assets\image-20210525095447871.png)

![image-20210525095611853](MyBatis-笔记-DAY02.assets\image-20210525095611853.png)



翻译插件 translation 安装--**A8**

![image-20210525100036521](\MyBatis-笔记-DAY02.assets\image-20210525100036521.png)



开启IDEA自动导包

![image-20210525105013791](MyBatis-笔记-DAY02.assets/image-20210525105013791.png)







## 三.Mybatis回顾

mybatis的基本使用咱们已经学习过。在项目中也一直在使用mybatis。现在咱们来回顾一下mybatis的基本使用，但是区别在于，今天咱们使用idea以及maven来进行操作。

### 1.项目搭建

#### 1.1.idea中创建maven项目

![1621901474820](MyBatis-笔记-DAY02.assets/1621901474820.png)

　![1621901501216](MyBatis-笔记-DAY02.assets/1621901501216.png)

　![1621901523968](MyBatis-笔记-DAY02.assets/1621901523968.png)　

####　1.2.maven导包

回顾：以前导包是直接拷备jar包(1个核心包,7个依赖包,1个数据库连接包)　，maven导包都是在pom.xml中进行配置(代码如下)

导包部分大家直接拷备使用即可，但是**要求必需知道每个配置的含义**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>cn.ronghuanet</groupId>
    <artifactId>mybatis-day02</artifactId>
    <version>1.0-SNAPSHOT</version>
    <dependencies>
    	<!-- mybatis核心包 -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.2.1</version>
        </dependency>
        <!-- mysql驱动包 -->
        <dependency>
             <groupId>mysql</groupId>
             <artifactId>mysql-connector-java</artifactId>
             <version>5.1.26</version>
         </dependency>
         <!-- junit测试包 -->
         <dependency>
             <groupId>junit</groupId>
             <artifactId>junit</artifactId>
             <version>4.12</version>
             <scope>test</scope>
         </dependency>
        <!-- https://mvnrepository.com/artifact/log4j/log4j -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>
  </dependencies>
    <!-- 局部jdk 1.8配置，pom.xml中 -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 2.MyBatis集成

#### 2.1.mybatis配置文件

这个地方咱们不做详细的解释，大家也可以把以前的文件拷备过来。注意一下文件的位置：src/main/resources 文件夹下面

log4j.properties

```properties
#\u5168\u5C40\u65E5\u5FD7\u914D\u7F6E
log4j.rootLogger = info,console

#log level: 
#debug is used by develop,  error or warn is used by online
log4j.logger.com.ronghuanet=debug


### \u914D\u7F6E\u8F93\u51FA\u5230\u63A7\u5236\u53F0
log4j.appender.console = org.apache.log4j.ConsoleAppender
### \u4F7F\u7528System.out\u6253\u5370\u65E5\u5FD7
log4j.appender.console.Target = System.out
### \u6307\u5B9A\u65E5\u5FD7\u7684\u683C\u5F0F\u5E03\u5C40(\u65E5\u5FD7\u662F\u6709\u683C\u5F0F\u7684)
log4j.appender.console.layout = org.apache.log4j.PatternLayout
### \u65E5\u5FD7\u7684\u6253\u5370\u683C\u5F0F
log4j.appender.console.layout.ConversionPattern =  %d{ABSOLUTE} %5p %c{1}:%L - %m%n
```

先准备jdbc.properties

```properties
jdbc.username=root
jdbc.password=admin
jdbc.url=jdbc:mysql:///mybatis
jdbc.driverClassName=com.mysql.jdbc.Driver
```

创建：mybatis-config.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
   <!-- 加载jdbc.properties-->
    <properties resource="jdbc.properties"/>
    <!--申明操作数据库的环境-->
    <environments default="MYSQL">
        <environment id="MYSQL">
            <!--使用jdbc的事务-->
            <transactionManager type="JDBC"/>
            <!--支持连接池-->
            <dataSource type="POOLED">
                <!--自动补全结构：ctrl+shift+回车-->
                <property name="username" value="${jdbc.username}"/>
                <property name="password" value="${jdbc.password}"/>
                <property name="url" value="${jdbc.url}"/>
                <property name="driver" value="${jdbc.driverClassName}"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
       <!-- 加载mapper.xml文件-->
    </mappers>
</configuration>
```







#### 2.2.创建MyBatisUtils

```java
public class MybatisUtils {
    private static SqlSessionFactory sessionFactory ;
    static{
        try {
            //提示处理异常快捷键    alt+回车       移动代码的快捷键  alt+上键或者下键
            //快速使用变量接收，    alt+回车
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            //根据io流创建SqlSessionFactory对象
            sessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static SqlSession openSession(){
        if (sessionFactory != null) {
            return sessionFactory.openSession();
        }
        return null;
    }
}
```



#### 2.3  测试Mybatis是否可用

```java
package com.ronghuanet;

import com.ronghuanet.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

/**
 * 测试mybatis集成进来没有
 */
public class MybatisTest {

    @Test
    public void test() throws Exception{
        SqlSession sqlSession = MybatisUtils.openSession();
        System.out.println(sqlSession);
        MybatisUtils.closeSession(sqlSession);

    }
}

```



#### 2.4.创建模型

```java
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    /**
     * alt+insert:快速生成setter  getter方法  toString方法   构造方法
     */
	//getter，setter代码略...
}
```

#### 2.3.productMapper接口

```java
public interface ProductMapper {
    Product findOne(Long id);
}
```

#### 2.4.创建productMapper.xml文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<!--注：namespace的内容就是ProductMapper接口的全限定名-->
<mapper namespace="cn.ronghuanet.mybatis._01_batch.mapper.ProductMapper">
    <!--id的值保证ProductMapper接口的方法名一值-->
    <select id="findOne" parameterType="long" resultType="cn.ronghuanet.mybatis._01_batch.domain.Product">
        select * from product where id = #{id}
    </select>
</mapper>
```

#### 2.5.注册mapper.xml

主配置文件(mybatis-config.xml)中引入

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
     ...
    <mappers>
       <!-- 加载mapper.xml文件-->
        <mapper resource="cn/ronghuanet/mybatis/_01_batch/mapper/ProductMapper.xml" />
    </mappers>
</configuration>
```

`【注意】大家特别注意文件的位置`

![1621901895746](MyBatis-笔记-DAY02.assets/1621901895746.png) 



`【注意】创建resources中文件夹的方式(小心再小心)`

![1621901944049](MyBatis-笔记-DAY02.assets/1621901944049.png) 

#### 3.1.获取单个对象

```java
public class MyBatisTest {
    /**
     * 定义一个映射器mapper接口，使用mybatis自动为我们创建代理类
     * @throws Exception
     */
    @Test
    public void findOne()throws Exception{
        //获取到会话对象
        SqlSession session = MybatisUtils.openSession();
        //拿到映射对象，可以做相应的操作
        ProductMapper mapper = session.getMapper(ProductMapper.class);
        Product product = mapper.findOne(1L);

        System.out.println(product);
    }
}
```

## 二.MyBatis映射器(重要)

#### 1什么是映射器

我感觉到了有点不爽，根据我们上面代码我们已经可以完成单表的CRUD了，但是我们发现我们的dao实现类中出现了大量重复的代码，并且每个方法中的代码都相对比较复杂。手动去拼Sql的Statement很容易出错。

MyBatis的映射器就是用来解决这一问题，映射器其实是一个Interface接口,我们通过编写简单的映射器接口，就可以将我们之前在Dao中做的重复的，看起来比较低级的代码给替换掉。也就是说我们以后不用向之前那样写代码，而是直接调用映射器接口即可完成SQL的调用。我们可以理解为 这个映射器 底层使用了动态代理的方式将复杂的代码进行了处理，屏蔽了细节。--- 如同JPA的接口

#### 2创建Domain

新开一个包  _02_mapper，创建domain/Employee 实体类

```java
public class Employee{
    private Long id;
    private String username;
    private Integer age = 0;
    private Boolean sex;
    //...
}
```

#### 3创建映射器接口

创建实体类接口，我们一般给接口命名： XxxMapper  ,如 EmployeeMapper ，跟EmployeeMapper.xml同名。在接口中编写相应的CRUD方法 ：cn.ronghuanet.mybatis._02_mapper.mapper.EmployeeMapper

```java
package cn.ronghuanet.mybatis._02_mapper.mapper;

import cn.ronghuanet.mybatis._02_mapper.domain.Employee;

import java.util.List;

public interface EmployeeMapper {
    int insert(Employee employee);
    int updateById(Employee employee);
    int deleteById(Long id);
    Employee selectById(Long id);
    List<Employee> selectAll();
}

```

#### 4创建SQL映射文件

需要注意：SQL映射文件的namespace必须和 映射器接口的全限定名一直，这个非常重要，因为我们在调用映射器接口的时候，底层是通过该接口的 ==全限定名+方法名== 去匹配 SQL映射文件的==namespace+id==。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.ronghuanet.mybatis._02_mapper.mapper.EmployeeMapper">

    <resultMap id="baseResultMap" type="cn.ronghuanet.mybatis._02_mapper.domain.Employee">
        <id column="id" property="id" />
        <result column="username" property="username"/>
        <result column="age" property="age"/>
        <result column="sex" property="sex"/>
    </resultMap>

    <select id="selectAll" resultMap="baseResultMap" >
        select
            id,
            username,
            age,
            sex
        from employee
    </select>
</mapper>
```

注意：这里是另外一个Mapper，对应的是Employee，所以所有的 有用到实体类的地方都要使用Employee的全限定名。

再次强调：底层是通过映射器接口的 ==全限定名+方法名== 去匹配 SQL映射文件的==namespace+Id==，所以：

- namespace 需要和 映射器接口的全限定名一致
- sql的id必须和 映射器的 方法名一致。

这里暂时写了一个查询所有的方法，同学们自行完成其他的几个方法

#### 5注册Mapper

修改mybatis-config.xml文件，添加<mappers> , 注意修改成自己的路径

```xml
<mappers>
    <mapper resource="cn/ronghuanet/mybatis/mapper/ProductMapper.xml"/>
    <mapper resource="cn/ronghuanet/mybatis/mapper/EmployeeMapper.xml"/>
</mappers>
```



#### 6 实现CRUD操作

编写测试类，使用Mapper接口映射器进行CRUD

```java
@Test
public void selectAll() {
    try(SqlSession sqlSession = MyBatisUtil.openSession()){
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
        employeeMapper.selectAll().forEach(e ->{
            System.out.println(e);
        });
    }

}
```

其他方法留给你们自己实现

## 三. Sql编写高级特性-高级查询

### 1 什么是高级查询

1. **高级查询即为多条件查询**，为了快速查找想要的信息，过滤掉不需要的信息，这种查询在web项目中是必不可少的。例如一些后台管理系统、招聘网站等；

   ![image-20220422104824337](images/image-20220422104824337-1651219015892.png)

2. 如图所示，根据查询条件查询出相应的内容，可以是多个查询条件任意组合，查询出的结果都是满足查询条件的；

3. 高级查询可以使用Mybatis的动态Sql实现，减少了原始的很多逻辑判断代码，提高开发效率；

### 2 Query规范

1. 一般高级查询会将前端传递的参数封装成一个XxxQuery对象，方便维护和扩展；

2. 一个实体类对应一个Query对象；

   例如：针对上面的页面中的两个查询条件，我们可以写一个XxxQuery类【Xxx是实体类名或模块名或表名】，Controller直接使用这个类型去接收前端传递过来的数据，然后将该对象传递到Mapper中使用动态sql查询；

```java
public class ArticleQuery{
	//文章标题
	private String title;
	//默认启用状态
	private Boolean enable;
     //此处省略getter和setter方法
}
```



### 3 高级查询实现

#### 3.1.动态SQL - Concat

我们先使用一个高级查询案例来演示 

##### 3.1.1.编写查询对象

```java
public class EmployeeQuery {
    private Integer sex;
    private String username;
    //...
}
```

##### 3.1.2.编写Mapper查询方法

```java
public interface EmployeeMapper {
	List<Employee> selectForList(EmployeeQuery employeeQuery);
    //...
}
```

##### 3.1.3.编写SQL

这里有一个条件，就是根据 username like “%zs%” 进行查询，那么该如何将这个条件优雅的拼接到SQL中呢

方式一 ：`"%"#{username}"%"`    不推荐

```xml
    <select id="selectForList" resultMap="baseResultMap" >
        SELECT
            id,
            username,
            age,
            sex
        FROM employee WHERE username like "%"#{username}"%"
    </select>
```

方式二 `CONCAT("%",#{username},"%")` ：标准

```xml
    <select id="selectForList" resultMap="baseResultMap" >
        SELECT
            id,
            username,
            age,
            sex
        FROM employee WHERE username like CONCAT("%",#{username},"%")
    </select>
```

- **CONCAT: mysql提供的用来拼接字符串的函数。**

##### 3.1.4.编写测试

```java
  @Test
    public void selectForList() {
        try(SqlSession sqlSession = MyBatisUtil.openSession()){
            //查询条件
            EmployeeQuery employeeQuery = new EmployeeQuery() ;
            employeeQuery.setUsername("zs");

            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            employeeMapper.selectForList(employeeQuery).forEach(e ->{
                System.out.println(e);
            });
        }
    }
```



#### 3.2.动态SQL -If

在上面的查询中，我们有这样的查询条件 , 其实这样的写法是不合理的，如果 username的值为 “null” 或者“”空字符串呢？是不是这个SQL就查询不出内容了，所以我们在添加查询条件的时候一般==需要判断条件的值不为空的时候在加条件==

```xml
WHERE username like CONCAT("%",#{username},"%")
```

##### 3.2.1.SQL条件增加IF判断

**方式一**：在sql中 使用 if 增加空值判断，如果username的值不为空再加上 where条件

```xml
<select id="selectForList" resultMap="baseResultMap" >
    SELECT
    id,
    username,
    age,
    sex
    FROM employee
    <if test="username != null and username != ''">
        WHERE username like concat("%",#{username},"%")
    </if>
</select>
```

这种方式我个人不推荐，在低版本的mybatis中，会把空字符串 ' ' 识别为数字类型导致类型转换异常。我比较推荐使用下面这种方式：

**方式二**：在SQL中使用if判断， 在查询对象中处理空字符串。

修改查询对象如下：

```java
public class EmployeeQuery {
    private Integer sex;
    private String username;

    public String getUsername() {
        //如果是空或者空字符串，就返回null,否则就返回 原本的值，使用trim去掉两端的空格
        return username == null || username.length() == 0? null : username.trim();
    }
    //...
}
```

修改SQL如下：

```xml
<select id="selectForList" resultMap="baseResultMap" >
    SELECT
    id,
    username,
    age,
    sex
    FROM employee
    <if test="username != null">
        WHERE username like concat("%",#{username},"%")
    </if>
</select>
```



#### 3.3.动态SQL-WHERE

我们上面演示了一个条件的情况，可以通过IF来判断条件的空值然后在加入WHERE。那如果有两个以上的条件呢？

##### 3.3.1.修改SQL增加查询条件

在原本的SQL基础上增加一个age

```xml
<select id="selectForList" resultMap="baseResultMap" >
    SELECT
    id,
    username,
    age,
    sex
    FROM employee
    <if test="username != null and username != ''">
        WHERE username LIKE concat("%",#{username},"%")
    </if>
    <if test="age != null">
        AND age = #{age}
    </if>
</select>
```

测试代码

##### 3.3.2.测试代码

```java
@Test
public void selectForList() {
    try(SqlSession sqlSession = MyBatisUtil.openSession()){
        //查询条件
        EmployeeQuery employeeQuery = new EmployeeQuery() ;
        employeeQuery.setUsername("zs");
        //增加age查询条件
        employeeQuery.setAge(11);
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
        employeeMapper.selectForList(employeeQuery).forEach(e ->{
            System.out.println(e);
        });
    }
}
```

经过测试，这个代码没有什么问题，但是如果我把 username的条件注释呢？如下：

```java
 //employeeQuery.setUsername("zs");
//增加age查询条件
employeeQuery.setAge(11);
```

经过测试，该代码会出现如下异常：

>  SQL: SELECT             id,             username,             age,             sex         FROM employee                                 AND age = ?
>  Cause: com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException: You have an error in your SQL syntax; check the manual that corresponds to your MySQL server version for the right syntax to use near 'AND age = 11' at line 9

这里的是SQL语法错误，很奇怪，注释掉一个条件怎么会出现语法错误呢？你观察他的SQL   `... FROM employee AND age = ?` ,这个很明显不对，SQL怎么会变成这个样子,WHERE 关键字去哪儿了？这个是因为我们的 IF判断造成的，请看:

```xml
SELECT
    id,
    username,
    age,
    sex
FROM employee
<if test="username != null">
    WHERE username LIKE concat("%",#{username},"%")
</if>
<if test="age != null">
    AND age = #{age}
</if>
```

问题就在  `<if test="username != null">` 这里，当查询对象中 username没有值，就意味着 `WHERE username LIKE concat("%",#{username},"%")` 条件不会被加入SQL，那么SQL就变成了 `...FROM employee And age = #{age}` 这个语法很明显不对，所以我们要对这种情况进行处理，可以使用 WHERE动态sql。

##### 3.3.3.使用WHERE动态SQL

```xml
<select id="selectForList" resultMap="baseResultMap" >
        SELECT
            id,
            username,
            age,
            sex
        FROM employee
        <where>
            <if test="username != null and username != ''">
                AND username LIKE concat("%",#{username},"%")
            </if>
            <if test="age != null">
                AND age = #{age}
            </if>
        </where>
    </select>
```

在条件外面套一个 <where> 元素 ，里面写IF判断，增加条件 ，所有的条件前面全部用 AND ，Mybatis会自动把第一个条件前面变成where。  

注意喔：不要自以为是的把第一个条件前面的AND 变成WHERE 。都用AND就对了。





#### 3.4.公共SQL片段的抽取

当我们的SQL中的内容写得比较多的时候，看起来就比较臃肿，加载上有些SQL片段是公共的，比如 selectForList和 selectForCount 拥有相同的 where条件。对于这些内容我们可以进行统一抽取，让SQL的结构看起来更清爽。

##### 3.4.1.抽取WHERE条件

```xml
    <sql id="base_where">
        <where>
            <if test="username != null and username != ''">
                AND username LIKE concat("%",#{username},"%")
            </if>
            <if test="age != null">
                AND age = #{age}
            </if>
        </where>
    </sql>

    <select id="selectForList" resultMap="baseResultMap" >
        SELECT
            id,
            username,
            age,
            sex
        FROM employee
        <include refid="base_where"/>
    </select>
```

使用  <sql  id=""/> 标签来抽取公共的SQL片段，然后使用   <include refid="sql片段ID"/> 来引入抽取的SQL片段 ，代码结构是不是编的清爽了呢？

##### 3.4.2.抽取查询的列

通常我们除了要抽取WHERE以外，还会对查询的列进行抽取

```xml
<sql id="base_where">
        <where>
            <if test="username != null and username != ''">
                AND username LIKE concat("%",#{username},"%")
            </if>
            <if test="age != null">
                AND age = #{age}
            </if>
        </where>
    </sql>
    
    <sql id="base_columns">
           id,username,age,sex
    </sql>

    <select id="selectForList" resultMap="baseResultMap" >
        SELECT 
        <include refid="base_columns"/>
        FROM employee
        <include refid="base_where"/>
    </select>
```

#### 3.5.特殊符号 -“>” , "<"处理

我们来玩一个范围查询，修改EmployeeQuery，增加查询条件字段。

##### 3.5.1.修改查询对象

```java
public class EmployeeQuery {

    private Integer minAge;
    private Integer maxAage;
	//...
```

##### 3.5.2.修改SQL增加条件

```xml
 <sql id="base_where">
        <where>
            <if test="minAge != null">
                and age >= #{minAage}
            </if>
            <if test="maxAge != null">
                and age <= #{maxAge}
            </if>
            <if test="username != null and username != ''">
                AND username LIKE concat("%",#{username},"%")
            </if>
            <if test="age != null">
                AND age = #{age}
            </if>
        </where>
    </sql>
```

这个SQL一写出来IDEA编译器就会检查出错，直接测试的话会出现如下错误

> Cause: org.apache.ibatis.builder.BuilderException: Error parsing SQL Mapper Configuration. Cause: org.apache.ibatis.builder.BuilderException: Error creating document instance.  Cause: org.xml.sax.SAXParseException; lineNumber: 21; columnNumber: 26; 元素内容必须由格式正确的字符数据或标记组成。

因为在XML里面， "<" 符号是一个特殊符号，在这里不能别识别成小于符号，如何处理呢？

**方式一：** 使用  `&gt;=` 大于等于 ， `%lt;=` 小于等于 ; 这个是W3C特殊符号的转移符号。

```xml
 <if test="maxAge != null">
     and age $lt;= #{maxAge}
</if>
```

**方式二：**使用 CDATA 原样输出， <![CDATA[ 内容 ]]> 这个是固定格式

```xml
    <if test="maxAge != null">
        <![CDATA[
            and age   <=  #{maxAge}
        ]]>
    </if>
```

##### 5.3.测试代码

```java
@Test
    public void selectForList() {
        try(SqlSession sqlSession = MyBatisUtil.openSession()){
            //查询条件
            EmployeeQuery employeeQuery = new EmployeeQuery() ;
            //employeeQuery.setUsername("zs");
            //增加age查询条件
            //employeeQuery.setAge(11);
            employeeQuery.setMinAge(10);
            employeeQuery.setMaxAge(20);
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            employeeMapper.selectForList(employeeQuery).forEach(e ->{
                System.out.println(e);
            });
        }
    }
```

#### 3.6.取值表达式 #{}和${}

我们之前的SQL中取方法传入的参数值使用的都是#{} ， 其实${}也可以用来取值，但是他们有有着很大的区别，和不同的使用场景。

##### 3.6.1.#{}和${}的相同点

都可以用来取值

##### 3.6.2.不同点

其实==#{}的底层就是使用的 “?”占位符==的方式来拼接SQL，而==${}使用的是直接把拼接到SQL中==。 如有两条SQL如下：

```xml
    <select id="selectById" parameterType="long" resultType="...Employee">
        select * from employee where id = #{id}
    </select>
-----------------------------------------------------------------------------------------------------
    <select id="selectById" parameterType="long" resultType="...Employee">
        select * from employee where id = ${id}
    </select>
```

上面是接受==普通数据类型的参数==， 他们表现出如下区别：

| 接受的参数类型                          | where id = ==#{id}==                                       | where id = ==${id}==                                         |
| --------------------------------------- | ---------------------------------------------------------- | ------------------------------------------------------------ |
| 普通类型参数(8大基本数据类型和其包装类) | 正常取值，控制台SQL: `select * from employee where id = ?` | 取不到，抛出异常：Cause: org.apache.ibatis.reflection.ReflectionException: `There is no getter for property named 'id' in 'class java.lang.Long'` |

使用#{}正常取值，使用${}抛出异常，根据异常我们可以看到，其实他去参数值找了一个名字叫 “getId”的属性。说白了就是这个==它默认是取对象的属性值。而不能用来取一个普通值。==



增加一个方法，我们把SQL的参数类型修改为对象类型Employee,(mapper接口也要改哦)如有下面SQL：

```xml
    <select id="selectBy" parameterType="cn.ronghuanet.mybatis._02_mapper.domain.Employee"
            resultType="cn.ronghuanet.mybatis._02_mapper.domain.Employee">
         select * from employee where id = #{id}
    </select>
-----------------------------------------------------------------------------------------------------
   <select id="selectBy" parameterType="cn.ronghuanet.mybatis._02_mapper.domain.Employee"
            resultType="cn.ronghuanet.mybatis._02_mapper.domain.Employee">
         select * from employee where id = ${id}
    </select>
```

这里的==参数类==型是一个对象Employee,==那么使用#{}，和${}他们的区别表现如下

| 接受的参数类型                          | where id = #{id}                                           | where id = ${id}                                           |
| --------------------------------------- | ---------------------------------------------------------- | ---------------------------------------------------------- |
| 普通类型参数(8大基本数据类型和其包装类) | 正常取值，控制台SQL: `select * from employee where id = ?` | 正常取值，控制台SQL：`select * from employee where id = 1` |

对象类型，两种方式取值都可以，只是在拼接SQL上面有些区别，# 是使用?占位 ，而 $是直接把参数拼接到SQL中。

##### 3.6.3.做个小结

`#` 的特点

- `#` 它可以用来取任意值 ，普通值和对象都可以
- `#` 使用的占位符 ？拼接SQL，有效防止SQL注入 - 预编译
- `#` 使用预编译性能较高
- `#` 在接受字符串的时候会给字符串的两边加上“”引号 ，如username=zs ,使用 `where username = #{username}` 最终效果 `where username = "zs"` ，而使用 `where username = ${username}`  最终效果 `where username = zs` ，值的两边没有引号，会导致SQL异常，出现找不到 column的情况。

``$`` 的特点

- `$` 它只能取对象中的属性值。
- ``$`` 没有使用？占位符，直接拼接参数到SQL有SQL注入的风险 - 没有预编译
- `$` 没有使用预编译，性能比 `#`低一点
- ``$`` 在接受字符串的时候，不会给字符串的两边加上“”引号，所以一般用来接受字段名或者表名的时候用如：

```
select id,${fieldName}  from ${tableName} order by ${orderColumns}
```

如何选择？ 尽量用 #{} 来取值 ，${}一般用来动态排序。

例如： 表明 , 字段名，或者 排序条件 需要动态传入的时候，可以使用${}。

## 四. Sql编写高级特性-批量增删

当我们在删除多条数据，或者添加多条数据时，我们可能会选择for循环的方式调用mapper逐条删除或添加，如果有N条数据你就会调用N次mapper，这样的代码无疑是非常消耗性能的，我们可以通过MyBatis的动态SQL `foreach`来实现批量删除和批量添加，调用一次mapper就能添加n条数据，提升性能。

### 1.SQL-foreach-批量删除

有的时候我们需要对数据进行批量操作，如根据多个ID查询，或者根据多个ID删除，我们可以通过 `in(1,2)` 来实现，编写如下SQL

```sql
SELECT * from employee where id in (1,2);	//批量查询
DELETE   from employee where id in (3,4)	//批量删除
```

那么在MyBastis怎么批量删除呢？

#### 1.1.批量删除方法

批量删除方法，参数是一个LIST,即：根据多个ID来删除。

```java
public interface EmployeeMapper {
    //...
    int batchDelete(List<Long> ids);
}
```

#### 1.2.编写SQL

```xml
<delete id="batchDelete" parameterType="list">
        DELETE FROM employee
        where id in
        <foreach collection="list" open="(" item="id" separator="," close=")" >
            #{id}
        </foreach>
    </delete>
```

这里稍微麻烦，我们的目的是要把 list参数中的多个 id值 动态的拼接成 `where id in (1,2);` 这种效果，这里用到了 foreach 循环 

- `collection` 指的是集合或者数组，这里接受两种值，如果是集合就写 “list”，如果是数组就写“array”
- `open` 开始元素，我们需要使用“（ ” 作为开始
- `item` 循环的每一个元素，这里的每一个元素就是list中的每一个id
- `separator` 分隔符，我们拼接后的sql需要使用“,”来分割多个ID
- `close` 结束元素，我们需要使用“ ）”作为结束
- `#{id}` 循环的内容，这里是把id的值取出来了，比如循环三次就如同 : (`#{id}`，`#{id}`，`#{id}`)

根据上面的配置，这个循环会在最前面加上 "(" , 后面加上“)” , 然后取出每个item的值即ID ，然后使用分隔符“,”进行分割，最终形成  `(1,2)`  这种效果。

#### 1.3.编写测试

```java
@Test
    public void batchDelete() {
        try(SqlSession sqlSession = MyBatisUtil.openSession()){
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            employeeMapper.batchDelete(Arrays.asList(11L,12L));
            sqlSession.commit();
        }
    }
```

记得提交事务 ， 观察控制台SQL

> 9:51:57,947 DEBUG batchDelete:132 - ==>  Preparing: DELETE FROM employee where id in ( ? , ? ) 

### 2.动态SQL-foreach-批量插入

批量插入和批量删除都可以使用foreach来实现，

#### 2.1.映射器批量插入方法

```java
public interface EmployeeMapper {
	int batchInsert(List<Employee> employees);
	//....
}
```

#### 2.2.编写SQL

我们来回顾一下我们以前批量insert的SQL语法 

```sql
INSERT INTO employee(username,age,sex) VALUES("ls",11,1),("ww",12,1),("cq",13,1)
```

 那么这个语法在MyBatis中怎么实现呢？修改SQL映射文件，添加SQL：

```xml
<insert id="batchInsert" parameterType="list">
        INSERT INTO employee(username,age,sex) VALUES
        <foreach collection="list" item="emp" separator=",">
            (#{emp.username},#{emp.age},#{emp.sex})
        </foreach>
    </insert>
```

注意哦：这里跟批量删除有点不一样 ，我们需要的效果是 `VALUES ("ls",11,1),("ww",12,1),("cq",13,1)` 这种，你观察它其实没有开始符号和结束符号，因为每个值前面都有 ”(“以及”)“ ，所以不能使用 open和close 。这里的循环内容为   ` (#{emp.username},#{emp.age},#{emp.sex}) ` ,比如循环两次，用“ ， ”分割就是：

 `(#{emp.username},#{emp.age},#{emp.sex})` , `(#{emp.username},#{emp.age},#{emp.sex})`  效果。这里的item的值是emp，其实是一个Employee对象，因为list中装的就是Employee对象，当 #{xx}被替换成对应的值之后就形成了`VALUES ("ls",11,1),("ww",12,1)`这种效果



小结：这里有点绕，需要你根据最终的SQL效果 ，和foreach的特性反复思考推敲。

#### 2.3.编写测试

```java
@Test
    public void batchInsert() {
        try(SqlSession sqlSession = MyBatisUtil.openSession()){
            EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);
            //批量插入
            employeeMapper.batchInsert(Arrays.asList(
                    new Employee("ww",11,true),
                    new Employee("cq",12,true),
                    new Employee("zl",13,true)
            ));
            sqlSession.commit();
        }
    }
```

观察控制台SQL

> Preparing: INSERT INTO employee(username,age,sex) VALUES (?,?,?) , (?,?,?) , (?,?,?) 

## 五.Sql编写高级特性-多对一

**对象关联关系理解**

- 一对一：一个用户对应一个身份证号 ， 一个支付宝账号对应一个余额宝账号

  ​    任意 一方放一个外键

- 多对一：多个员工属于同一个部门

    在多方设计一个外键关联就OK

  ![1657503803783](images/1657503803783.png)

- 一对多：一个部门下面有多个员工

    还是在多方设计一个外键来处理

  ![1657503853744](images/1657503853744.png)

- 多对多：一个学生可以有多个老师，一个老师也可以有多个学生 。站在任何一方思考都是一对多，那就是多对多。

  ![1657504297195](images/1657504297195.png)

**关联查询方式**-ResultMap（关联映射）

  什么叫做关联查询！就是我们在查询数据的时候，把关联对象一起查询出来。   比如查询员工的时候要查询他所对应部门信息，首先来说查询出来要有对象来放。所以需要设计关联对象类来访，案例如下

public  class Employee

{

   //自己信息

​	private Long id;

   private String name;

  **private Department dept;**

}



查询方式有两种：

-- 方案1：嵌套查询，发送N+1条，效率低

select * from t_employee -- where id =1;
select * from t_department where id =1 

-- 方案2**：嵌套结果 一条sql,效率高**
SELECT
	e.*,
	d.id did,
	d.NAME dname 
FROM
	t_employee e
	LEFT JOIN t_department d ON e.dept_id = d.id



### 1.多对一保存

#### 1.1.创建员工部门Domain

建立对象之间的关系，Employee中包含Dept对象

```java
public class Dept {
    private Long id;
    private String name;
    private String sn;

--------------------    

public class Employee {

    private Long id;
    private String username;
    private String password;
    private Integer age;
    //关系：多对一
    private Dept dept;

    public Employee() {
    }

    public Employee(String username, String password, Integer age, Dept dept) {
        this.username = username;
        this.password = password;
        this.age = age;
        this.dept = dept;
    }
```

#### 1.2.创建员工部门表

建立表之间的关系，employee中有detp_id外键ID

```sql
CREATE TABLE `dept` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `sn` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;


--------------------------------------------

CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `dept_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

```

#### 1.3.部门DeptMapper映射器

编写DeptMapper，insert方法

```java
public interface DeptMapper {
    void insert(Dept dept);
}

```

#### 1.4.编写DeptMapper.xml映射文件

编写DeptMapper.xml，insert方法

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--
    namespace:命名空间 ，sql坐标（SQL在哪儿）
-->
<mapper namespace="cn.ronghuanet.mapper.DeptMapper">

    <insert id="insert" useGeneratedKeys="true" keyColumn="id" keyProperty="id">
        INSERT INTO dept(name,sn) values (#{name},#{sn})
    </insert>
</mapper>
```

#### 1.5.员工EmployeMapper映射器

EmployeMapper，insert方法

```java
package cn.ronghuanet.mapper;

import cn.ronghuanet.domain.Employee;

public interface EmployeeMapper {
    void insert(Employee employee);
}

```

#### 1.6.员工EmployeeMapper.xml映射文件

编写EmployeeMapper.xml的insert方法的SQL

```xml
<!--    1.多对一保存：#{dept.id} 取员工对象中关联的的 dept对象的id属性-->
<insert id="insert" useGeneratedKeys="true" keyColumn="id" keyProperty="id">
    INSERT INTO
    employee(username,password,age,dept_id)
    VALUES
    (#{username},#{password},#{age},#{dept.id})
</insert>
```

`【重要】由于Employee对象中关联的是Dept对象，数据库中的外键是dept_id，所以这里取值的是否是#{dept.id}` 

#### 1.7.测试代码

```java
 @Test
public void insert() {
    try(SqlSession sqlSession = MyBatisUtil.openSession()){

        DeptMapper deptMapper = sqlSession.getMapper(DeptMapper.class);
        EmployeeMapper employeeMapper = sqlSession.getMapper(EmployeeMapper.class);

        Dept dept = new Dept();
        dept.setName("部门1");
        deptMapper.insert(dept);
        //----------------------------------------
        Employee emp = new Employee();
        emp.setAge(11);
        emp.setUsername("xxx");
        
        emp.setDept(dept);	
        employeeMapper.insert(emp);
        sqlSession.commit();
    }
}
```

注意：先insert部门，在insert员工

`【重要】emp.setDept(dept);  把部门添加给员工对象，通过sql #{dept.id}保存给数据库`

### 2.多对一修改

```xml
<update id="updateById" parameterType="cn.ronghuanet.mybatis._03_many_2_one.domain.Employee">
    update employee
    <set>
        <if test="username != null and username != ''">
            username = #{username}
        </if>
        <if test="dept != null and dept.id != null">
            dept_id = #{dept.id}
        </if>
    </set>
</update>
```

### 3.多对一删除

晚上作业 ， 自行完成

### 4.多对一查询-嵌套结果（重要）

```xml
    <select id="selectAll" resultType="cn.ronghuanet.mybatis._03_many_2_one.domain.Employee">
        select id,username,age,sex,dept_id
        from employee
    </select>
```

这种SQL是查询不到关联对象的，如果要让Employee.dept有值，肯定要两张表关联查询，如下：

```xml
   <select id="selectAll" resultType="cn.ronghuanet.mybatis._03_many_2_one.domain.Employee">
        select
            e.id,
            e.username,
            e.age,
            e.sex,

            d.id,
            d.name
        from employee e join dept d on d.id = e.dept_id
    </select>
```

但是这个SQL也查询不到关联对象 ，ResultType只能根据查询的列把对应的值封装到实体类的属性中，Employee中的Dept是一个自定义的字段，如果查询的列明和对象中的属性名不一致，就需要用到resultMap。如下：

```xml
<resultMap id="baseResultMap" type="cn.ronghuanet.mybatis._03_many_2_one.domain.Employee">
        <id column="e_id" property="id" />
        <result column="age" property="age" />
        <result column="username" property="username" />
        <result column="sex" property="sex" />
        <result column="age" property="age" />
        <!-- 处理关联对象的映射
            property="dept"  ：对employee.dept属性的映射
            javaType="..Dept" :  employee.dept的类型 -->
        <association property="dept" javaType="cn.ronghuanet.mybatis._03_many_2_one.domain.Dept">
            <id column="d_id" property="id" />
            <result column="name" property="name" />
        </association>
</resultMap>
<select id="selectAll" resultMap="baseResultMap">
    select
    e.id as e_id,
    e.username,
    e.age,
    e.sex,

    d.id as d_id,
    d.name
    from employee e join dept d on d.id = e.dept_id
</select>
```

解释：

这里使用了ResultMap来处理结果集映射 ，因为有2个表所以查询的列都加上了别名，两个表相同的字段一定要用  as来取别名，不然会出问题 。

员工的列的映射方式跟我们之前一样的映射方式一样没有什么区别，对于employee中的dept对象需要使用<association元素来映射， property="dept" 指的是要处理那个字段 ， javaType指的是这个字段的类型，<association里面的字段映射内容跟通常的映射方式一样。

需要注意的是，如果查询的列用了别名，那么在映射的时候，column=“别名”需要跟别名。他们的映射关系如下：



![image-20200420162457107](images/image-20200420162457107.png)

### 5.多对一查询-嵌套查询（重要）

上面的方式是第一种查询方式，我们叫着嵌套结果，意思就是查询的SQL使用JSON连表的方式把要查询的内容employee和dept全部查询出来 ,然后使用ResultMap来处理结果。

还有一种方式叫嵌套查询，这种方式在查询SQL的时候，只需要查询employee表即可，不需要去连表查询Dept，而是在ResultMap中额外发一个子SQL去查询emloyee关联的dept的数据，然后映射给Employee。

总结一下区别：前者是在SQL连表查询出两个表的内容，然后在ResultMap处理关系，映射结果。而后者是在SQL查询不连表，而是在ResultMap额外发SQL查询管理的对象。

1.修改xml，使用嵌套查询

```xml
<resultMap id="baseResultMap2" type="cn.ronghuanet.mybatis._03_many_2_one.domain.Employee">
        <id column="e_id" property="id" />
        <result column="age" property="age" />
        <result column="username" property="username" />
        <result column="sex" property="sex" />
        <result column="age" property="age" />
        <association property="dept"
                     javaType="cn.ronghuanet.mybatis._03_many_2_one.domain.Dept"
                     column="dept_id"                  select="cn.ronghuanet.mybatis._03_many_2_one.mapper.DeptMapper.selectById"
                     
        />
    </resultMap>

    <select id="selectAll2" resultMap="baseResultMap">
        select
            e.id as e_id,
            e.username,
            e.age,
            e.sex,
            e.dept_id
        from employee
    </select>
```

这里我们的SQL并没有去关联dept，但是在resultMap处理结果集的时候，使用了<association 来映射 dept,

- select="....DeptMapper.selectById" : 这里的意思是额外发一条SQL去查询当前employee关联的dept
- column="dept_id" ：额外SQL的参数使用 employee表中的dept_id，

在deptMapper.xml编写查询的SQL

```xml
<mapper namespace="cn.ronghuanet.mybatis._03_many_2_one.mapper.DeptMapper">

    <select id="selectById" resultType="cn.ronghuanet.mybatis._03_many_2_one.domain.Dept">
        select id,name from dept where id = #{id}
    </select>
    //....
```

画一个流程图如下：

![image-20200420170025152](images/image-20200420170025152.png)

其实这种查询方式我们用Java代码来模拟一下就是

```java
//先查询出employee
List<Employee> emps = employeeMapper.selectAll();
//循环每个 employee
for(Employee emp : emps){
    //取出当前employee所关联的部门ID
    Long deptId = emp.getDeptId();
    //额外发送一个sql去查询关联的部门
    Dept dept = deptMapper.selectById(deptId);
    //然后把部门设置到employee中
    emp.setDept(dept);
}
```

总结：

这里讲了两种方式，他们的区别是：嵌套结果(关联查询)只会发一条SQL就把数据查询出来了，而嵌套查询(子查询)的方式会发n+1条SQL, 即：查询employee的时候发了一条SQL，而查询出来的每个emloyee都要额外发一个子查询去查询他的部门，性能不好。

选择：尽量使用嵌套结果(关联查询) ， 

### 6.懒加载配置

换包

```xml
 <!-- https://mvnrepository.com/artifact/org.mybatis/mybatis -->
        <dependency>
            <groupId>org.mybatis</groupId>
            <artifactId>mybatis</artifactId>
            <version>3.4.6</version>
        </dependency>
```

开启懒加载配置

![1657525648345](images/1657525648345.png)

```xml
<settings>
        <!--延迟加载总开关  true 开启延迟加载  false关闭延迟加载，即关联查询的时候会立刻查询关联对象-->
        <setting name="lazyLoadingEnabled" value="true"/>
        <!--侵入式延迟加载开关：
        侵入式延迟： 执行对主加载对象的查询时，不会执行对关联对象的查询。但当要访问主加载对象的详情属性时，就会马上执行关联对象的select查询
        默认是false
        查询主加载对象的任何属性时，都要执行关联对象的查询-->
        <setting name="aggressiveLazyLoading" value="false"/>
        <!-- lazyLoadTriggerMethods：指定对象的方法触发一次延迟加载。默认值：equals() clone() hashCode() ) toString() -->
        <setting name="lazyLoadTriggerMethods" value=""/>
    </settings>
```

指定某个查询不使用懒加载

![image-20210527095004311](MyBatis-笔记-DAY02.assets/image-20210527095004311.png) 

## 五.Sql编写高级特性-一对多

### 1.一对多保存

1.建立对象之间关系

```java
//一对多，一方
public class ProductType {
    private Long id;
    private String name;

    //维护关系
    private List<Product> productList = new ArrayList<>();
----------------------------------------------------------------
//一对多，多
public class Product {
    private Long id;
    private String productName;
    private Long productTypeId;
```

并且建立表的关系

2.编写ProductTypeMapper接口，insert方法

3.编写ProductMapper接口，insert方法

4.编写ProductTypeMapper.xml，insert方法

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--
    namespace:命名空间 ，sql坐标（SQL在哪儿）
-->
<mapper namespace="cn.ronghuanet.mapper.ProductTypeMapper">
    <insert id="insert" useGeneratedKeys="true" keyColumn="id" keyProperty="id">
        INSERT INTO product_type(name) values (#{name})
    </insert>
</mapper>
```

5.编写ProductMapper.xml，insert方法

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!--
    namespace:命名空间 ，sql坐标（SQL在哪儿）
-->
<mapper namespace="cn.ronghuanet.mapper.ProductMapper">


    <insert id="insert" useGeneratedKeys="true" keyColumn="id" keyProperty="id">
        INSERT INTO product(product_name,product_type_id)

        values (#{productName},#{productTypeId})
    </insert>
</mapper>
```

6.编写Dao

```java
public class ProductDaoImpl implements IProductDao {
    @Override
    public void insert(Product product) {
        try(SqlSession sqlSession = MyBatisUtil.openSession()){
            ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
            mapper.insert(product);
            sqlSession.commit();
        }
    }
```

...

7.编写测试

```java
public class One2ManyTest {

    private IProductTypeDao productTypeDao = new ProductTypeDaoImpl();
    private IProductDao productDao = new ProductDaoImpl();

    @Test
    public void testAddProductType(){

        ProductType productType = new ProductType();
        productType.setName("鼠标");
        productTypeDao.insert(productType);



        productType.setProductList(Arrays.asList(
                new Product("鼠标2",productType.getId()),
                new Product("鼠标3",productType.getId()),
                new Product("鼠标4",productType.getId())
        ));

        productType.getProductList().forEach( product -> {
            productDao.insert(product);
        });
    }

}

```

### 2.一对多查询

#### 2.1.嵌套结果(内联查询)内联查询方式

DeptMapper.xml

```xml
<resultMap id="baseResultMap" type="cn.ronghuanet.mybatis._04_one_2_many.domain.Dept">
        <id column="d_id" property="id"/>
        <result column="name" property="name" />
        <collection property="employees" ofType="cn.ronghuanet.mybatis._04_one_2_many.domain.Employee">
            <id column="e_id" property="id" />
            <result column="username" property="username" />
            <result column="age" property="age" />
            <result column="sex" property="sex" />
        </collection>
    </resultMap>
    <select id="selectAll" resultMap="baseResultMap">
        select
            d.id as d_id ,
            d.name ,

            e.id as e_id,
            e.username,
            e.age,
            e.sex
        from dept d join employee e on e.dept_id = d.id order by d.id
    </select>
```

![image-20200420191124228](images/image-20200420191124228.png)

注意：查询的SQL做一个排序，不然查询的结果中可能出现重复的部门数据。

#### 2.1.嵌套查询(子查询)

DeptMapper.xml

```xml
<mapper namespace="cn.ronghuanet.mybatis._04_one_2_many.mapper.DeptMapper">

    <resultMap id="baseResultMap2" type="cn.ronghuanet.mybatis._04_one_2_many.domain.Dept">
        <id column="d_id" property="id"/>
        <result column="name" property="name" />
        <collection property="employees"
                    ofType="cn.ronghuanet.mybatis._04_one_2_many.domain.Employee"
                    column="id"
                    select="cn.ronghuanet.mybatis._04_one_2_many.mapper.EmployeeMapper.selectByDeptId"
        />
    </resultMap>
    <select id="selectAll2" resultMap="baseResultMap2">
        select
            d.id as d_id ,
            d.name
        from dept d
    </select>
```

EmployeeMapper.xml编写selectByDeptId方法

```xml
<mapper namespace="cn.ronghuanet.mybatis._04_one_2_many.mapper.EmployeeMapper">
    <select id="selectByDeptId" resultType="cn.ronghuanet.mybatis._04_one_2_many.domain.Employee">
        select id,username ,age ,sex,dept_id
        from employee where dept_id = #{deptId}
    </select>
```

![image-20200420203112044](images/image-20200420203112044.png)

使用Java模拟上面的查询关系

```java
List<Dept> depts = deptMapper.selectAll();
for(Dept dept: depts){
	Long id =  dept.getId();
    List<Employee> emps = employeeMapper.selectByDeptId();
    dept.setEmployees(emps);
}
```





## 六.缓存的使用

![1657526035830](images/1657526035830.png)

​                 以空间换取时间

### 1.一级缓存

一级缓存默认开启，在SqlSession中，要命中一级缓存需要使用同一个SqlSession发送相同的SQL。

```java
  //通过ID查询
    @Override
    public Product selectById(Long id) throws IOException {
        try(
            //4.得到SqlSession : sql回话对象，用来执行Sql语句，包括事务的提交，回滚等
            SqlSession sqlSession = MyBatisUtil.openSession();
        ){

            ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
            //一级缓存： 相同的SqlSession,相同的SQL
            //第一次查询 ，走MySql 把结果 以    sql=对象  的方式缓存到SqlSession
            //第二次查询，还是这个SqlSesion，还是这个SQL,那么就会命中缓存 ， 直接返回值
            mapper.selectById(id);
            mapper.selectById(id);
        }
        return null;
    }
```

### 2.二级缓存

二级缓存在SqlSessionFactory中，要命中的条件是同一个SqlSessionFactory，发送相同的SQL。缓存是以namespace为单位的，不同namespace下的操作互不影响。

mapper.xml增加： 

```xml	
<cache />  开启二级缓存
```

mybatis-config.xml ：开启二级缓存

```xml
<settings>
      <setting name="cacheEnabled" value="true"></setting>
</settings>
```

实体类实现序列化接口

```java
class Employee implement Serilizable{
...
}
```

测试二级缓存 ，测试的时候，sqlSession执行完之后使用commit()才能将数据从暂存区存放到缓存中，才能看到二级缓存效果。



​    1 一级是Sqlsession级别，一般我们不会直接使用，框架为了关联查询提高效率

​    **2 二级缓存是SqlSessionFactory（整个应用中只有一份），可以使用来增强效率。**

​           默认一级开发，只需要在需要使用的映射文件中加上<cache/>,并且要缓存对象需要实现序列化接口   Serilizable

https://www.cnblogs.com/happyflyingpig/p/7739749.html





![1657527249253](images/1657527249253.png)

