# **后端主流框架-Spring-day1-基础**

## 一 课程介绍

-   1.常见的设计模式（了解）
-   2.为什么学习Spring； （了解）
-   3.初识Spring； （掌握）
-   4.Spring入门； （掌握）
-   5.Spring依赖注入； （掌握）
-   6.Spring测试； （掌握）
-   7.Spring配置细节； （掌握）

## 二 常见的设计模式

### 1 设计模式概述

#### 1.1 什么是设计模式

​	前人开发过程总结出来，一套开发代码优秀实践，被总结提炼处理成为一种开发代码模式。我们使用这些模式能让我们代码可读性更高、效率更高。

#### 1.2 设计模式分类 23

**创建型模式：对象实例化的模式，创建型模式用于解耦对象的实例化过程。**

**结构型模式：把类或对象结合在一起形成一个更大的结构。**

**行为型模式：类和对象如何交互，及划分责任和算法。**

![1657591013301](images/1657591013301.png)

### 2  常见设计模式

#### 2.1 单例模式

##### 2.1.1  单例模式使用场景

​	什么是单例模式？保障一个类只能有一个对象（实例）的代码开发模式就叫单例模式

​    什么时候使用？ 工具类！（一种做法，所有的方法都是static，还有一种单例模式让工具类只有一个实例）   某类工厂（SqlSessionFactory）

##### 2.1.2 实现方式

1. 饿汉

   ```java
   package com.ronghua._01dp._01singleton;
   
   public class _01JdbcUtil {
   
       //1 构造方法私有化，保证只有自己类能够创建
       private _01JdbcUtil(){
   
       }
   
       //2 创建一个private对象
       private static final _01JdbcUtil INSTANCE = new _01JdbcUtil();//这个地方就体现饿汉，一来就创建对象给他
   
       //3 提供一个static方法来获取你的这个单实例对象
       public static _01JdbcUtil newInstance(){
           return INSTANCE;
       }
   
   
   
       //普通方法
       public  String getConnection(){
           return "I am connection!";
       }
   }
   
   ```

2. 懒汉

   懒汉太懒了，要用的时候才能创建。

   ```java
   package com.ronghua._01dp._01singleton;
   
   public class _02JdbcUtil {
   
       //1 构造方法私有化，保证只有自己类能够创建
       private _02JdbcUtil(){
   
       }
   
       //2 创建一个private对象
       private static _02JdbcUtil INSTANCE;
   
       //3 提供一个static方法来获取你的这个单实例对象
       //方案1：方法锁,里面还有业务逻辑
       //public static synchronized  _02JdbcUtil newInstance(){
       public  static   _02JdbcUtil newInstance(){
           //方案2：锁代码块
           //synchronized (_02JdbcUtil.class){ // 100个线程哪怕有一个已经创建了也要排队
               if (INSTANCE==null){
                   //多线程来了？ T1 T2
                   synchronized (_02JdbcUtil.class){
                       if (INSTANCE==null){
                           INSTANCE = new _02JdbcUtil();
                      }
                   }
               }
           //}
   
           //此处有10W行代码....
           return INSTANCE;
       }
   
   
   
       //普通方法
       public  String getConnection(){
           return "I am connection!";
       }
   }
   
   ```

3. 枚举

   ```java
   package com.ronghua._01dp._01singleton;
   
   /**
    * 枚举：让它只有一个实例就是单例
    */
   public enum  _03JdbcUtil {
       INSTANCE;
   
       /**
        * 普通方法
        * @return
        */
       public  String getConnection(){
           return "I am connection!";
       }
   }
   
   ```

   

4. 其他

#### 2.2 工厂模式

```java
package com.ronghua._01dp._02_factory;

import java.util.Properties;

/**
 * 传入对象字节码就能创建一个对象
 */
public class ObjectFactory {

    /**
     * 对象工厂
     * @param clazzkey
     * @return
     */
    public static  Object getObject(String clazzkey){
        try {
            Properties properties = new Properties();
            properties.load(ObjectFactory.class.getClassLoader().getResourceAsStream("beans.properties"));
            String clazzName = properties.getProperty(clazzkey);
            if (clazzName==null || "".equals(clazzName))
                return null;

            return Class.forName(clazzName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

```



