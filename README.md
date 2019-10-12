SpringBoot系列Demo代码

SpringBoot用的是2.1.0.RELEASE，Demo代码主要参照官方文档，以及百度、google写的，每一个子工程就是一个小案例，简单明了<br/>
这里的测试例子基本上跟博客的一样，没什么修改，大家看博客就好了<br/>
具体介绍请看我的博客[《SpringBoot系列》](https://www.cnblogs.com/huanzi-qch/category/1355280.html) <br/>

[使用h2数据库](https://jingyan.baidu.com/article/c275f6ba607282e33d756784.html)

[浏览器访问h2数据库](http://localhost:8080/h2-console)

配置文件
```
spring:
  datasource:
    driverClassName: org.h2.Driver
    url: jdbc:h2:~/crawler
    username: root
    password: 123456
  jpa:
    database: MySQL
    show-sql: true
  h2:
    console:
      path: /h2-console
      enabled: true

imgPath: src/main/resources/img/
```