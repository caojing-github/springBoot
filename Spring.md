IOC	控制反转 Inversion of Control
OOP	面向对象编程 Object Oriented Programming
AOP	面向切面编程 Aspect Oriented Programming

获取spring容器示例（有XML配置文件）
ApplicationContext applicationContext = new ClassPathXmlApplicationContext("bean.xml");
UserDao userDao = (UserDao) applicationContext.getBean("userDao");

获取spring容器示例（无XML配置文件）
ApplicationContext ac = new AnnotationConfigApplicationContext(SpringConfiguration.class);
//2.根据id获取对象
CustomerService cs = (CustomerService) ac.getBean("customerService");

Spring和Junit整合
@RunWith(SpringJUnit4ClassRunner.class)  //注解替换原有运行器

@ContextConfiguration("classpath:applicationContext.xml")

@Profile("dev")

@ActiveProfiles("dev")

```shell script
java -D"spring.profiles.active=test" -jar C:\Users\admin\Desktop\dididu-order-system\target\dididu-order-system.jar 
java -jar C:\Users\admin\Desktop\dididu-order-system\target\dididu-order-system.jar spring.profiles.active=test
java -jar C:\Users\admin\Desktop\dididu-order-system\target\dididu-order-system.jar --spring.profiles.active=test
java -jar dididu-order-system.jar -Dspring.profiles.active=test
```

获取容器对象
```java
    @Resource
    private ApplicationContext applicationContext;

    // 代码中调用
    applicationContext.getBean(ContentParser.class)
```