## 三 为什么学习Spring

开发应用时常见的问题：

### 1 代码耦合高

1. 应用程序是由一组相互协作的对象组成，一个完整的应用是由一组相互协作的对象组成。所以开发一个应用除了要开发业务逻辑之外，最多的是关注如何使这些对象协作来完成所需功能，而且要低耦合、高内聚。业务逻辑开发是不可避免的，那就需要有个框架出来帮我们来创建对象及管理这些对象之间的依赖关系；

   ```java
   import EmployeeJdbcDaoImpl;
   public class EmployeeServiceImpl {
       //如果接口和实现类在同一地方存在，那么就是高耦合，维护难度增加了。
       private IEmployeeDao employeeDao = new EmployeeJdbcDaoImpl();  //new EmployeeMybatisDaoImpl();
   }
   ```

   

2. EmployeeService对象除了完成业务逻辑功能的实现，还需要把数据通过EmployeeDao进行持久化，那么EmployeeService对象就依赖于EmployeeDao，如果EmployeeService离开了EmployeeDao就无法进行工作了；

   问题：若把IEmployeeDao的实现改成：EmployeeMybatisDaoImpl？

   传统解决方式：在每一个使用到IEmployeeDao的地方，都需要修改为new EmployeeMybatisDaoImpl（）；

   **工厂模式**：通过一个工厂来创建管理IEmployeeDao实例对象，然后在需要用到IEmployeeDao的地方直接通过工厂来获取：

   ```java
   bean.properties
   employeeDao = cn.ronghuanet.dao.impl.EmployeeMybatisDaoImpl  
   
   public class ObjectFactory{
       public  static Object getObject(String name){ //name =employeeDao
           Properties  ps = new Properties();
           ps.load(bean.properties);
           return Class.forName(name).newInstance();
           }
       }
   
   public class AServiceImpl {
       private IEmployeeDao employeeDao = ObjectFactory.getObject("employeeDao");
   }
   public class BServiceImpl {
       private IEmployeeDao employeeDao = ObjectFactory.getObject("employeeDao");
   }
   ```

   

### 2 对象之间依赖关系处理繁琐

```java
public class EmpployeeController {
	private IEmployeeService service;
}
-----------------------------------------------------------
public class EmpployeeService{
	private IEmployeeDao dao;
}
```

​	问题：如果对象有很多，且互相存在依赖关系，并且有的对象需要单例模式，有的则需要多个实例，处理起来比较繁琐；

### 3 事务控制繁琐

1.  实际开发中，某一个业务逻辑可能会牵涉多个操作（多个sql），事务开启在 service 层，是极好的。但是每个方法前都要开启事务，每个方法后都要关闭事务，这样就会导致代码臃肿，开发效率低下的问题；

    思考：如何降低业务逻辑部分之间耦合度，提高程序的可重用性，同时提高开发的效率！ \--\> **AOP**
    
    ```java
    EmployeeService{
    public void save(...){
        try{
            开启事务
            dao.save(...);
            提交事务
             
        }catch(Exception e){
            回滚事务
        }
       
    }
    public void update(...){
       try{
            开启事务
            dao.update(...);
            提交事务
             
        }catch(Exception e){
            回滚事务
        }
    }
    ```
    

## 四 初识Spring

### 1 什么是Spring

