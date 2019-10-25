package demo.hutool;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Spring容器对象工具
 *
 * @author CaoJing
 * @date 2019/1/8 20:33
 */
@Component("springContextUtils")
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        if (null == applicationContext) {
            applicationContext = context;
        }
    }

    public static <T> T getBean(String beanId) {
        return (T) applicationContext.getBean(beanId);
    }

    public static <T> T getBean(Class<T> requiredType) {
        return applicationContext.getBean(requiredType);
    }

    /**
     * 获取系统环境变量
     *
     * @param propertyName 系统环境变量名
     * @return String       系统环境变量值
     */
    public static String getProperty(String propertyName) {
        return applicationContext.getEnvironment().getProperty(propertyName);
    }
}
