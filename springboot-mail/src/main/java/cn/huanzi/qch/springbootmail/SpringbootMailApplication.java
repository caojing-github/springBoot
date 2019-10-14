package cn.huanzi.qch.springbootmail;

import cn.huanzi.qch.springbootmail.serive.mail.SpringBootMailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import java.io.File;
import java.util.ArrayList;

@Slf4j
@RestController
@SpringBootApplication
public class SpringbootMailApplication {

    public static void main(String[] args) {

        /**
         * 代码配置的方式发送邮件
         */
        {
            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost("smtp.exmail.qq.com");
            sender.setDefaultEncoding("UTF-8");
            sender.setUsername("caojing@icourt.cc");
            sender.setPassword("i113234CJ");

            String[] to = {"2078068092@qq.com", "caojing0229@foxmail.com", "caojing@icourt.cc"};

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("caojing@icourt.cc");
            msg.setTo(to);
            msg.setSubject("【推客-dev环境-bug】errorMsg");
            msg.setText("具体错误...");
            sender.send(msg);
        }

        SpringApplication.run(SpringbootMailApplication.class, args);
    }

    @Autowired
    private SpringBootMailService springBootMailService;

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 基于配置文件方式发送邮件
     */
    @GetMapping("/")
    public String index() throws MessagingException {

        StopWatch watch = new StopWatch();

        watch.start("简单邮件");
        // 简单邮件
        springBootMailService.sendSimpleMail("2078068092@qq.com", "Simple Mail", "第一封简单邮件");
        watch.stop();

        watch.start("HTML格式邮件");
        // HTML格式邮件
        Context context = new Context();
        context.setVariable("username", "我的小号");
        springBootMailService.sendHtmlMail("2078068092@qq.com", "HTML Mail", templateEngine.process("mail/mail", context));
        watch.stop();

        watch.start("HTML格式邮件，带附件");
        // HTML格式邮件，带附件
        Context context2 = new Context();
        context2.setVariable("username", "我的小号（带附件）");
        ArrayList<File> files = new ArrayList<>();
        //File对象
        files.add(new File("/Users/icourt/IdeaProjects/springBoot/springboot-mail/src/main/resources/附件1.txt"));
        files.add(new File("/Users/icourt/IdeaProjects/springBoot/springboot-mail/src/main/resources/附件2.txt"));
        springBootMailService.sendAttachmentsMail("2078068092@qq.com", "Attachments Mail", templateEngine.process("mail/attachment", context2), files);
        watch.stop();

        log.info(watch.prettyPrint());

        return "欢迎访问 springboot-mail，邮件发送成功！";
    }
}