1.  Spring是一个开源的轻量级控制反转(IOC)和面向切面编程(AOP)的容器框架；

    1. 轻量级：相对于重量级（框架设计比较繁琐，配置较多，例如EJB（tomcat不支持），现在基本不用了）而言，开发使用都比较简单，功能强大；

    2. IOC（Inverse of control - 控制反转）：将创建对象的权利和依赖关系维护（字段赋值）交给Spring容器（不再使用以前new关键字创建对象）。对于某个具体的对象而言，以前是它控制其他对象，现在是所有对象都被Spring控制，所以这叫控制反转；

       ```xml
       <beans>
       
       	<bean id="employeeDao" class="...EmployeeDaoImpl">
       
       </beans>
       ```
    
       
    
    3. DI（Dependency injection）-包含ioc
    
       对于交给Spring管理某个对象，如果依赖于其他对象，他会给你自动注入（设置进来）。
    
       ```java
       public class EmployeeServiceImpl implements IEmployeeService{
       
           //spring会把它管理EmployeeDao实现自动给你设置进来
           @Autowired
       	private IEmployeeDao dao;
       }
       ```
    
       
    
    4. AOP（Aspect Oriented Programming）：将相同的逻辑抽取出来，即将业务逻辑从应用服务中分离出来。然后以拦截的方式作用在一个方法的不同位置。例如日志，事务的处理；
    
    注意事项：Spring底层原理：xml+dom4j+工厂设计模式+反射
    
    ​	**spring一个j2ee框架，主要有两个核心，第一个是ioc或Di，就是把我们的对象交给spring管理，并且有依赖关系是会自动注入。第二个是aop，面向切面编程，就是把非核心业务逻辑以切面的方式剥离处理，达到解耦的效果。**

### 2 Spring框架的好处

1.  方便解耦，降低维护难度，提高开发效率（Spring相当于是一个大的工厂，它提供的IOC思想，可以将对象的创建和依赖关系维护都交给spring管理）；

2.  spring支持AOP编程（spring提供面向切面编程，可以很方便的实现对程序进行权限拦截和运行监控等功能，可以将一些通用任务如安全、事务、日志等进行集中式管理，从而提供了更好的复用）；

3.  Spring致力于J2EE应用的各层的解决方案，而不是仅仅专注于某一层的方案。在企业级开发中，通常用于整合其他层次的框架；

4.  方便程序的测试（Spring 对junit4支持，可以通过注解测试Spring 程序，非常便捷）；

5.  方便集成各种优秀的框架（Spring并不强制应用完全依赖于Spring，开发者可自由选用Spring框架的部分或全部，也可以集成其他优秀的框架）；

6.  Spring降低了javaEE API的使用难度（Spring 对javaEE开发中非常难用的一些API，例如JDBC、javaMail、远程调用等，都提供了封装，是这些API应用难度大大降低）；

    注：Spring的DI机制降低了业务对象替换的复杂性，提高了组件之间的解耦，很好集成三方框架，降低了j2ee项目开发难度
    
     

### 3 Spring框架的模块化

1. 简单地说，模块化就是有组织地把一个大文件拆成独立并互相依赖的多个小模块；

   ![image-20220422111933402](images/image-20220422111933402.png)

2. Spring框架的功能大约由20个模块组成，这些模块按组可以分为：

   (1) Core Container（核心容器）:

   1.  Beans：负责Bean工厂中Bean的装配，所谓Bean工厂即是创建对象的工厂，Bean的装配也就是对象的创建工作；

   2.  Core：这个模块即是负责IOC（控制反转）最基本的实现；

   3.  Context：Spring的IOC容器，因大量调用Spring Core中的函数，整合了Spring的大部分功能。Bean创建好对象后，由Context负责建立Bean与Bean之间的关系并维护。所以也可以把Context看成是Bean关系的集合；

   4.  SpEl：即Spring Expression Language（Spring表达式语言）；

​	(2) Data Access/Integration（数据访问/集成）:

1.  JDBC：对JDBC的简单封装；

2.  ORM：支持数据集成框架的封装（如Mybatis，Hibernate）；

3.  OXM：即Object XML Mapper，它的作用是在Java对象和XML文档之间来回转换；

4.  JMS：生产者和消费者的消息功能的实现；

5.  Transations：事务管理；



(3) Web与远程调用：

