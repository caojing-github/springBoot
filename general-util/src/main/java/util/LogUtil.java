package util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 日志工具（若要对 controller 返回数据拦截放开下面注释）
 *
 * @author CaoJing
 * @since 2018/08/12 17:22
 */
//@ControllerAdvice(basePackages = "com.dididu.controller")
public class LogUtil implements ResponseBodyAdvice<Object> {

    private static final Logger LOG = LoggerFactory.getLogger(LogUtil.class);

    /**
     * 不打印 ResponseBody 的接口的 ServletPath
     */
    public static List<String> noResponseBodyLog = new ArrayList<>();

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        String servletPath = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest().getServletPath();

        if (LOG.isDebugEnabled() && !noResponseBodyLog.contains(servletPath)) {

            LOG.debug(JSONObject.toJSONString(body));
            return body;

        }

        LOG.info(JSONObject.parseObject(JSONObject.toJSONString(body)).getString("msg"));
        return body;
    }

    public static void log(HttpServletRequest request) {

        String requestURL = request.getRequestURL().toString();
        String parameters = getParameters(request);

        LOG.info(requestURL + parameters);
    }

    private static String getParameters(HttpServletRequest request) {

        Map<String, String[]> parameterMap = request.getParameterMap();
        StringBuilder builder = new StringBuilder();
        builder.append("?");
        for (String key : parameterMap.keySet()) {
            String value = request.getParameter(key);
            builder.append(key);
            builder.append("=");
            builder.append(value);
            builder.append("&");
        }
        int indexOf = builder.lastIndexOf("&");
        if (indexOf != -1) {
            builder.deleteCharAt(indexOf);
        }
        return builder.toString();
    }
}