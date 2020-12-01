package cn.huanzi.qch.springbootjarwar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.WebApplicationInitializer;

//jar包
//@SpringBootApplication
//public class SpringbootJarWarApplication {
//
//    public static void main(String[] args) {
//        SpringApplication.run(SpringbootJarWarApplication.class, args);
//    }
//
//}

//war包
@SpringBootApplication
public class Application extends SpringBootServletInitializer implements WebApplicationInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}