1.  WebSocket：提供Socket通信，web端的的推送功能；

2.  Servlet：Spring MVC框架的实现；

3.  Web：包含web应用开发用到Spring框架时所需的核心类，包括自动载入

    WebApplicationContext特性的类，Struts集成类、文件上传的支持类、Filter类和大量辅助工具类；

4.  Portlet：实现web模块功能的聚合（如网站首页（Port）下面可能会有不同的子窗口（Portlet））；

(4) AOP：面向切面；

(5) Aspects：同样是面向切面的一个重要的组成部分，提供对AspectJ框架的整合；

(6) Instrumentation（设备）：相当于一个检测器，提供对JVM以及对Tomcat的检测；

(7) Messaging（消息）：Spring提供的对消息处理的功能；

(8) Test（测试）：我们在做单元测试时，Spring会帮我们初始化一些测试过程当中需要用到的资源对象；

​	![image-20220422112130901](images/image-20220422112130901.png)

## 五 Spring入门

1.  Spring提供了强大的IOC机制，能够帮助我们管理对象和依赖关系维护：

    1.  管理对象：包括对象的创建，初始化，和销毁（分情况）；

        注：通常被Spring管理的类称之为Bean，在Spring容器中的对象称之为Bean对象；

    2.  依赖关系维护：DI（Dependency Injection）依赖注入，后续再讲；

### 1 完成第一个Spring

#### 1.1   导入Spring相关jar包

​	

```xml
 <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>spring-context</artifactId>
     <version>5.2.4.RELEASE</version>
</dependency>
```

#### 1.2 **准备Spring配置文件**

1.  在classpath的根目录下新建一个applicationContext.xml配置文件，文件名可以自定义，但是通常使用applicationContext这个名字；

2.  添加文档声明和约束（这个东西不需要记忆）：

    1.  可以参考文档，中英文文档都可以；

        1.  **spring-framework-4.1.2.RELEASE\\docs\\spring-framework-reference\\pdf**

    2.  可以参考资源中的资料；

    3.  可以百度spring的配置文件；

    4.  也可以直接拿以下内容去修改
    
    ```java
    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
    http://www.springframework.org/schema/beans/spring-beans.xsd">
    	<bean id="..." class="...">
    	<!-- collaborators and configuration for this bean go here -->
    	</bean>
    </beans>
    ```
    
    

#### 1.3 **编写逻辑代码**

​	准备一个普通的Java类（MyBean）

```java
public class MyBean {
	public void hello(){
		System.out.println("hello spring...");
	}
}
```

#### 1.4 **将这个类交给Spring去管理即注册到Spring容器中**

1.  大家要记得，Spring是一个容器，我们需要把我们的类交给Spring去管理。 因为，我们的测试是创建一个普通的类，然后再通过Spring帮我们把这个类的对象创建出来就算是成功了；

2. 在配置文件中将这个Java类交给Spring管理。在applicationContext.xml中配置

   ```java
   <beans ...>
     <bean id="myBean" class="cn.ronghuanet._01_hello.MyBean"></bean>
   </beans>
   ```

3. 元素和属性讲解：

   bean元素：表示对象配置或注册标签；

   id属性：这个bean对象在Spring容器中的唯一标识，也可以使用name，常用id（唯一特性），获取这个对象的时候就可以通过这个表示来获取；

   class属性：对应对象所属类的完全限定名。注意这里可以是JDK自带的类，也可以是自己新建的类；

   注意：Spring容器中不允许有两个名字【不管是id指定还是name指定】一个的对象

   问题：我想使用或者获取Spring容器创建的这个对象或Bean，那么如何操作？

### 2 Spring容器的实例化（如何创容器对象_测试用）

1.  Spring容器对象有两种：BeanFactory和ApplicationContext；

2.  ApplicationContext继承自BeanFactory接口，拥有更多的企业级方法，推荐使用该类型；

#### 2.1 BeanFactory

