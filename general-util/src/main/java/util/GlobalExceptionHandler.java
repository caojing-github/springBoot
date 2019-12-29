package util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.validation.ConstraintViolationException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static util.ThreadPoolManager.THREAD_POOL;

/**
 * 全局异常处理器
 *
 * @author CaoJing
 * @date 2019/1/18 15:57
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 中文字符范围
     */
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 未捕获异常会在这处理
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> exceptionErrorHandler(Exception e) {

        Result<Object> r = new Result<>();

        if (e instanceof CaseParseException) {
            String errorMsg = getErrorMsg(e);
            r.setData(errorMsg);
            r.setResultMsg(e.getMessage());
            return r;

        } else if (e instanceof MaxUploadSizeExceededException) {
            r.setResultMsg("上传文件不能超过5M");
            return r;

        } else if (e instanceof MissingServletRequestParameterException) {
            r.setResultMsg("请求参数不能为空");
            return r;

        }
//        else if (e instanceof feign.FeignException) {
//            log.error("其他系统微服务抛出的异常", e);
//
//            // 参照 FeignException 的 errorStatus 方法
//            if (e.getMessage().contains("; content:\n")) {
//                // 服务提供方返回的 Result
//                Result remote = JSON.parseObject(e.getMessage().split("; content:\n")[1], Result.class);
//                r.setResultMsg("执行失败!".equals(remote.getResultMsg()) ? "微服务调用失败" : remote.getResultMsg());
//                r.setData(remote.getData());
//            } else {
//                r.setResultMsg("微服务调用失败");
//                r.setData(e.getMessage());
//            }
//            return r;
//
//        }
        else if (e instanceof MethodArgumentNotValidException) {
            r.setResultMsg(e.getMessage().substring(e.getMessage().lastIndexOf("default message [") + "default message [".length()).replace("]]", ""));
            return r;

        } else if (e instanceof ConstraintViolationException) {
            r.setResultMsg(e.getMessage().split(": ")[1]);
            return r;

        } else if (e instanceof BindException) {
            r.setResultMsg(e.getMessage().substring(e.getMessage().lastIndexOf("default message [") + "default message [".length()).replace("]", ""));
            return r;
        }

        // 释放Redis连接
//        Optional.ofNullable(CURRENT_JEDIS.get()).ifPresent(x -> {
//            x.close();
//            CURRENT_JEDIS.remove();
//        });

        StringWriter stringWriter = new StringWriter();

        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            log.error("系统异常", e);

            e.printStackTrace(printWriter);
            String errorMsg = stringWriter.toString();

            sendErrorMail(e.getMessage(), errorMsg);

            // 提取错误中文信息
            if (isContainChinese(errorMsg)) {
                r.setResultMsg(errorMsg.substring(errorMsg.indexOf(':') + 1, errorMsg.indexOf("\n\t")));
            } else {
                r.setData(errorMsg);
            }
        }
        return r;
    }

    /**
     * 获取异常详细信息
     *
     * @param e 异常
     * @return String
     * @author CaoJing
     * @date 2019/10/30 17:26
     */
    public static String getErrorMsg(Exception e) {
        StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            e.printStackTrace(printWriter);
            return stringWriter.toString();
        }
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param s 待判断字符串
     * @return boolean 是否包含中文
     * @author CaoJing
     * @date 2019/08/28 11:40
     */
    private static boolean isContainChinese(String s) {
        Matcher m = CHINESE_PATTERN.matcher(s);
        return m.find();
    }

    /**
     * 异步发送邮件（阿里云和华为云不支持smtp 25端口，改用ssl 465端口发送才好使）
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @author CaoJing
     * @date 2019/10/15 15:57
     */
    public static void sendMail(String subject, String content) {
        THREAD_POOL.submit(() -> {
            Properties properties = new Properties();
            // 开启认证
            properties.setProperty("mail.smtp.auth", "true");
            // 启用调试
//            properties.setProperty("mail.debug", "true");
            // 设置链接超时
            properties.setProperty("mail.smtp.timeout", "25000");
            // 设置端口
            properties.setProperty("mail.smtp.port", "465");
            // 设置ssl端口
            properties.setProperty("mail.smtp.socketFactory.port", "465");
            properties.setProperty("mail.smtp.socketFactory.fallback", "false");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

            JavaMailSenderImpl sender = new JavaMailSenderImpl();
            sender.setHost("smtp.exmail.qq.com");
            sender.setDefaultEncoding("UTF-8");
            sender.setUsername("caojing@icourt.cc");
            sender.setPassword("i113234CJ");
            sender.setPort(465);
            sender.setJavaMailProperties(properties);

            String[] to = {"caojing@icourt.cc", "dujiang@icourt.cc"};

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setFrom("caojing@icourt.cc");
            msg.setTo(to);
            msg.setSubject(subject);
            msg.setText(content);
            sender.send(msg);
        });
    }

    /**
     * 异步打印并且异步发送邮件
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @author CaoJing
     * @date 2019/11/1 10:58
     */
    public static void infoLogAndMail(String subject, String content) {
        try {
            THREAD_POOL.submit(() -> {
                sendMail(subject, content);
                log.info(subject + ":" + content);
            }).get();
        } catch (Exception ex) {
            log.error("logInfoAndMail fail", ex);
        }
    }

    /**
     * 异步打印并且异步发送邮件
     *
     * @param subject 邮件主题
     * @param content 邮件内容
     * @author CaoJing
     * @date 2019/11/1 10:58
     */
    public static void errorLogAndMail(String subject, String content) {
        try {
            THREAD_POOL.submit(() -> {
                sendMail(subject, content);
                log.error(subject + ":" + content);
            }).get();
        } catch (Exception ex) {
            log.error("errorLogAndMail fail", ex);
        }
    }

    /**
     * 异步发送系统异常邮件（阿里云和华为云不支持smtp 25端口，改用ssl 465端口发送才好使）
     *
     * @param briefErrorMsg 异常简写
     * @param errorMsg      详细错误信息
     * @author CaoJing
     * @date 2019/10/15 15:57
     */
    public static void sendErrorMail(String briefErrorMsg, String errorMsg) {
        String subject = "【推客BUG-" + SpringContextUtils.getProperty("spring.cloud.config.profile") + "环境】" + briefErrorMsg;
        sendMail(subject, errorMsg);
    }

    /**
     * 异步打印异常并且异步发送系统异常邮件
     *
     * @param briefErrorMsgPrefix 错误信息前添加的前缀
     * @param e                   异常
     * @param methodName          方法名
     * @param methodArg           方法入参
     * @author CaoJing
     * @date 2019/11/1 10:58
     */
    public static void logAndSendErrorMail(String briefErrorMsgPrefix, Exception e, String methodName, Object... methodArg) {
        try {
            THREAD_POOL.submit(() -> {
                String briefErrorMsg = briefErrorMsgPrefix + e.getMessage() + " ";
                String methodInfo = "方法名:" + methodName + getMethodArgs(methodArg);
                sendErrorMail(briefErrorMsg, methodInfo + "\n\t" + getErrorMsg(e));
                log.error(briefErrorMsg + methodInfo, e);
            }).get();
        } catch (Exception ex) {
            log.error("logAndSendMail fail", ex);
        }
    }

    /**
     * 获取方法入参
     *
     * @param arg 方法入参
     * @return String
     * @author CaoJing
     * @date 2019/11/1 10:58
     */
    private static String getMethodArgs(Object... arg) {
        StringBuilder sb = new StringBuilder(" 方法入参:");
        if (null == arg) {
            return sb.append("null").toString();
        }
        for (int i = 0; i < arg.length; i++) {
            sb.append("参数").append(i + 1).append(":").append(JSON.toJSONString(arg[i]));
        }
        return sb.toString();
    }
}