1. BeanFactory是一个接口，可以通过其实现类XmlBeanFactory获取其实例。接口中有一个getBean()方法可以获取Spring容器中配置或注册的Bean对象；

   ```java
   @Test
   public void testHelloSpring() throws Exception {
   	/**
   	 *我们第一步是要启动框架，而启动框架则需要拿到Spring的核心对象
   	 *咱们学习的第一个核心对象是BeanFactory : 顾名思义，这是一个创建Bean的工厂
   	 *而Bean工厂创建对象又必需拿到配置文件中的数据
   	 *因为：我们的第一步读取配置文件，拿到BeanFactory工厂	
   	 */
   	
   	//第一步：读取资源文件
   	Resource resource = new ClassPathResource("applicationContext.xml");
   	//第二步：拿到核心对象 BeanFactory
   	BeanFactory factory = new XmlBeanFactory(resource);
   }
   ```

   

#### 2.2 ApplicationContext（推荐使用）

1. ApplicationContext的中文意思是\"应用程序上下文\"，它继承自BeanFactory接口，除了包含BeanFactory的所有功能之外，在国际化支持、资源访问（如URL和文件）、事件传播等方面进行了良好的支持，被推荐为JavaEE应用之首选，可应用在Java APP与Java Web中；

   ```java
   //加载工程classpath下的配置文件实例化
   String conf = "applicationContext.xml";
   ApplicationContext factory = new ClassPathXmlApplicationContext(conf);
   ```

   

#### 2.3 获取对象方式

**方式一：通过id直接拿到相应的Bean对象**

//通过[xml]{.ul}中配置的id拿到对象

MyBean bean = (MyBean)factory.getBean(\"myBean\");

System.*out*.println(bean);

**方式二：通过id与对象的Class对象拿到Bean对象（推荐使用）**

//通过id与对象的class拿到Bean对象

MyBean bean = factory.getBean(\"myBean\",MyBean.**class**);

System.*out*.println(bean);

#### 2.4 ApplicationContext与BeanFactory的区别【掌握】

##### 2.4.1  联系

ApplicationContext是BeanFactory的子类，拥有更多的功能与方法；

##### 2.4.2 区别

​	ApplicationContext默认是在读取配置文件的时候就会根据配置创建Bean对象（迫切加载）。而BeanFactory是在使用的时候才进行对象的创建（懒加载/延迟加载）

##### 2.4.3 扩展

1.  我们在使用ApplicationContext的时候，可以通过配置让它也变成与BeanFactory一样的懒加载：

    配置一：让所有Bean都变成懒加载，只需要在\<beans\>标签中加入default-lazy-init=\"true\"：
    
    ```java
    <beans xmlns="http://www.springframework.org/schema/beans"
    ....
    default-lazy-init="true">
    	<bean id="myBean" class="cn.ronghuanet._01_hello.MyBean"></bean>
    </beans>
    ```
    
    配置二：让其中一个Bean变成懒加载，在\<bean\>标签中加入**lazy-init=\"true\"**：
    
    ```xml
    <bean id="myBean" class="cn.ronghuanet._01_hello.MyBean" lazy-init="true"></bean>
    ```
    
    

## 六  spring管理bean两种方式

### 1 xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd"
>    
    <bean id="myBean" class="com.ronghua._04di._01xml.MyBean">

    </bean>

    <bean id="otherBean" class="com.ronghua._04di._01xml.OtherBean"></bean>
</beans>
```

### 2 注解（推荐使用）

#### 2.1 配置扫描注解

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
       http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
         http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
"
>
    <!--扫描组件：通过指定包，就是找到本包或者子孙包下面加了注解Component及@Controller等注解的类，把它交给spring管理
    就相当于我们xml当中一个一个配置-->
    <context:component-scan base-package="com.ronghua._04di._02annotation"></context:component-scan>
</beans>
```

#### 2.2 加注解

​    @Component  @Controller @ Service  @Repository等注解，后面三个是前一个子注解。其实我们任意用一个都可以，但是为了代码可读性，最好分门别类的使用 。

​    Controller就用 @Controller 注解

   Service就用@Service注解

  dao/mapper用@Repository

  其他的用@Component  

```java
@Component
public class OtherBean {
    public void hello(){
        System.out.println("OtherBean hello!");

    }
}
```



##  七Spring依赖注入

1.  IoC是一种思想，它的一个重点是在系统运行中，动态的向某个对象提供它所需要的其他对象。这一点是通过DI（Dependency Injection，依赖注入）来实现的；

2.  Spring中的对象都是由Spring进行统一管理，但是在对象中还存在属性，这些属性实际上引用的也是别的对象，那么这些对象也是由Spring来管理的；

3.  在实际使用时，我们需要给Spring中对象的属性字段赋值，这称为依赖注入DI（Dependency Injection）；

4.  依赖注入又分为xml注入和注解注入；

### 1 xml注入

顾名思义：在xml中进行配置，但是这种方式必须有对应的setter方法，所有这种注入方式又称之为属性注入或setter方法注入；

​	bean定义

```java
public class MyBean{
	private OtherBean otherBean;
	public void hello(){
		otherBean.hello();
	}
     public void setOtherBean(OtherBean otherbean){
         this.OtherBean = OtherBean
}
}

public class OtherBean{
	public void hello(){
		System.out.println("otherbean hello");
	}
}
```

xml配置

```xml
//xml配置：
<bean id="otherBean" class="cn.ronghuanet.bean.OtherBean"></bean>
<bean id="myBean" class="cn.ronghuanet.bean.MyBean">
  <property name="otherBean" ref="otherBean"></property>
</bean
```

测试

```java
//测试：main方法测试
//加载工程classpath下的配置文件实例化
String conf = "applicationContext.xml";
ApplicationContext factory = new ClassPathXmlApplicationContext(conf);
```



### 2 注解注入

顾名思义：通过注解实现注入，这种方式可以将注解写在setter方法上，也可以写在字段上，如果写在字段上可以不需要setter方法；

#### 方案一：使用@Autowired

@Autowired为Spring提供的注解，需要导入包org.springframework.beans.factory.annotation.Autowired;

```java
@Component
public class ServiceBean {
    @Autowired
    private DaoBean1 daoBean;

    public void sayHello(){
        System.out.println("hello spring!!!!");
        daoBean.hello();
    }
}
@Component
public class DaoBean1 {
    public void hello(){
        System.out.println("hello DaoBean1!!!!!");
    }
}
@Component
public class DaoBean {
    public void hello(){
        System.out.println("hello DaoBean!!!!!");
    }
}
```

最后打印的结果是`hello DaoBean1!!!!!`，说明Autowired这个注解默认是先按照类型来找的，如果该类型的bean有多个，那么就再按照名字来找。

#### 方案二：使用@Resource

@Resource由J2EE提供，需要导入包javax.annotation.Resource，Spring支持该注解

```java
public class MyBean{
    @Resource //默认按照名字匹配【名字对了，类型也必须一致】，然后按照类型匹配
    //@Resource(name="otherBean1")//指定Bean的名称
	private OtherBean otherBean;
	public void hello(){
		otherBean.hello();
	}
}

public class OtherBean{
	public void hello(){
		System.out.println("otherbean hello");
	}
}
```

@Resource默认是按照name来注入的，如果name没找到，就按照type来找。

注意事项：在实例化Bean和注入Bean对象的同时，不要将xml方式和注解方式进行混用，要么都用xml方式【今天用】，要么都用注解方式【明天学完全注解就可以用了】；

## 八 Spring测试

### 1 Spring测试介绍

1.  单元测试在我们的软件开发流程中占有举足轻重的地位；

2.  而且目前基于Java 的企业应用软件来说，Spring 已经成为了标准配置，那么如何在Spring框架中更好的使用单元测试呢？

3. Spring框架提供了对单元测试（junit4）的强大支持，我们不用在使用传统的单元测试去测试Spring功能。通过SpringJunit测试，使用注解帮我们读取配置文件和赋值，简化测试代码，提高测试效率；

   ![image-20220422135709731](images/image-20220422135709731.png)

4. 三大框架整合的时候如果Spring的配置文件不能读取，那么整个项目是跑不起来的， 而Spring的测试可以让我们在不启动服务器的情况下，使用注解读取相应的配置文件，把项目跑起来；

### 2 Spring测试步骤

**第一步：导入相应的包**	

```xml
 <dependency>
     <groupId>org.springframework</groupId>
     <artifactId>spring-test</artifactId>
     <version>5.2.4.RELEASE</version>
</dependency>
```

**第二步：编写测试类和方法**

注解：

-   @RunWith：表示先启动Spring容器，把junit运行在Spring容器中；

-   @ContextConfiguration(\"classpath:applicationContext.xml\")：表示从CLASSPATH路径去加载资源文件；

-   Autowired：表示自动装配，自动从Spring容器中取出对应bean赋值给当前使用的属性；

    Spring测试代码：

```java
/**
 * @RunWith：代表开启Spring的测试
 * 		SpringJUnit4ClassRunner：代表是Junit4的测试环境
 * 		
 * @ContextConfiguration：找到我们的核心配置文件
 * 特别注意这里路径：
 * 	方式一（web工程，正式开发时使用）：
 * 		/cn/ronghuanet/_02_test/applicationContext.xml  → 从当前类所在包找（不建议使用）
 * 		classpath:cn/ronghuanet/_02_test/applicationContext.xml  → 从classpath的根目录开始（正式开发）
 * 	方式二（测试的时候使用）：
 * 		applicationContext.xml  → 当前类所在的包下面开始查找
 * 	方式三（测试的时候使用）：
 * 		注解后面不写名称  → 在当前目录下来创建xml的名称为：测试类名-Context.xml
 * 			例：SpringTest-Context.xml。使用这种方式，这种写法好处是@ContextConfiguration可以不加参数	
 *		
 * @author Administrator
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class SpringTest {
	/**
	 * 	现在的测试环境应该是这样的
	 * 	Spring环境自行启动，所有的核心对象已经处于Spring之中，都应该由Spring自己来创建
	 * 	@Autowired的意思是自动注入，ApplcationContext这个类是Spring内部存在的，它也是一个Bean，
	 * 		Spring可以把它创建出来，在它看到上面的这个标签后，再把创建的Bean注入进来
	 */
	@Autowired
	private ApplicationContext context;
	
	/**
	 * 这个TestBean我们已经在Spring中配置好。交给Spring来管理
	 * Spring即可以来创建这个对象，然后将这个对象赋值给这个成员变量
	 */
	@Autowired
	private TestBean testBean;
	
	@Test
	public void testGetBean() throws Exception {
		System.out.println(testBean);
	}
}
```

小结：Spring测试让测试变得更加简单，需要注意配置文件的位置，需要了解几个注解：

@RunWith(SpringJunit4ClassRunner.class)

@ContextConfiguration("classpath:applicationContext.xml")

@AutoWired

## 九 Spring配置细节

### 1 Bean对象的作用域

1.  指的是我们配置的Bean是单例还是多例/原型

![image-20220422140104956](images/image-20220422140104956.png)

2.  通过Bean元素中的scope属性指定：

-   singleton：默认值，单例

-   prototype：多例

  ```xml
  <bean id="scopeBean" class="cn.ronghuanet._03_scope.MyScopeBean" scope="prototype">
  </bean>
  ```

3.  其他属性值：（一般都不用）

    ![image-20220422140205492](images/image-20220422140205492.png)

### 2 Bean对象配置懒加载

1. Spring中所有bean对象全部懒加载：配置文件的根对象中添加default-lazy-init为true

   ```xml
   <beans xmlns="http://www.springframework.org/schema/beans"
   	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   	xsi:schemaLocation="http://www.springframework.org/schema/beans
   	http://www.springframework.org/schema/beans/spring-beans.xsd"
   	default-lazy-init="true">
   </beans>
   ```

2. 单个Bean对象懒加载/延迟加载

   <bean id=\"user\" class=\"cn.itdemo.test.User\" lazy-init=\"true\"\>\</bean\>

### 3 Bean对象的生命周期

1.  Bean对象的生命周期指的是：从对象创建、初始化、调用执行到销毁的一个过程；

2.  不同作用域（singleton，prototype）的Bean，生命周期有所区别：

    1.  Spring管理的Bean对象默认是单例的；

    2.  Bean对象的实例化（创建）和初始化：

        1.  实例化实质是Spring容器调用Bean的**无参构造**创建Bean对象；

        2.  初始化实质上是Spring容器调用指定的初始化方法；

        3.  BeanFactory管理的Bean默认是在使用的时候才创建Bean对象，即延迟加载，而AppliacationContext管理的Bean默认是在容器创建的时候就会创建Bean对象，即迫切加载；

    3.  Bean对象的销毁：

        1.  实质上是Spring容器调用指定的销毁方法（并不是真正意义上的销毁Bean对象）；

        2.  在容器关闭的时候（ApplicationContext对象没有close方法，其实现类有），Spring

            容器会自动调用指定的销毁方法；
        
        ![image-20220422140326556](images/image-20220422140326556.png)
    
3. 可以通过bean元素的init-method和destroy-method属性指定初始化方法和销毁方法。但是一般我们自己不会来配置这个生命周期。而这个基本上Spring自身来使用，例如在Spring操作连接池的时候，它会在DateSource销毁的时候执行；



注解版本：

```java
package com.ronghuanet._01beanconfigdetail;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
//@Scope("prototype")
public class LifeCycleBean {
    public LifeCycleBean(){
        System.out.println("LifeCycleBean created!");
    }
    public void hello(){
        System.out.println("hello LifeCycleBean!");
    }

    //PostConstruct创建之后，其实就是初始化
    //PreDestroy销毁前，其实就是销毁
    @PostConstruct
    public void init(){
        System.out.println("LifeCycleBean init！");
    }
    @PreDestroy
    public void destroy(){
        System.out.println("LifeCycleBean destroy！");
    }
}

```



## 十 课程总结

### 1 重点

1.  Mybatis高级查询；

2.  Spring是什么，有什么作用或好处；

3.  Spring编程；

4.  Spring依赖注入；

5.  Spring测试；

6.  Spring配置细节；

### 2 难点

1.  Mybatis高级查询；

2.  Spring是什么，有什么作用或好处；

3.  Spring编程；

4.  Spring依赖注入；

5.  Spring测试；

6.  Spring配置细节；

## 十一 如何掌握？

1.  课上认真听课；

2.  完成课后练习；

3.  抓住课程重点；

## 十二  排错技巧

1.  通过异常和错误找出问题，分析问题，解决问题；

2.  Spring的测试注意路径问题（是否找到核心配置文件）；



## 十三 课后练习

1.  课堂代码1-2遍；

2.  请写一个单例模式？ （预计5分钟完成）

## 十四 面试题

1.  什么是高级查询，高级查询如何实现？ （预计5分钟完成）

2.  什么是Spring，Spring框架的作用是什么？ （预计5分钟完成）

3.  怎么理解IOC？ （预计5分钟完成）

4.  BeanFactory与ApplicationContext的区别? （预计5分钟完成）

## 十五 扩展阅读或课外阅读推荐（可选）

### 1 扩展知识

1.  关于注解@Autowired和@Resource的匹配方式：

    1.  @Autowired：默认按照类型【子类也可以】匹配，如果有多个类型一样的Bean，按照名字匹配，匹配不上报错

    2.  @Resource：默认按照名字取匹配，但是类型必须对应

### 2 课外阅读
